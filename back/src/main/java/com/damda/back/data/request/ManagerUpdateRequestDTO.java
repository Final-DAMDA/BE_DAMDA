package com.damda.back.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerUpdateRequestDTO {
    
    private String managerName;
    private String managerPhoneNumber;
    private List<Boolean> activityDay;
    private String certificateStatus;
    private String certificateStatusEtc;
    private Integer level;
    private Boolean vehicle;
    private String fieldExperience;
    private Boolean mainJobStatus;
    private String mainJobStatusEtc;
    private String memo;
    private String currManagerStatus;

}
