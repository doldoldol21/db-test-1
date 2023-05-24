package com.example.neo4j.repository;

import com.example.neo4j.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepository extends Neo4jRepository<User, Long>{

    @Query("MATCH path=((u:User {userId: $userId}) - [CHILD_OF*]-> (p:User))" +
            "WHERE NOT (p)-[:CHILD_OF]->()" +
            "RETURN path")
    User findUser(Long userId);

    @Query("MATCH path=((u:User) - [CHILD_OF*]-> (p:User))" +
            "WHERE NOT (p)-[:CHILD_OF]->()" +
            "RETURN path SKIP $skip LIMIT $batchSize")
    List<User> findUsersWithPagination(int skip, int batchSize);
}
