package com.ThaiHoc.indentity_service.controller;

import com.ThaiHoc.indentity_service.dto.request.PermissionRequest;
import com.ThaiHoc.indentity_service.dto.request.RoleRequest;
import com.ThaiHoc.indentity_service.dto.response.ApiResponse;
import com.ThaiHoc.indentity_service.dto.response.PermissionResponse;
import com.ThaiHoc.indentity_service.dto.response.RoleResponse;
import com.ThaiHoc.indentity_service.mapper.PermissionMapper;
import com.ThaiHoc.indentity_service.service.PermissionService;
import com.ThaiHoc.indentity_service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    RoleService roleService;


    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{roles}")
    ApiResponse<String> delete(@PathVariable String roles){
        return ApiResponse.<String>builder()
                .result(
                        roleService.delete(roles)?"roles has been deleted":"roles name not found"
                )
                .build();
    }

}
