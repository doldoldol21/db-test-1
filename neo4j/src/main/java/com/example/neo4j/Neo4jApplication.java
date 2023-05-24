package com.example.neo4j;

import com.example.neo4j.model.User;
import com.example.neo4j.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.cypherdsl.core.Node;
import org.neo4j.cypherdsl.core.ResultStatement;
import org.neo4j.driver.internal.value.PathValue;
import org.neo4j.driver.types.Path;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.neo4j.cypherdsl.core.Cypher;
import org.springframework.data.neo4j.repository.query.QueryFragments;

import java.util.HashMap;
import java.util.List;


@SpringBootApplication
@EnableNeo4jRepositories
@RequiredArgsConstructor
@Slf4j
public class Neo4jApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final Neo4jTemplate neo4jTemplate;
    private final Neo4jClient neo4jClient;

    public static void main(String[] args) {
        SpringApplication.run(Neo4jApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        // spring-boot-starter-data-neo4j
        log.info("{}",userRepository.findUsersWithPagination(pageable.getPageNumber(), pageable.getPageSize()));

        // neo4j-cypher-dsl

    }

    private List<User> findAllWithPagination() {
        Node u = Cypher.node("User").named("u");
        Node p = Cypher.node("User").named("p");

        var statement = Cypher.match(Cypher.path("path").definedBy(u.relationshipTo(p, "CHILD_OF").unbounded()))
                .where(u.property("userId").eq(Cypher.literalOf(1523344L)))
                .returning("path")
                .build();

        log.info("{}", statement.getCypher());
        return neo4jTemplate.findAll(statement, User.class);
    }
}
