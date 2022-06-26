package com.invygo.staffscheduling.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
public class UserUpdateReqDTO {
    @ApiModelProperty(notes = "password", example = "your password", required = false)
    private String password;
    @ApiModelProperty(notes = "fullName", example = "your name", required = false)
    private String fullName;
    @ApiModelProperty(notes = "roles", example = "['ADMIN', 'USER']", required = false)
    private Set<String> roles;
}
