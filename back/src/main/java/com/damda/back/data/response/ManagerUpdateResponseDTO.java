package com.damda.back.data.response;

import com.damda.back.domain.manager.Manager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerUpdateResponseDTO {

    private String managerName;

    private String managerPhone;

    private String address;

    private Integer level;

    private String certificateStatus;

    private Boolean vehicle;

    private String managerStatus;

    private String memo;

    public ManagerUpdateResponseDTO(Manager manager) {
        this.managerName = manager.getManagerName();
        this.managerPhone = manager.getPhoneNumber();
        this.level = manager.getLevel();
        this.certificateStatus = manager.getCertificateStatus().toString();
        this.vehicle = manager.getVehicle();
        this.managerStatus = manager.getCurrManagerStatus().toString();
        this.memo = manager.getMemo();
    }

}
