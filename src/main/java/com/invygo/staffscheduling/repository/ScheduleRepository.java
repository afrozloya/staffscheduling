package com.invygo.staffscheduling.repository;

import com.invygo.staffscheduling.models.Schedule;
import com.invygo.staffscheduling.models.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface ScheduleRepository extends CrudRepository<Schedule, String>, ScheduleCustomRepository {
    Iterable<Schedule> findAllByUser(User user);

}
