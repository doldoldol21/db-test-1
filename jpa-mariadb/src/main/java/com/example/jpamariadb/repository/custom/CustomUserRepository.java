package com.example.jpamariadb.repository.custom;

import com.example.jpamariadb.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomUserRepository {
    List<User> findByParentUserIsNotNull();
    Page<User> findByParentUserIsNotNullWithPagination(Pageable pageable);
    Page<User> findByParentUserIsNotNullAndIdGreaterThanOrderByIdWithPagination(Long id, Pageable pageable);

}
