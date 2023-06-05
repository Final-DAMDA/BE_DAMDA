package com.damda.back.data.request;

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

    private List<String> activityArea;

    private String certificateStatus;

    private String certificateStatusEtc;

    private Boolean vehicle;

    private String fieldExperience;

    private Boolean mainJobStatus;

    private String mainJobStatusEtc;

    public Manager toEntity() {
        return Manager.builder()
                .certificateStatusEtc(certificateStatusEtc)
                .build();
    }

}
