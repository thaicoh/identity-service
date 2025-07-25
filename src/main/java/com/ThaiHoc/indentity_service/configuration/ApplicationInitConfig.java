package com.ThaiHoc.indentity_service.configuration;

import com.ThaiHoc.indentity_service.entity.User;
import com.ThaiHoc.indentity_service.enums.Role;
import com.ThaiHoc.indentity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;


    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
           if(userRepository.findByUserName("admin").isEmpty()){
               HashSet<String> role = new HashSet<>();
               role.add(Role.ADMIN.name());

               User user = User.builder()
                       .userName("admin")
                       //.roles(role)
                       .password(passwordEncoder.encode("admin"))
                       .build();

               userRepository.save(user);
               log.warn("admin user has been created with default password: admin, pls change it");
           }
        };
    }
}
