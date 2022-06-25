package com.invygo.staffscheduling.controller;

import com.invygo.staffscheduling.dto.DateRangeDTO;
import com.invygo.staffscheduling.dto.ScheduleDTO;
import com.invygo.staffscheduling.dto.TotalShiftLengthReqDTO;
import com.invygo.staffscheduling.dto.TotalShiftLengthRespDTO;
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
import java.util.Date;
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

    @PostMapping("/api/schedules/my")
    @RolesAllowed({"ADMIN", "USER"})
    public Iterable<Schedule> mySchedules(@RequestBody DateRangeDTO dateRangeDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return scheduleRepository.findAllByUserForDateRange(user, dateRangeDTO.getStartDate(),
                    dateRangeDTO.getEndDate());
    }

    @PostMapping(value = "/api/schedules/user/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public Iterable<Schedule> userSchedules(@PathVariable String id, @RequestBody DateRangeDTO dateRangeDTO) {
        User user = userRepository.findById(id).orElse(null);
        return scheduleRepository.findAllByUserForDateRange(user, dateRangeDTO.getStartDate(),
                dateRangeDTO.getEndDate());
    }

    @PostMapping(value = "/api/schedules/summary")
    @RolesAllowed("ADMIN")
    public Iterable<TotalShiftLengthRespDTO> getTotalShiftByUser(@RequestBody TotalShiftLengthReqDTO reqDTO) {
        Date startDate = reqDTO.getDateRange() != null ? reqDTO.getDateRange().getStartDate() : null;
        Date endDate = reqDTO.getDateRange() != null ? reqDTO.getDateRange().getEndDate() : null;
        Iterable<TotalShiftLengthRespDTO> totalShiftLengths = scheduleRepository.getTotalShiftLengthByUser(
                reqDTO.isDescending(), startDate, endDate);
        return totalShiftLengths;
    }

}
