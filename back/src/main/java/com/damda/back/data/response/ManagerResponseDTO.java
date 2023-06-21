package com.damda.back.data.response;

import com.damda.back.domain.Member;
import com.damda.back.domain.manager.ActivityDay;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.CertificateStatusEnum;
import com.damda.back.domain.manager.Manager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
