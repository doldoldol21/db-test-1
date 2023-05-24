## JPA,QueryDsl 데이터베이스 조회 테스트

* MacOS, IntelliJ(2GB JVM option)
* mariadb 10.11 (On Docker 2GB Max Memory)
* QueryDsl 5.0

### User Entity

```java

@Entity
@TableGenerator(
        name = "USER_SEQ_GENERATOR",
        table = "sequences",
        pkColumnValue = "user_seq_id",
        allocationSize = 10000
)
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"childUsers"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "USER_SEQ_GENERATOR")
    private Long id;

    @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20) COMMENT '이름'")
    private String name;

    @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20) COMMENT '전화번호'")
    private String phoneNumber;

    @Column(nullable = false, length = 30, columnDefinition = "VARCHAR(30) COMMENT '국가'")
    private String county;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_user_id")
    private User parentUser;

    @OneToMany(mappedBy = "parentUser", fetch = FetchType.LAZY)
    private List<User> childUsers = new ArrayList<>();
}
```

### 조회 조건

* User의 ParentUser가 NOT NULL인 데이터(690,000 건)
* User엔티티와 관계 설정된 `parentUser`와 `childUsers`도 `fetch join`을 이용해 같이 불러온다.

#### 실행 쿼리문 (JPA, QueryDsl 동일)

```sql
select distinct u1_0.id,
                c1_0.parent_user_id,
                c1_0.id,
                c1_0.county,
                c1_0.name,
                c1_0.phone_number,
                u1_0.county,
                u1_0.name,
                p1_0.id,
                p1_0.county,
                p1_0.name,
                p1_0.parent_user_id,
                p1_0.phone_number,
                u1_0.phone_number
from user u1_0
         join
     user p1_0
     on p1_0.id = u1_0.parent_user_id
         left join
     user c1_0
     on u1_0.id = c1_0.parent_user_id
where u1_0.parent_user_id is not null ...(paging query)
```

### 결과

#### 1회차

```shell
---------------------------------------------
ns         %     Task name
---------------------------------------------
18026597083  005%  test01-JPA 일반 조회, List 반환
92944706208  024%  test02-JPA 페이징 적용 조회, List 반환
59127714125  015%  test03-JPA 페이징 적용 조회, Stream 반환
51292620167  013%  test04-JPA 페이징 적용 조회, Zero Offset 적용, List 반환
13010815667  003%  test05-QueryDsl 일반 조회, List 반환
95753078916  025%  test06-QueryDsl 페이징 적용 조회, List 반환
52462937125  014%  test07-QueryDsl 페이징 적용 조회, Zero Offset 적용, List 반환
```

| 번호 | 구분       | 내용                                          | 소요 시간(s) |
|----|----------|---------------------------------------------|----------|
| 1  | JPA      | 일반 조회, List 반환                              | 18.02    |
| 2  | JPA      | 페이징 적용 조회, List 반환                          | 92.94    |
| 3  | JPA      | 페이징 적용 조회, Stream 반환                        | 59.12    |
| 4  | JPA      | 페이징 적용 조회, Zero Offset 적용, List 반환          | 51.29    |
| 5  | QueryDsl | 일반 조회, List 반환                              | 13.01    |
| 6  | QueryDsl | 페이징 적용 조회, List 반환                          | 95.75    |
| 7  | QueryDsl | QueryDsl 페이징 적용 조회, Zero Offset 적용, List 반환 | 52.46    |

--- 

#### 2회차

```shell
---------------------------------------------
ns         %     Task name
---------------------------------------------
18707192875  005%  test01-JPA 일반 조회, List 반환
92391452000  024%  test02-JPA 페이징 적용 조회, List 반환
60763956459  016%  test03-JPA 페이징 적용 조회, Stream 반환
51621769458  013%  test04-JPA 페이징 적용 조회, Zero Offset 적용, List 반환
13412184625  003%  test05-QueryDsl 일반 조회, List 반환
98810123292  025%  test06-QueryDsl 페이징 적용 조회, List 반환
54494905333  014%  test07-QueryDsl 페이징 적용 조회, Zero Offset 적용, List 반환
```

| 번호 | 구분       | 내용                                          | 소요 시간(s) |
|----|----------|---------------------------------------------|----------|
| 1  | JPA      | 일반 조회, List 반환                              | 18.70    |
| 2  | JPA      | 페이징 적용 조회, List 반환                          | 92.39    |
| 3  | JPA      | 페이징 적용 조회, Stream 반환                        | 60.76    |
| 4  | JPA      | 페이징 적용 조회, Zero Offset 적용, List 반환          | 51.62    |
| 5  | QueryDsl | 일반 조회, List 반환                              | 13.41    |
| 6  | QueryDsl | 페이징 적용 조회, List 반환                          | 98.81    |
| 7  | QueryDsl | QueryDsl 페이징 적용 조회, Zero Offset 적용, List 반환 | 54.49    |

