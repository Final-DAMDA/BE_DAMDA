package com.damda.back.domain;


import com.damda.back.data.common.MemberRole;
import com.damda.back.data.common.MemberStatus;
import lombok.*;
import org.aspectj.apache.bcel.classfile.Code;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Table(name = "member",indexes = @Index(name = "idx_username",columnList = "username"))
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "username")
    private String username;

    @Column(columnDefinition = "TEXT")
    private String password;

    private String address;


    @Column(columnDefinition = "varchar(20)")
    private String gender;

    @Column(unique = true)
    private String phoneNumber;

    private String profileImage;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Builder.Default
    @BatchSize(size = 100)
    @OrderBy("createdAt DESC ")
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "member")
    private List<ReservationSubmitForm> reservationSubmitFormList = new ArrayList<>();


    private String discountCode;

    public void changeCode(String code){
        this.discountCode = code;
    }
    public void changeMemo(String memo){
        this.memo = memo;
    }

}
