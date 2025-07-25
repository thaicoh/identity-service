package com.ThaiHoc.indentity_service.mapper;

import com.ThaiHoc.indentity_service.dto.request.RoleRequest;
import com.ThaiHoc.indentity_service.dto.response.RoleResponse;
import com.ThaiHoc.indentity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);


    RoleResponse toRoleResponse(Role role);
}
