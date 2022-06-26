package com.invygo.staffscheduling.controller;

import com.invygo.staffscheduling.dto.DateRangeDTO;
import com.invygo.staffscheduling.dto.ScheduleDTO;
import com.invygo.staffscheduling.dto.TotalShiftLengthReqDTO;
import com.invygo.staffscheduling.dto.TotalShiftLengthRespDTO;
import com.invygo.staffscheduling.models.Schedule;
import com.invygo.staffscheduling.repository.ScheduleRepository;
import com.invygo.staffscheduling.repository.UserRepository;
import com.invygo.staffscheduling.testutil.UnitTestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class ScheduleControllerTest {

    @InjectMocks
    ScheduleController scheduleController;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    UserRepository userRepository;

    @Test
    void testFindAllSchedules() {
        Iterable<Schedule> schedules = scheduleController.findAllSchedules();
        Mockito.verify(scheduleRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testFindScheduleById() {
        Optional<Schedule> schedule = scheduleController.findScheduleById("1");
        Mockito.verify(scheduleRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    void testSaveSuccess() {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setUser("kkl");
        scheduleDTO.setShiftLength("1");
        scheduleDTO.setWorkDate(new Date());
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(UnitTestUtils.getNewUser()));
        Mockito.when(scheduleRepository.save(Mockito.any())).thenReturn(new Schedule());
        String res = scheduleController.save(scheduleDTO);
        Mockito.verify(scheduleRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testUpdateAllField() {
        Mockito.when(scheduleRepository.findById(Mockito.any())).thenReturn(Optional.of(UnitTestUtils.getNewSchedule()));
        ScheduleDTO updateReq = new ScheduleDTO();
        updateReq.setUser("kkl");
        updateReq.setShiftLength("1");
        updateReq.setWorkDate(new Date());
        Schedule response = scheduleController.update("1", updateReq);
        Mockito.verify(scheduleRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testUpdateNone() {
        Mockito.when(scheduleRepository.findById(Mockito.any())).thenReturn(Optional.of(UnitTestUtils.getNewSchedule()));
        Schedule response = scheduleController.update("1", new ScheduleDTO());
        Mockito.verify(scheduleRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testUpdateNotExist() {
        Mockito.when(scheduleRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Schedule response = scheduleController.update("1", new ScheduleDTO());
        Mockito.verify(scheduleRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void testDelete() {
        Mockito.when(scheduleRepository.findById(Mockito.any())).thenReturn(Optional.of(UnitTestUtils.getNewSchedule()));
        String response = scheduleController.delete("1");
        Mockito.verify(scheduleRepository, Mockito.times(1)).delete(Mockito.any());
    }

//    @WithUserDetails("abc@gmail.com")
//    @Test
//    void testMySchedules() {
//        Iterable<Schedule>  schedules = scheduleController.mySchedules( new DateRangeDTO());
//        Mockito.verify(scheduleRepository, Mockito.times(1)).findAllByUserForDateRange(
//                Mockito.any(), Mockito.any());
//    }

    @Test
    void testUserSchedules() {
        Iterable<Schedule>  schedules = scheduleController.userSchedules("1", new DateRangeDTO());
        Mockito.verify(scheduleRepository, Mockito.times(1)).findAllByUserForDateRange(
                Mockito.any(), Mockito.any());
    }

    @Test
    void testGetTotalShiftByUser() {
        TotalShiftLengthReqDTO totalShiftReqDTO = new TotalShiftLengthReqDTO();
        totalShiftReqDTO.setDescending(true);
        totalShiftReqDTO.setDateRange(new DateRangeDTO());
        Iterable<TotalShiftLengthRespDTO>  totalShiftByUser =
                scheduleController.getTotalShiftByUser(totalShiftReqDTO);
        Mockito.verify(scheduleRepository, Mockito.times(1)).getTotalShiftLengthByUser(
                Mockito.anyBoolean(), Mockito.any());
    }
}