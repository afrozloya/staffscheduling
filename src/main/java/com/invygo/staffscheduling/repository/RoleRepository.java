package com.invygo.staffscheduling.repository;

import com.invygo.staffscheduling.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRole(String role);
}
