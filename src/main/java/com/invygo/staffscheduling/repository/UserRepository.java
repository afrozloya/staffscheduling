package com.invygo.staffscheduling.repository;

import com.invygo.staffscheduling.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
