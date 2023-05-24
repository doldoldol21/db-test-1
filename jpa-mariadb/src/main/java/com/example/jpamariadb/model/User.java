package com.example.jpamariadb.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Builder
    public User(Long id,
                String name,
                String phoneNumber,
                String county,
                User parentUser
    ) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.county = county;
        this.childUsers = new ArrayList<>();
        this.setParentUser(parentUser);
    }

    public void addChildUser(User childUser) {
        this.childUsers.add(childUser);
    }

    public void setParentUser(User parentUser) {
        this.parentUser = parentUser;
        if (Objects.nonNull(parentUser)) {
            parentUser.addChildUser(this);
        }
    }

    @PreRemove
    private void delete(){
        this.childUsers.forEach(user -> user.setParentUser(null));
        this.childUsers.clear();
        this.parentUser.getChildUsers().remove(this);
        this.parentUser = null;
    }


}

