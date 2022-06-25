package com.invygo.staffscheduling.repository;

import com.invygo.staffscheduling.dto.TotalShiftLengthRespDTO;
import com.invygo.staffscheduling.models.Schedule;
import com.invygo.staffscheduling.models.User;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface ScheduleCustomRepository {
    Iterable<TotalShiftLengthRespDTO> getTotalShiftLengthByUser(boolean isDescending, @Param("startDate") Date startDate,
                                                                @Param("endDate") Date endDate);

    Iterable<Schedule> findAllByUserForDateRange(@Param("user") User user,
                                                 @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
