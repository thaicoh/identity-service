package com.ThaiHoc.indentity_service.dto.request;

import com.ThaiHoc.indentity_service.exception.ErrorCode;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    private String userName;
    private String firstName;
    private String lastName;


    private LocalDate yob;

    @Size(min = 8, message = "PASSWORD_INVALID")
    private String password;
}
