package com.invygo.staffscheduling.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DateRangeDTO {
    private Date startDate;
    private Date endDate;
}
