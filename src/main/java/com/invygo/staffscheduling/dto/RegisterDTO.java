package com.invygo.staffscheduling.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterDTO extends LoginDTO{
    private Set<String> roles;
}
