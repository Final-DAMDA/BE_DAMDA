package com.damda.back.domain;


import com.damda.back.data.common.Gender;
import com.damda.back.data.common.MemberRole;
import com.damda.back.data.common.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    @Column(columnDefinition = "TEXT")
    private String password;

    private String address;


    @Column(columnDefinition = "varchar(20)")
    private String gender;

    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "member")
    private List<ReservationSubmitForm> reservationSubmitFormList = new ArrayList<>();


}
