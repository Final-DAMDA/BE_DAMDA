package com.damda.back.domain.manager;

import com.damda.back.domain.Member;
import com.damda.back.domain.area.DistrictEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
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
    private Member member;

    @OneToOne
    @JoinColumn(name = "activity_day_id")
    private ActivityDay activityDay;

    @Enumerated(EnumType.STRING)
    private DistrictEnum activityArea;
    
    @Enumerated(EnumType.STRING)
    private CertificateStatusEnum certificateStatus;
    
    private String certificateStatusEtc;
    
    private Integer level;

    private Boolean vehicle;

    private String fieldExperience;
    
    private Boolean mainJobStatus;
    
    private String mainJobStatusEtc;

    private String memo;

    public void addActivityDay(ActivityDay activityDay){
        this.activityDay=activityDay;
    }
}
