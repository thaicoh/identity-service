package com.ThaiHoc.indentity_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequest {
    private String name;
    private String description;
    private Set<String> permissions;
}
