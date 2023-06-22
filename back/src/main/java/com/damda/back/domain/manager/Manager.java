package com.damda.back.domain.manager;

import com.damda.back.data.request.ManagerUpdateRequestDTO;
import com.damda.back.domain.BaseEntity;
import com.damda.back.domain.Match;
import com.damda.back.domain.Member;
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
public class Manager extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    
    private String managerName;
    private String managerPhoneNumber;
    private String address;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_day_id")
    private ActivityDay activityDay;

    @OneToMany(mappedBy = "areaManagerKey.manager", cascade = CascadeType.ALL, orphanRemoval = true)
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
    private ManagerStatusEnum prevManagerStatus;
    private ManagerStatusEnum currManagerStatus;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches = new ArrayList<>();

    public void addActivityDay(ActivityDay activityDay) {
        this.activityDay = activityDay;
    }

    public void updateManager(ManagerUpdateRequestDTO dto) {
        this.managerName = dto.getManagerName();
        this.managerPhoneNumber = dto.getManagerPhoneNumber();
//        this.activityDay = dto.getActivityDay();
        this.certificateStatus = CertificateStatusEnum.valueOf(dto.getCertificateStatus());
        this.certificateStatusEtc = dto.getCertificateStatusEtc();
        this.level = dto.getLevel();
        this.vehicle = dto.getVehicle();
        this.fieldExperience = dto.getFieldExperience();
        this.memo = dto.getMemo();
        this.prevManagerStatus = this.currManagerStatus;
        this.currManagerStatus = ManagerStatusEnum.valueOf(dto.getCurrManagerStatus());

    }


    public void removeAll() {
        this.areaManagers.clear();
    }

}
