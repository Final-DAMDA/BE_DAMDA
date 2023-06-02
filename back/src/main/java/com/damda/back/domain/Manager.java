package com.damda.back.domain;

import com.damda.back.domain.area.DistrictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DayOfWeek;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "manager_tb")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne  // 연관관계 어떻게 할지
    @JoinColumn(name = "member_id")
    private Member userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek activityDay;  // 테이블 따로 빼서 one to many 양방향

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DistrictEnum activityArea;  // 테이블 따로 빼서 one to many 양방향

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificateStatus certificateStatus;  // List로 받아야 함, db에 어떻게 저장? 테이블로 빼기

    @Column(nullable = false)
    private boolean vehicle;

    @Column(nullable = false)
    private boolean fieldExperience;

    @Column(nullable = false)
    private boolean serviceRule;

}

/**
 * 자격증 여부
 * (Certificate status)
 */
enum CertificateStatus {
    FIRST_RATE_OFF("1급(오프라인)"),
    SECOND_RATE_OFF("2급(오프라인)"),
    FIRST_RATE_ON("1급(온라인)"),
    SECOND_RATE_ON("2급(온라인)"),
    NONE("없음"),
    ETC("기타");  // TODO: 기타 처리

    private String value;

    CertificateStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CertificateStatus{" +
                "value='" + value + '\'' +
                '}';
    }
}
