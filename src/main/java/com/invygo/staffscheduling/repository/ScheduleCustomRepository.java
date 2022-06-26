package com.invygo.staffscheduling.repository;

import com.invygo.staffscheduling.dto.DateRangeDTO;
import com.invygo.staffscheduling.dto.TotalShiftLengthRespDTO;
import com.invygo.staffscheduling.models.Schedule;
import com.invygo.staffscheduling.models.User;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface ScheduleCustomRepository {
    Iterable<TotalShiftLengthRespDTO> getTotalShiftLengthByUser(boolean isDescending, DateRangeDTO dateRangeDTO);

    Iterable<Schedule> findAllByUserForDateRange(User user, DateRangeDTO dateRangeDTO);

}
