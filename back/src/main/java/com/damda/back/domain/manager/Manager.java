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
@Table(name = "manager_tb")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member userId;

    @OneToOne
    @JoinColumn(name = "activity_day_id")
    private ActivityDay activityDay;

    @Enumerated(EnumType.STRING)
    private DistrictEnum activityArea;
    
    @OneToOne
    @JoinColumn(name = "certificate_status_id")
    private CertificateStatus certificateStatus;
    
    private Integer level;

    private boolean vehicle;

    private boolean fieldExperience;

    private boolean serviceRule;

    private String memo;
}
