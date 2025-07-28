package com.ThaiHoc.indentity_service.service;

import com.ThaiHoc.indentity_service.dto.request.UserCreationRequest;
import com.ThaiHoc.indentity_service.dto.request.UserUpdateRequest;
import com.ThaiHoc.indentity_service.dto.response.UserResponse;
import com.ThaiHoc.indentity_service.entity.Role;
import com.ThaiHoc.indentity_service.entity.User;
import com.ThaiHoc.indentity_service.exception.AppException;
import com.ThaiHoc.indentity_service.exception.ErrorCode;
import com.ThaiHoc.indentity_service.mapper.UserMapper;
import com.ThaiHoc.indentity_service.repository.RoleRepository;
import com.ThaiHoc.indentity_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request){


        //role.add(Role.USER.name());
        //user.setRoles(role);

        try {
            User user = userMapper.toUser(request);

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            HashSet<Role> roleUser = new HashSet<>();

            roleUser.add(roleRepository.findById("USER").orElseThrow(() ->new AppException(ErrorCode.UNHANDLED_EXCEPTION)));

            user.setRoles(roleUser);

            userRepository.save(user);

            return userMapper.toUserResponse(user);


        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
    }


    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')") // @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers(){
        log.info("In method getUsers");

        return userMapper.toListUserResponse(userRepository.findAll());
    }

    @PostAuthorize("returnObject.userName == authentication.name || hasAuthority('SCOPE_ROLE_ADMIN')")
    public UserResponse getUser(String userId){
        return userMapper.toUserResponse( userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse getMyInfo(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUserName(name).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }


    public UserResponse updateUser(String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    public boolean deleteUser(String userId){

        if(userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;

    }
}
