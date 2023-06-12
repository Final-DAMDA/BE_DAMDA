package com.damda.back.domain.manager;

import com.damda.back.domain.Member;
import com.damda.back.domain.area.DistrictEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_day_id")
    private ActivityDay activityDay;

    @OneToMany(mappedBy = "areaManagerKey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AreaManager> areaManagers = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private CertificateStatusEnum certificateStatus;
    
    private String certificateStatusEtc;
    
    private Integer level; // 매니저 레벨

    private Boolean vehicle; //자차 여부

    private String fieldExperience; //현장경험 유무
    
    private Boolean mainJobStatus; //본업 유무
    
    private String mainJobStatusEtc; //본업 기재

    private String memo;
    
    private String prevManagerStatus;
    
    private String currManagerStatus;

    public void addActivityDay(ActivityDay activityDay){
        this.activityDay=activityDay;
    }
}
