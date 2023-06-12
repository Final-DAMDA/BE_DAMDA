package com.damda.back.data.request;

import com.damda.back.domain.Member;
import com.damda.back.domain.area.Area;
import com.damda.back.domain.manager.ActivityDay;
import com.damda.back.domain.manager.CertificateStatusEnum;
import com.damda.back.domain.manager.Manager;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerApplicationDTO {

    private List<Boolean> activityDay;//(월~일 boolean만 보냄)
    private List<String> activityCity;
    private List<String> activityDistrict;
    private String certificateStatus;
    private String certificateStatusEtc;

    private Boolean vehicle;

    private String fieldExperience;

    private Boolean mainJobStatus;

    private String mainJobStatusEtc;

    public Manager toManagerEntity(Member manager) { //TODO: 매니저 상태값 체크
        return Manager.builder()
                .certificateStatusEtc(certificateStatusEtc)
                .certificateStatus(CertificateStatusEnum.valueOf(certificateStatus))
                .vehicle(vehicle)
                .fieldExperience(fieldExperience)
                .mainJobStatus(mainJobStatus)
                .mainJobStatusEtc(mainJobStatusEtc)
                .member(manager)
                .build();
    }



    public ActivityDay toDayEntity(){
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
