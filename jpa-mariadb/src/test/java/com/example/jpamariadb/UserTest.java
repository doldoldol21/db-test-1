package com.example.jpamariadb;

import com.example.jpamariadb.model.User;
import com.example.jpamariadb.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DataJpaTest
@Disabled
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public UserTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    @Disabled
    @Transactional
    void test01() {
        User user2 = userRepository.findById(2L).orElseThrow();
        User user3 = User.builder()
                .name("테스트1")
                .phoneNumber("12345")
                .parentUser(user2)
                .county("test")
                .build();
        userRepository.save(user3);

        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);

        userRepository.delete(user2);
        users = userRepository.findAll();
        users.forEach(System.out::println);

    }

    @Test
    @Disabled
    void test02() {
        // 상위가 더 이상 없는 100만
        final int TARGET_USER_COUNT = 1000000;
        List<User> userList = new ArrayList<>();
        List<User> saveList = new ArrayList<>();
        for (int i = 0; i < TARGET_USER_COUNT; i++) {
            User user = this.makeUser();
            userList.add(user);
            saveList.add(user);
            if ((i + 1) % 10000 == 0) {
                System.out.println("#1 TARGET_USER_COUNT: " + (i + 1));
                userRepository.saveAllAndFlush(saveList);
                saveList.clear();
            }
        }
        final int TARGET_USER_COUNT_2 = 690000;

        // 상위가 랜덤으로 맺어지는 100만
        // 을 노렸으나 Out Of Memory로 69만 들어감, 테스트 진행
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < TARGET_USER_COUNT_2; i++) {
            User user = this.makeUser();
            int randIdx = random.nextInt(userList.size());
            User parentUser = userList.get(randIdx);
            user.setParentUser(parentUser);
            userList.add(user);
            saveList.add(user);
            if ((i + 1) % 10000 == 0) {
                System.out.println("#2 TARGET_USER_COUNT: " + (i + 1));
                userRepository.saveAllAndFlush(saveList);
                saveList.clear();
            }
        }
    }


    private User makeUser() {
        return User.builder()
                .name(this.getRandomName())
                .phoneNumber(this.getRandomPhoneNumber())
                .county("United States")
                .build();
    }

    private String getRandomName() {
        List<String> firstName = Arrays.asList("김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권", "황", "안",
                "송", "류", "전", "홍", "고", "문", "양", "손", "배", "조", "백", "허", "유", "남", "심", "노", "정", "하", "곽", "성", "차", "주",
                "우", "구", "신", "임", "나", "전", "민", "유", "진", "지", "엄", "채", "원", "천", "방", "공", "강", "현", "함", "변", "염", "양",
                "변", "여", "추", "노", "도", "소", "신", "석", "선", "설", "마", "길", "주", "연", "방", "위", "표", "명", "기", "반", "왕", "금",
                "옥", "육", "인", "맹", "제", "모", "장", "남", "탁", "국", "여", "진", "어", "은", "편", "구", "용");
        List<String> lastName = Arrays.asList("가", "강", "건", "경", "고", "관", "광", "구", "규", "근", "기", "길", "나", "남", "노", "누", "다",
                "단", "달", "담", "대", "덕", "도", "동", "두", "라", "래", "로", "루", "리", "마", "만", "명", "무", "문", "미", "민", "바", "박",
                "백", "범", "별", "병", "보", "빛", "사", "산", "상", "새", "서", "석", "선", "설", "섭", "성", "세", "소", "솔", "수", "숙", "순",
                "숭", "슬", "승", "시", "신", "아", "안", "애", "엄", "여", "연", "영", "예", "오", "옥", "완", "요", "용", "우", "원", "월", "위",
                "유", "윤", "율", "으", "은", "의", "이", "익", "인", "일", "잎", "자", "잔", "장", "재", "전", "정", "제", "조", "종", "주", "준",
                "중", "지", "진", "찬", "창", "채", "천", "철", "초", "춘", "충", "치", "탐", "태", "택", "판", "하", "한", "해", "혁", "현", "형",
                "혜", "호", "홍", "화", "환", "회", "효", "훈", "휘", "희", "운", "모", "배", "부", "림", "봉", "혼", "황", "량", "린", "을", "비",
                "솜", "공", "면", "탁", "온", "디", "항", "후", "려", "균", "묵", "송", "욱", "휴", "언", "령", "섬", "들", "견", "추", "걸", "삼",
                "열", "웅", "분", "변", "양", "출", "타", "흥", "겸", "곤", "번", "식", "란", "더", "손", "술", "훔", "반", "빈", "실", "직", "흠",
                "흔", "악", "람", "뜸", "권", "복", "심", "헌", "엽", "학", "개", "롱", "평", "늘", "늬", "랑", "얀", "향", "울", "련");
        Collections.shuffle(firstName);
        Collections.shuffle(lastName);
        return firstName.get(0) + lastName.get(0) + lastName.get(1);
    }

    private String getRandomPhoneNumber() {
        SecureRandom rand = new SecureRandom();
        int num1 = rand.nextInt(10000);
        int num2 = rand.nextInt(10000);

        DecimalFormat df4 = new DecimalFormat("0000"); // 4 zeros
        return "010-" + df4.format(num1) + "-" + df4.format(num2);
    }

}
