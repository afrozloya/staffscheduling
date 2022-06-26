package com.invygo.staffscheduling.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DateRangeDTO {
    @ApiModelProperty(notes = "Start Date", example = "2022-10-13")
    private Date startDate;
    @ApiModelProperty(notes = "End Date", example = "2022-10-13")
    private Date endDate;
}
