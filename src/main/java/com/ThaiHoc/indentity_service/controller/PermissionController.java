package com.ThaiHoc.indentity_service.controller;

import com.ThaiHoc.indentity_service.dto.request.PermissionRequest;
import com.ThaiHoc.indentity_service.dto.response.ApiResponse;
import com.ThaiHoc.indentity_service.dto.response.PermissionResponse;
import com.ThaiHoc.indentity_service.entity.Permission;
import com.ThaiHoc.indentity_service.mapper.PermissionMapper;
import com.ThaiHoc.indentity_service.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @Autowired
    PermissionMapper permissionMapper;


    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest permissionRequest){
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(permissionRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<String> delete(@PathVariable String permission){
        return ApiResponse.<String>builder()
                .result(
                        permissionService.delete(permission)?"permission has been deleted":"permission name not found"
                )
                .build();
    }

}
