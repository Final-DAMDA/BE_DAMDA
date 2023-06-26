package com.damda.back.domain.manager;

import com.damda.back.data.request.ManagerStatusUpdateRequestDTO;
import com.damda.back.data.request.ManagerUpdateRequestDTO;
import com.damda.back.domain.BaseEntity;
import com.damda.back.domain.Match;
import com.damda.back.domain.Member;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import lombok.*;
import org.hibernate.annotations.BatchSize;

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
    private String phoneNumber;
    private String address;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_day_id")
    private ActivityDay activityDay;


    @BatchSize(size = 100)
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
        this.phoneNumber = dto.getManagerPhoneNumber();
        this.certificateStatus = CertificateStatusEnum.valueOf(dto.getCertificateStatus());
        this.certificateStatusEtc = dto.getCertificateStatusEtc();
        this.level = dto.getLevel();
        this.vehicle = dto.getVehicle();
        this.fieldExperience = dto.getFieldExperience();
        this.memo = dto.getMemo();

    }


    public void removeAll() {
        this.areaManagers.clear();
    }

    public void updateManagerStatus(ManagerStatusUpdateRequestDTO dto) {
        
        if (this.currManagerStatus == dto.getCurrManagerStatus()) {
            throw new CommonException(ErrorCode.MANAGER_STATUS_BAD_REQUEST);
        }

        this.prevManagerStatus = this.currManagerStatus;
        this.currManagerStatus = dto.getCurrManagerStatus();
    }

}
