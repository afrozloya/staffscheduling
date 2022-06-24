package com.invygo.staffscheduling.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ScheduleDTO {
    private Date workDate;
    private String shiftLength;
    private String user;
}
