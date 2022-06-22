package com.invygo.staffscheduling.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
    private String id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    private String email;
    private String password;
    private String fullName;
    private boolean enabled;
    @DBRef
    private Set<Role> roles;
}
