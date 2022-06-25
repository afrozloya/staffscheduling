package com.invygo.staffscheduling.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TotalShiftLengthReqDTO {
    private DateRangeDTO dateRange;
    private boolean descending = false;
}
