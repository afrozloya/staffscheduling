package com.invygo.staffscheduling.dto;

import com.invygo.staffscheduling.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalShiftLength {
    private User user;
    private int totalShiftLength;
}
