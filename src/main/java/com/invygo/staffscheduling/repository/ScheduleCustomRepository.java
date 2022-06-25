package com.invygo.staffscheduling.repository;

import com.invygo.staffscheduling.dto.TotalShiftLength;
import com.invygo.staffscheduling.models.Schedule;
import com.invygo.staffscheduling.models.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface ScheduleCustomRepository {
    Iterable<TotalShiftLength> getTotalShiftLengthByUser();
}
