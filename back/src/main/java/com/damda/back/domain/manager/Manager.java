package com.damda.back.domain.manager;

import com.damda.back.domain.Member;
import com.damda.back.domain.area.DistrictEnum;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "manager_tb")
@ToString
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

    @OneToMany(mappedBy = "managerId.manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AreaManager> areaManagers = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private CertificateStatusEnum certificateStatus;
    
    private String certificateStatusEtc;
    
    private Integer level;

    private Boolean vehicle;

    private String fieldExperience;
    
    private Boolean mainJobStatus;
    
    private String mainJobStatusEtc;

    private String memo;
    
    private String prevManagerStatus;
    
    private String currManagerStatus;

    private String phoneNumber;

    private String managerName;

    public void addActivityDay(ActivityDay activityDay){
        this.activityDay=activityDay;
    }
}
