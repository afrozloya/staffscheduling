package com.invygo.staffscheduling.repository;

import com.invygo.staffscheduling.models.Schedule;
import com.invygo.staffscheduling.models.User;
import org.springframework.data.repository.CrudRepository;

public interface ScheduleRepository extends CrudRepository<Schedule, String> {
    Iterable<Schedule> findAllByUser(User user);
}
