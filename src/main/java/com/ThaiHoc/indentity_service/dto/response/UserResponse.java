package com.ThaiHoc.indentity_service.dto.response;

import com.ThaiHoc.indentity_service.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private LocalDate yob;
    //private String password;
    private Set<RoleResponse> roles;
}
