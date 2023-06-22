package com.damda.back.data.request;

import com.damda.back.domain.Member;
import com.damda.back.domain.manager.ActivityDay;
import com.damda.back.domain.manager.CertificateStatusEnum;
import com.damda.back.domain.manager.Manager;
import com.damda.back.domain.manager.ManagerStatusEnum;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerApplicationDTO {

    private String managerName;
    private String managerPhoneNumber;
    private List<Boolean> activityDay;//(월~일 boolean만 보냄)
    private Map<String, List<String>> region;
    private String certificateStatus;
    private String certificateStatusEtc;
    private String fieldExperience;
    private Boolean mainJobStatus;
    private String mainJobStatusEtc;
    private Boolean vehicle;

    public Manager toManagerEntity(Member member) {
        return Manager.builder()
                .managerName(managerName)
                .managerPhoneNumber(managerPhoneNumber)
                .level(1)
                .certificateStatusEtc(certificateStatusEtc)
                .certificateStatus(CertificateStatusEnum.valueOf(certificateStatus))
                .vehicle(vehicle)
                .fieldExperience(fieldExperience)
                .mainJobStatus(mainJobStatus)
                .mainJobStatusEtc(mainJobStatusEtc)
                .member(member)
                .currManagerStatus(ManagerStatusEnum.WAITING)
                .build();
    }


    public ActivityDay toDayEntity() {
        return ActivityDay.builder()
                .isOkMonday(activityDay.get(0))
                .isOkTuesday(activityDay.get(1))
                .isOkWednesday(activityDay.get(2))
                .isOkThursday(activityDay.get(3))
                .isOkFriday(activityDay.get(4))
                .isOkSaturday(activityDay.get(5))
                .isOkSunday(activityDay.get(6))
                .build();
    }
    
}
