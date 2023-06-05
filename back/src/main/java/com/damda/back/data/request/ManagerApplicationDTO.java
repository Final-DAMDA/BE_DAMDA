package com.damda.back.data.request;

import com.damda.back.domain.area.DistrictEnum;
import com.damda.back.domain.manager.ActivityDay;
import com.damda.back.domain.manager.CertificateStatusEnum;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagerApplicationDTO {

    private List<String> activityDay;
    
    private List<String> activityArea;

    private String certificateStatus;

    private String certificateStatusEtc;

    private Boolean vehicle;

    private String fieldExperience;

    private Boolean mainJobStatus;

    private String mainJobStatusEtc;
    
}
