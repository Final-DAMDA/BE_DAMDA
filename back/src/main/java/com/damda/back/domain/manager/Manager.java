package com.damda.back.domain.manager;

import com.damda.back.domain.BaseEntity;
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
public class Manager extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;
    
    private String phoneNumber;
    
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

    public void addActivityDay(ActivityDay activityDay){
        this.activityDay=activityDay;
    }

}
