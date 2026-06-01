package org.example.springdtovalidation.model;

import lombok.Builder;
import lombok.With;
import org.springframework.data.mongodb.core.mapping.Document;

@With
@Builder
@Document("User")
public record User(String id, String username, int age, String email, String password) { }
