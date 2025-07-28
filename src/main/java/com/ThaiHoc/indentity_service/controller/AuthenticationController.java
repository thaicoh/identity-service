package com.ThaiHoc.indentity_service.controller;

import com.ThaiHoc.indentity_service.dto.request.AuthenticationRequest;
import com.ThaiHoc.indentity_service.dto.request.IntrospectRequest;
import com.ThaiHoc.indentity_service.dto.request.LogoutRequest;
import com.ThaiHoc.indentity_service.dto.request.RefreshTokenRequest;
import com.ThaiHoc.indentity_service.dto.response.ApiResponse;
import com.ThaiHoc.indentity_service.dto.response.AuthenticationResponse;
import com.ThaiHoc.indentity_service.dto.response.IntrospectResponse;
import com.ThaiHoc.indentity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
       AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);

       return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationResponse)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse introspectResponse = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .result(introspectResponse)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void>  logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException{

        authenticationService.logout(request);

        return ApiResponse.<Void>builder() // Void k can .result
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationResponse)
                .build();
    }
}
