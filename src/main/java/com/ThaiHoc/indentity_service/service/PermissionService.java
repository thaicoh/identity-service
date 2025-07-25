package com.ThaiHoc.indentity_service.service;

import com.ThaiHoc.indentity_service.dto.request.PermissionRequest;
import com.ThaiHoc.indentity_service.dto.response.PermissionResponse;
import com.ThaiHoc.indentity_service.entity.Permission;
import com.ThaiHoc.indentity_service.mapper.PermissionMapper;
import com.ThaiHoc.indentity_service.mapper.UserMapper;
import com.ThaiHoc.indentity_service.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
     private PermissionRepository permissionRepository;

    @Autowired
    private PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionRepository.save(permissionMapper.toPermission(request));
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        var permission =  permissionRepository.findAll();
        return permission.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public boolean delete(String permissionName){
        if(permissionRepository.existsById(permissionName)){
            permissionRepository.deleteById(permissionName);
            return true;
        }
        return false;
    }
}
