package com.invygo.staffscheduling.repository;

import com.invygo.staffscheduling.dto.DateRangeDTO;
import com.invygo.staffscheduling.dto.TotalShiftLengthRespDTO;
import com.invygo.staffscheduling.models.Schedule;
import com.invygo.staffscheduling.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ScheduleCustomRepositoryImpl implements ScheduleCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ScheduleCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Iterable<TotalShiftLengthRespDTO> getTotalShiftLengthByUser(boolean isDescending,
                                                                       DateRangeDTO dateRangeDTO) {
        Criteria workDateCriteria = workDateCriteria = getWorkCriteria(dateRangeDTO);
        MatchOperation matchOperation = Aggregation.match(workDateCriteria);
        GroupOperation groupOperation = Aggregation.group("user")
                .last("user").as("user")
                .sum("shiftLength").as("totalShiftLength");
        ProjectionOperation projectionOperation = Aggregation.project("user", "totalShiftLength");
        Sort.Direction sortDir = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        SortOperation sortOperation = Aggregation.sort(sortDir, "totalShiftLength");
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                matchOperation,
                groupOperation,
                projectionOperation,
                sortOperation
        ), Schedule.class, TotalShiftLengthRespDTO.class).getMappedResults();
    }

    @Override
    public Iterable<Schedule> findAllByUserForDateRange(User user, DateRangeDTO dateRangeDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user").is(user));
        Criteria workDateCriteria = workDateCriteria = getWorkCriteria(dateRangeDTO);
        query.addCriteria(workDateCriteria);
        Iterable<Schedule> schedules = mongoTemplate.find(query, Schedule.class);
        return schedules;
    }

    private Criteria getWorkCriteria(DateRangeDTO dateRangeDTO) {
        Criteria workDateCriteria = null;
        Date startDate = dateRangeDTO != null ? dateRangeDTO.getStartDate() : null;
        Date endDate = dateRangeDTO != null ? dateRangeDTO.getEndDate() : null;

        LocalDate today = LocalDate.now();
        LocalDate lastYear = today.minusYears(1);
        if (startDate == null || lastYear.isAfter(convertToLocalDate(startDate))) { //due to this cork criteria can never be null
            startDate = convertToUtilDate(lastYear);
        }
        workDateCriteria = Criteria.where("workDate");
        if (startDate != null)
            workDateCriteria = workDateCriteria.gte(startDate);
        if (endDate != null)
            workDateCriteria = workDateCriteria.lte(endDate);
        return workDateCriteria;
    }

    public LocalDate convertToLocalDate(Date dateToConvert) {
        return LocalDate.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }

    public Date convertToUtilDate(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
