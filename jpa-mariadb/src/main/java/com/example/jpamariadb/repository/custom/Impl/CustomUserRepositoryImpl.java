package com.example.jpamariadb.repository.custom.Impl;

import com.example.jpamariadb.model.QUser;
import com.example.jpamariadb.model.User;
import com.example.jpamariadb.repository.custom.CustomUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.example.jpamariadb.model.QUser.user;

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findByParentUserIsNotNull() {
        QUser parentUser = new QUser("parentUser");
        QUser childUser = new QUser("childUser");
        return queryFactory
                .selectFrom(user)
                .join(user.parentUser, parentUser)
                .fetchJoin()
                .leftJoin(user.childUsers, childUser)
                .fetchJoin()
                .where(user.parentUser.isNotNull())
                .distinct()
                .fetch();
    }

    @Override
    public Page<User> findByParentUserIsNotNullWithPagination(Pageable pageable) {
        QUser parentUser = new QUser("parentUser");
        QUser childUser = new QUser("childUser");
        List<User> results = queryFactory
                .selectFrom(user)
                .join(user.parentUser, parentUser)
                .fetchJoin()
                .leftJoin(user.childUsers, childUser)
                .fetchJoin()
                .where(user.parentUser.isNotNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        long totalCount = Objects.requireNonNullElse(
                queryFactory
                        .select(user.id.count())
                        .from(user)
                        .where(user.parentUser.isNotNull())
                        .fetchOne(), 0L
        );

        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public Page<User> findByParentUserIsNotNullAndIdGreaterThanOrderByIdWithPagination(Long id, Pageable pageable) {
        QUser parentUser = new QUser("parentUser");
        QUser childUser = new QUser("childUser");
        List<User> results = queryFactory
                .selectFrom(user)
                .join(user.parentUser, parentUser)
                .fetchJoin()
                .leftJoin(user.childUsers, childUser)
                .fetchJoin()
                .where(user.parentUser.isNotNull(),
                        user.id.gt(id))
                .orderBy(user.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        long totalCount = Objects.requireNonNullElse(
                queryFactory
                        .select(user.id.count())
                        .from(user)
                        .where(user.parentUser.isNotNull())
                        .fetchOne(), 0L
        );

        return new PageImpl<>(results, pageable, totalCount);
    }
}
