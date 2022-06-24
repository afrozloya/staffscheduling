package com.invygo.staffscheduling.controller;

import com.invygo.staffscheduling.dto.ScheduleDTO;
import com.invygo.staffscheduling.models.Schedule;
import com.invygo.staffscheduling.models.User;
import com.invygo.staffscheduling.repository.ScheduleRepository;
import com.invygo.staffscheduling.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

@RestController
public class ScheduleController {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/api/schedules")
    @RolesAllowed({"ADMIN", "USER"})
    public Iterable<Schedule> schedule() {
        return scheduleRepository.findAll();
    }

    @PostMapping(value = "/api/schedules")
    @RolesAllowed("ADMIN")
    public String save(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        schedule.setShiftLength(Integer.parseInt(scheduleDTO.getShiftLength()));
        schedule.setWorkDate(scheduleDTO.getWorkDate());
        schedule.setUser(userRepository.findById(scheduleDTO.getUser()).orElse(null)); //todo error back!
        schedule = scheduleRepository.save(schedule);
        return schedule.getId();
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/api/schedules/{id}")
    public Optional<Schedule> show(@PathVariable String id) {
        return scheduleRepository.findById(id);
    }

    @RolesAllowed("ADMIN")
    @PutMapping("/api/schedules/{id}")
    public Schedule update(@PathVariable String id, @RequestBody ScheduleDTO scheduleUpd) {
        Schedule schedule = scheduleRepository.findById(id).orElse(null);
        if(schedule!=null){
            if (scheduleUpd.getShiftLength() != null)
                schedule.setShiftLength(Integer.parseInt(scheduleUpd.getShiftLength()));
            if (scheduleUpd.getUser() != null)
                schedule.setUser(userRepository.findById(scheduleUpd.getUser()).orElse(null)); //todo error back!
            if (scheduleUpd.getWorkDate() != null)
                schedule.setWorkDate(scheduleUpd.getWorkDate());
            scheduleRepository.save(schedule);
        }
        return schedule;
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping("/api/schedules/{id}")
    public String delete(@PathVariable String id) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        scheduleRepository.delete(schedule.get());

        return "schedule deleted";
    }

    @GetMapping(value = "/api/schedules/my")
    @RolesAllowed({"ADMIN", "USER"})
    public Iterable<Schedule> mySchedules() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return scheduleRepository.findAllByUser((User) auth.getPrincipal());
    }

    @GetMapping(value = "/api/schedules/user/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public Iterable<Schedule> userSchedules(@PathVariable String id) {
        User user = userRepository.findById(id).orElse(null);
        return scheduleRepository.findAllByUser(user);
    }


}
