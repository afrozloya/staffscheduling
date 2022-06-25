package com.invygo.staffscheduling.repository;

import com.invygo.staffscheduling.dto.TotalShiftLength;
import com.invygo.staffscheduling.models.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

public class ScheduleCustomRepositoryImpl implements ScheduleCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ScheduleCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Iterable<TotalShiftLength> getTotalShiftLengthByUser() {
//        MatchOperation matchOperation = getMatchOperation(minPrice, maxPrice);

        return mongoTemplate.aggregate(Aggregation.newAggregation(
//                matchOperation,
                getGroupOperation(),
                getProjectOperation(),
                getSortOperation()
        ), Schedule.class, TotalShiftLength.class).getMappedResults();
    }

    private GroupOperation getGroupOperation() {
        return Aggregation.group("user")
                .last("user").as("user")
                .sum("shiftLength").as("totalShiftLength");
    }

    private ProjectionOperation getProjectOperation() {
        return Aggregation.project("user", "totalShiftLength");
    }

    private SortOperation getSortOperation() {
        return Aggregation.sort (Sort.Direction.ASC, "totalShiftLength");
    }

//    private MatchOperation getMatchOperation(float minPrice, float maxPrice) {
//        Criteria priceCriteria = Criteria.where("price").gt(minPrice).andOperator(Criteria.where("price").lt(maxPrice));
//        return Aggregation.match(priceCriteria);
//    }
}
