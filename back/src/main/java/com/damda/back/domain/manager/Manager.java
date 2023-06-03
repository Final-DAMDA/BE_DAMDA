package com.damda.back.domain.manager;

import com.damda.back.domain.Member;
import com.damda.back.domain.area.DistrictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "member_id")
    @Column(nullable = false)
    private Member userId;

    // @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    // private DayOfWeek activityDay;  // 테이블 따로 빼서 one to many 양방향

    @OneToOne
    @JoinColumn(name = "activity_day_id")
    @Column(nullable = false)
    private ActivityDay activityDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DistrictEnum activityArea;
    
    @OneToOne
    @JoinColumn(name = "certificate_status_id")
    @Column(nullable = false)
    private CertificateStatus certificateStatus;
    
    private Integer level;

    @Column(nullable = false)
    private boolean vehicle;

    @Column(nullable = false)
    private boolean fieldExperience;

    @Column(nullable = false)
    private boolean serviceRule;

    private String memo;
}

/**
 * 자격증 여부
 * (Certificate status)
 */
enum CertificateStatusEnum {
    FIRST_RATE_OFF("1급(오프라인)"),
    SECOND_RATE_OFF("2급(오프라인)"),
    FIRST_RATE_ON("1급(온라인)"),
    SECOND_RATE_ON("2급(온라인)"),
    NONE("없음");

    private String value;

    CertificateStatusEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CertificateStatus{" +
                "value='" + value + '\'' +
                '}';
    }
}
