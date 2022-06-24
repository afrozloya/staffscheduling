package com.invygo.staffscheduling.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserUpdateReqDTO {
    private String password;
    private String fullName;
    private Set<String> roles;
}
