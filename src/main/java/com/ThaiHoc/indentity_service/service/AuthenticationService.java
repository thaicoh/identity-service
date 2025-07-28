package com.ThaiHoc.indentity_service.service;

import com.ThaiHoc.indentity_service.dto.request.AuthenticationRequest;
import com.ThaiHoc.indentity_service.dto.request.IntrospectRequest;
import com.ThaiHoc.indentity_service.dto.request.LogoutRequest;
import com.ThaiHoc.indentity_service.dto.request.RefreshTokenRequest;
import com.ThaiHoc.indentity_service.dto.response.AuthenticationResponse;
import com.ThaiHoc.indentity_service.dto.response.IntrospectResponse;
import com.ThaiHoc.indentity_service.entity.InvalidatedToken;
import com.ThaiHoc.indentity_service.entity.User;
import com.ThaiHoc.indentity_service.exception.AppException;
import com.ThaiHoc.indentity_service.exception.ErrorCode;
import com.ThaiHoc.indentity_service.repository.InvalidatedTokenRepository;
import com.ThaiHoc.indentity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Value("${jwt.signerKey}")
    protected String  SIGNER_KEY;

    private static final Logger log = LogManager.getLogger(AuthenticationService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    InvalidatedTokenRepository invalidatedTokenRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request){

        var user = userRepository.findByUserName(request.getUserName()).orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated =   passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }


        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();

    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean valid = true;

        try {
            verifyToken(token);
        }catch (AppException exception){
           valid = false;
        }

        return IntrospectResponse.builder()
                .valid(valid)
                .build();
    }

    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("thaihoc.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
          throw new RuntimeException(e);
        }
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");

        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());

                if(!CollectionUtils.isEmpty(role.getPermissions()))
                     role.getPermissions().forEach(permission -> {
                         stringJoiner.add(permission.getName());
                     });

            });
        }
        return stringJoiner.toString();
    }

    public SignedJWT verifyToken(String token) throws JOSEException, ParseException{

        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified =  signedJWT.verify(jwsVerifier);

        if (!verified && exp.after(new Date()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jti = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        invalidatedTokenRepository.save(
                InvalidatedToken.builder()
                        .id(jti)
                        .expiryTime(expiryTime)
                        .build()
        );
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        // Verify token
        var signedJWT =  verifyToken(request.getToken());

        // Add old token into invalidatedTokenRepository
        invalidatedTokenRepository.save(
                InvalidatedToken.builder()
                        .expiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                        .id(signedJWT.getJWTClaimsSet().getJWTID())
                        .build()
        );

        //Create new token
            // get user info from request token
        var userName = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUserName(userName).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

            // create new token

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();

    }
}
