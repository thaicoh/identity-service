package com.ThaiHoc.indentity_service.service;

import com.ThaiHoc.indentity_service.dto.request.RoleRequest;
import com.ThaiHoc.indentity_service.dto.response.RoleResponse;
import com.ThaiHoc.indentity_service.entity.Role;
import com.ThaiHoc.indentity_service.mapper.RoleMapper;
import com.ThaiHoc.indentity_service.repository.PermissionRepository;
import com.ThaiHoc.indentity_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);
        var permissitons = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permissitons));
        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll(){
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public boolean delete(String id){
        if(roleRepository.existsById(id)){
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
