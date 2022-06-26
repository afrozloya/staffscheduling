package com.invygo.staffscheduling.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginDTO {
    @ApiModelProperty(notes = "Email Id", example = "yourid@gmail.com", required = true)
    @NotBlank(message = "EMail is mandatory")
    @Email
    private String email;
    @ApiModelProperty(notes = "Password", example = "yourpassword", required = true)
    @NotBlank(message = "Password is mandatory")
    private String password;
}
