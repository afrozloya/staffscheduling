package com.invygo.staffscheduling.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ScheduleDTO {
    @ApiModelProperty(notes = "Work Date", example = "2022-10-13")
    private Date workDate;
    @ApiModelProperty(notes = "Shift Length", example = "5")
    private String shiftLength;
    @ApiModelProperty(notes = "User", example = "62b61ba5bb363c171bda215c")
    private String user;
}
