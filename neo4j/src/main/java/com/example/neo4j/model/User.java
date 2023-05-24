package com.example.neo4j.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String name;
    private String phoneNumber;
    private String country;
    @Relationship(type = "CHILD_OF")
    private User parentUser;
}
