package com.damda.back.data.request;

import com.damda.back.domain.Member;
import com.damda.back.domain.manager.ActivityDay;
import com.damda.back.domain.manager.Manager;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagerApplicationDTO {



    private List<String> activityDay;

    private List<String> activityCity;

    private List<String> activityDistrict;

    private String certificateStatus;

    private String certificateStatusEtc;

    private Boolean vehicle;

    private String fieldExperience;

    private Boolean mainJobStatus;

    private String mainJobStatusEtc;

    public Manager toManagerEntity(Member manager) {
        return Manager.builder()
                .certificateStatusEtc(certificateStatusEtc)
                .vehicle(vehicle)
                .fieldExperience(fieldExperience)
                .mainJobStatus(mainJobStatus)
                .member(manager)
                .build();
    }

//    public ActivityDay toActivityDayEntity(Manager manager){
//        return ActivityDay.builder().isOkMonday(monday).build();
//    }


}
