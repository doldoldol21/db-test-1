package com.example.jpamariadb.repository;

import com.example.jpamariadb.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.parentUser LEFT OUTER JOIN FETCH u.childUsers WHERE u.parentUser IS NOT NULL AND u.id > :id ORDER BY u.id")
    List<User> findByParentUserIsNotNullAndIdGreaterThanOrderByIdWithPagination(@Param("id") Long id, Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.parentUser LEFT OUTER JOIN FETCH u.childUsers WHERE u.parentUser IS NOT NULL")
    Stream<User> returnedStreamFindByParentUserIsNotNullWithPagination(Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.parentUser LEFT OUTER JOIN FETCH u.childUsers WHERE u.parentUser IS NOT NULL")
    List<User> findByParentUserIsNotNullWithPagination(Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.parentUser LEFT OUTER JOIN FETCH u.childUsers WHERE u.parentUser IS NOT NULL")
    List<User> findByParentUserIsNotNull();

}
