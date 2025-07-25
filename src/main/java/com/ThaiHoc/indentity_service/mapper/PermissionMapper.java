package com.ThaiHoc.indentity_service.mapper;

import com.ThaiHoc.indentity_service.dto.request.PermissionRequest;
import com.ThaiHoc.indentity_service.dto.response.PermissionResponse;
import com.ThaiHoc.indentity_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
}
