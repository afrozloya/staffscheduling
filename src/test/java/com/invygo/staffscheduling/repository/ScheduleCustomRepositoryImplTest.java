package com.invygo.staffscheduling.repository;

import com.invygo.staffscheduling.dto.DateRangeDTO;
import com.invygo.staffscheduling.dto.TotalShiftLengthRespDTO;
import com.invygo.staffscheduling.models.Schedule;
import com.invygo.staffscheduling.testutil.UnitTestUtils;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ScheduleCustomRepositoryImplTest {

    @InjectMocks
    ScheduleCustomRepositoryImpl customRepository;

    @Mock
    MongoTemplate mongoTemplate;


    @Test
    void getTotalShiftLengthByUser() {
        Mockito.when(mongoTemplate.aggregate(Mockito.any(Aggregation.class), Mockito.any(Class.class), Mockito.any()))
                .thenReturn(new AggregationResults<>(new ArrayList<>(), new Document()));
        Iterable<TotalShiftLengthRespDTO> dtos =
                customRepository.getTotalShiftLengthByUser(false, new DateRangeDTO());
        Mockito.verify(mongoTemplate, Mockito.times(1)).aggregate(
                Mockito.any(Aggregation.class), Mockito.any(Class.class), Mockito.any());
    }

    @Test
    void getTotalShiftLengthByUserWithNullDateRange() {
        Mockito.when(mongoTemplate.aggregate(Mockito.any(Aggregation.class), Mockito.any(Class.class), Mockito.any()))
                .thenReturn(new AggregationResults<>(new ArrayList<>(), new Document()));
        Iterable<TotalShiftLengthRespDTO> dtos =
                customRepository.getTotalShiftLengthByUser(false, null);
        Mockito.verify(mongoTemplate, Mockito.times(1)).aggregate(
                Mockito.any(Aggregation.class), Mockito.any(Class.class), Mockito.any());
    }

    @Test
    void getTotalShiftLengthByUserWithStartAndEndDate() {
        Mockito.when(mongoTemplate.aggregate(Mockito.any(Aggregation.class), Mockito.any(Class.class), Mockito.any()))
                .thenReturn(new AggregationResults<>(new ArrayList<>(), new Document()));
        Date stDate = new Date();
        DateRangeDTO dateRangeDTO = new DateRangeDTO();
        dateRangeDTO.setStartDate(stDate);
        dateRangeDTO.setEndDate(stDate);
        Iterable<TotalShiftLengthRespDTO> dtos =
                customRepository.getTotalShiftLengthByUser(false, dateRangeDTO);
        Mockito.verify(mongoTemplate, Mockito.times(1)).aggregate(
                Mockito.any(Aggregation.class), Mockito.any(Class.class), Mockito.any());
    }


    @Test
    void getTotalShiftLengthByUserDescOrder() {
        Mockito.when(mongoTemplate.aggregate(Mockito.any(Aggregation.class), Mockito.any(Class.class), Mockito.any()))
                .thenReturn(new AggregationResults<>(new ArrayList<>(), new Document()));
        Iterable<TotalShiftLengthRespDTO> dtos =
                customRepository.getTotalShiftLengthByUser(true, new DateRangeDTO());
        Mockito.verify(mongoTemplate, Mockito.times(1)).aggregate(
                Mockito.any(Aggregation.class), Mockito.any(Class.class), Mockito.any());
    }


    @Test
    void findAllByUserForDateRange() {
        Iterable<Schedule> schedules =
                customRepository.findAllByUserForDateRange(UnitTestUtils.getNewUser(), new DateRangeDTO());
        assertEquals(0, ((Collection) schedules).size());
    }

    @Test
    void convertToUtilDate() {
        LocalDate origDate = LocalDate.now();
        Date utilDate = customRepository.convertToUtilDate(origDate);
        LocalDate retVal = customRepository.convertToLocalDate(utilDate);
        assertEquals(origDate, retVal);
    }
}