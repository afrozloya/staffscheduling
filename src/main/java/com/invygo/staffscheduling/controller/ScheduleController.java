package com.invygo.staffscheduling.controller;

import com.invygo.staffscheduling.dto.DateRangeDTO;
import com.invygo.staffscheduling.dto.ScheduleDTO;
import com.invygo.staffscheduling.dto.TotalShiftLengthReqDTO;
import com.invygo.staffscheduling.dto.TotalShiftLengthRespDTO;
import com.invygo.staffscheduling.models.Schedule;
import com.invygo.staffscheduling.models.User;
import com.invygo.staffscheduling.repository.ScheduleRepository;
import com.invygo.staffscheduling.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "Get all schedules", notes = "Returns list of schedules")
    @GetMapping("/api/schedules")
    @RolesAllowed({"ADMIN", "USER"})
    public Iterable<Schedule> findAllSchedules() {
        return scheduleRepository.findAll();
    }

    @ApiOperation(value = "Add a new schedule based on given info", notes = "Returns newly added schedule id")
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

    @ApiOperation(value = "Get schedule by id", notes = "Returns one schedule based on id provided")
    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/api/schedules/{id}")
    public Optional<Schedule> findScheduleById(@PathVariable String id) {
        return scheduleRepository.findById(id);
    }

    @ApiOperation(value = "Update schedule by id", notes = "Returns updated schedule information")
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

    @ApiOperation(value = "Delete schedule by id", notes = "Deletes one schedule based on id provided")

    @RolesAllowed("ADMIN")
    @DeleteMapping("/api/schedules/{id}")
    public String delete(@PathVariable String id) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        scheduleRepository.delete(schedule.get());

        return "schedule deleted";
    }


    @ApiOperation(value = "Get my schedules", notes = "Returns list of schedules for logged in user")
    @PostMapping("/api/schedules/my")
    @RolesAllowed({"ADMIN", "USER"})
    public Iterable<Schedule> mySchedules(@RequestBody DateRangeDTO dateRangeDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return scheduleRepository.findAllByUserForDateRange(user, dateRangeDTO);
    }

    @ApiOperation(value = "Get one user schedules", notes = "Returns list of schedules for given user")
    @PostMapping(value = "/api/schedules/user/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public Iterable<Schedule> userSchedules(@PathVariable String id, @RequestBody DateRangeDTO dateRangeDTO) {
        User user = userRepository.findById(id).orElse(null);
        return scheduleRepository.findAllByUserForDateRange(user, dateRangeDTO);
    }

    @ApiOperation(value = "Get total shift length per user",
            notes = "Returns list of sum of shift length for given date range")
    @PostMapping(value = "/api/schedules/summary")
    @RolesAllowed("ADMIN")
    public Iterable<TotalShiftLengthRespDTO> getTotalShiftByUser(@RequestBody TotalShiftLengthReqDTO reqDTO) {
        Iterable<TotalShiftLengthRespDTO> totalShiftLengths = scheduleRepository.getTotalShiftLengthByUser(
                reqDTO.isDescending(), reqDTO.getDateRange());
        return totalShiftLengths;
    }

}
