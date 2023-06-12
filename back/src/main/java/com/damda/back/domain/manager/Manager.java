package com.damda.back.domain.manager;

import com.damda.back.domain.Member;
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

    private String managerName;

    private String managerPhoneNumber;
    
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

    public void addActivityDay(ActivityDay activityDay){
        this.activityDay=activityDay;
    }
}
