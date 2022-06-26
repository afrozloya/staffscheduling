package com.invygo.staffscheduling.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TotalShiftLengthReqDTO {
    @ApiModelProperty(notes = "Date Range", example = "{startDate: '2022-10-10, endDate: 2022-10-12'}")
    private DateRangeDTO dateRange;
    @ApiModelProperty(notes = "Descending order by total shift length", example = "true")
    private boolean descending = false;
}
