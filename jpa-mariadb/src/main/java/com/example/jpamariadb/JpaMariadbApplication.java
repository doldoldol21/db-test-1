package com.example.jpamariadb;

import com.example.jpamariadb.model.User;
import com.example.jpamariadb.repository.UserRepository;
import com.example.jpamariadb.repository.custom.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
@RequiredArgsConstructor
public class JpaMariadbApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CustomUserRepository customUserRepository;


    public static void main(String[] args) {
        SpringApplication.run(JpaMariadbApplication.class, args);
    }

    @Transactional(readOnly = true)
    @Override
    public void run(String... args) throws Exception {
        StopWatch stopWatch = new StopWatch();
        System.out.println("조건: User의 ParentUser가 NOT NULL인 데이터 (690,000건)");
        stopWatch.start("test01-JPA 일반 조회, List 반환");
        this.test01();
        stopWatch.stop();
        stopWatch.start("test02-JPA 페이징 적용 조회, List 반환");
        this.test02();
        stopWatch.stop();

        stopWatch.start("test03-JPA 페이징 적용 조회, Stream 반환");
        this.test03();
        stopWatch.stop();

        stopWatch.start("test04-JPA 페이징 적용 조회, Zero Offset 적용, List 반환");
        this.test04();
        stopWatch.stop();

        // querydsl
        stopWatch.start("test05-QueryDsl 일반 조회, List 반환");
        this.test05();
        stopWatch.stop();

        stopWatch.start("test06-QueryDsl 페이징 적용 조회, List 반환");
        this.test06();
        stopWatch.stop();

        stopWatch.start("test07-QueryDsl 페이징 적용 조회, Zero Offset 적용, List 반환");
        this.test07();
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }


    private void test01() {
        userRepository.findByParentUserIsNotNull();
    }

    private void test02() {
        int batchSize = 100000;
        int pageNumber = 0;
        boolean hasNextBatch = true;
        int sum = 0;
        while (hasNextBatch) {
            Pageable pageable = PageRequest.of(pageNumber, batchSize);
            List<User> users = userRepository.findByParentUserIsNotNullWithPagination(pageable);
            hasNextBatch = users.size() == batchSize;
            pageNumber++;

            sum += users.size();
        }
    }

    private void test03() {
        int batchSize = 100000;
        int pageNumber = 0;
        boolean hasNextBatch = true;
        int sum = 0;

        while (hasNextBatch) {
            Pageable pageable = PageRequest.of(pageNumber, batchSize);
            Stream<User> userStream = userRepository.returnedStreamFindByParentUserIsNotNullWithPagination(pageable);
            List<User> users = userStream.toList();
            hasNextBatch = users.size() == batchSize;
            pageNumber++;

            sum += users.size();
        }
    }

    private void test04() {
        int batchSize = 100000;
        long idNumber = 0;
        boolean hasNextBatch = true;
        int sum = 0;

        while (hasNextBatch) {
            Pageable pageable = PageRequest.of(0, batchSize);
            List<User> users = userRepository.findByParentUserIsNotNullAndIdGreaterThanOrderByIdWithPagination(idNumber, pageable);
            hasNextBatch = users.size() == batchSize;
            idNumber = users.get(users.size() - 1).getId();

            sum += users.size();
        }
    }

    private void test05() {
        customUserRepository.findByParentUserIsNotNull();
    }

    private void test06() {
        int batchSize = 100000;
        int pageNumber = 0;
        boolean hasNextBatch = true;
        int sum = 0;

        while (hasNextBatch) {
            Pageable pageable = PageRequest.of(pageNumber, batchSize);
            Page<User> userPage = customUserRepository.findByParentUserIsNotNullWithPagination(pageable);
            hasNextBatch = userPage.getNumberOfElements() == batchSize;
            pageNumber++;
            sum += userPage.getNumberOfElements();

        }
        System.out.println(sum);

    }
    private void test07() {
        int batchSize = 100000;
        long idNumber = 0;
        boolean hasNextBatch = true;
        int sum = 0;

        while (hasNextBatch) {
            Pageable pageable = PageRequest.of(0, batchSize);
            Page<User> userPage = customUserRepository.findByParentUserIsNotNullAndIdGreaterThanOrderByIdWithPagination(idNumber, pageable);
            hasNextBatch = userPage.getNumberOfElements() == batchSize;
            idNumber = userPage.getContent().get(userPage.getNumberOfElements() - 1).getId();

            sum += userPage.getNumberOfElements();
        }
        System.out.println(sum);
    }

}