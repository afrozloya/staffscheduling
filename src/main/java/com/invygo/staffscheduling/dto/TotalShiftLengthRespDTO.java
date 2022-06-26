package com.invygo.staffscheduling.dto;

import com.invygo.staffscheduling.models.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalShiftLengthRespDTO {
    @ApiModelProperty(notes = "User Info")
    private User user;
    @ApiModelProperty(notes = "Total Shift Length", example = "20")
    private int totalShiftLength;
}
