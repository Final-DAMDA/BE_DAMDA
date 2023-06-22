package com.damda.back.data.response;

import com.damda.back.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerResponseDTO {

    private Long id;
    private Member member;
    private String managerName;
    private String managerPhoneNumber;
    private List<Boolean> activityDay;
    private Map<String, List<String>> region;
    private Integer level;
    private String certificateStatus;
    private String certificateStatusEtc;
    private String fieldExperience;
    private Boolean mainJobStatus;
    private String mainJobStatusEtc;
    private Boolean vehicle;
    private String memo;
    private String prevManagerStatus;
    private String currManagerStatus;

}