---

#### 3회차

```shell
---------------------------------------------
ns         %     Task name
---------------------------------------------
18719010083  005%  test01-JPA 일반 조회, List 반환
95338798625  025%  test02-JPA 페이징 적용 조회, List 반환
61456437583  016%  test03-JPA 페이징 적용 조회, Stream 반환
52552358625  014%  test04-JPA 페이징 적용 조회, Zero Offset 적용, List 반환
13357629333  003%  test05-QueryDsl 일반 조회, List 반환
93771825042  024%  test06-QueryDsl 페이징 적용 조회, List 반환
51593049500  013%  test07-QueryDsl 페이징 적용 조회, Zero Offset 적용, List 반환
```

| 번호 | 구분       | 내용                                 | 소요 시간(s) |
|----|----------|------------------------------------|----------|
| 1  | JPA      | 일반 조회, List 반환                     | 18.71    |
| 2  | JPA      | 페이징 적용 조회, List 반환                 | 95.33    |
| 3  | JPA      | 페이징 적용 조회, Stream 반환               | 61.45    |
| 4  | JPA      | 페이징 적용 조회, Zero Offset 적용, List 반환 | 52.55    |
| 5  | QueryDsl | 일반 조회, List 반환                     | 13.35    |
| 6  | QueryDsl | 페이징 적용 조회, List 반환                 | 93.77    |
| 7  | QueryDsl | 페이징 적용 조회, Zero Offset 적용, List 반환 | 51.59    |

--- 

### 종합

| 번호 | 구분       | 내용                                 | 1회차   | 2회차   | 3회차   | 평균    |
|----|----------|------------------------------------|-------|-------|-------|-------|
| 1  | JPA      | 일반 조회, List 반환                     | 18.02 | 18.70 | 18.71 | 18.47 |
| 2  | JPA      | 페이징 적용 조회, List 반환                 | 92.94 | 92.39 | 95.33 | 93.55 |
| 3  | JPA      | 페이징 적용 조회, Stream 반환               | 59.12 | 60.75 | 61.45 | 60.44 |
| 4  | JPA      | 페이징 적용 조회, Zero Offset 적용, List 반환 | 51.29 | 51.62 | 52.55 | 51.82 |
| 5  | QueryDsl | 일반 조회, List 반환                     | 13.01 | 13.41 | 13.35 | 13.25 |
| 6  | QueryDsl | 페이징 적용 조회, List 반환                 | 95.75 | 98.81 | 93.77 | 96.11 |
| 7  | QueryDsl | 페이징 적용 조회, Zero Offset 적용, List 반환 | 52.46 | 54.49 | 51.59 | 52.84 |

### 정리

* 서버 하드웨어가 충분하다면 전체 조회가 가장 빠른듯 싶지만 10만건 부터는 한번에 조회하기에는 유지보수 측면에서 장애가능성에 대해 부담스러운 측면이 있다.
  페이징처리를 하여 조회하는 경우 OFFSET(페이지) 번호가 뒤로 갈수록 점점 느려지기때문에 4번 또는 7번에 적용되어있는 zero offset 페이징 처리를 하는 것이 가장 좋을 듯 하다.

```
zero offset 이란 기본키를 기준으로 정렬 후 0번보다 큰 데이터 조회 시작, 
pageSize만큼 조회 후 가장 마지막 요소의 기본키를 다시 조회 조건으로 삽입한다.
그래서 해당 조회 조건의 OFFSET(페이지)은 언제나 0번이 된다.

예시) pageOption = (page = 0, pageSize = 100000);
     searchId = 0;
     
     반복문 {
       List = 조회 메서드(searchId, pageOption);
       searchId = List의 마지막 요소의 id
     }
```

* Stream으로 반환되는 케이스에 대해 다양한 테스트를 하지 않았는데 2번,3번 케이스를 보면 의외로 성능차이가 있는 걸 확인할 수 있다.
  Stream으로 받아서 병렬처리를 하면 더 시너지를 낼 수 있을 것 같다.
* 대용량 데이터를 다루는 시스템은 초기에 테이블에 대한 설계도 충분히 고려되어야 할 것 같으며, 해당되는 테이블들은 join문을 피하는 것이 바람직해 보인다.

해당 테스트는 적은 서버 리소스 & 로컬 도커 환경에서 테스트했기 때문에 서버 사양에 따라 다른 결과가 나올 수 있다. 
