package com.invygo.staffscheduling.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "schedules")
public class Schedule {
    @Id
    private String id;
    private Date workDate;
    private int shiftLength;
    @DBRef
    private User user;
    public Schedule() {
    }

    public Schedule(Date workDate, int shiftLength, User user) {
        this.workDate = workDate;
        this.shiftLength = shiftLength;
        this.user = user;
    }
}
