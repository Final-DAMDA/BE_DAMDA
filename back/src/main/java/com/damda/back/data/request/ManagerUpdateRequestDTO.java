package com.damda.back.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerUpdateRequestDTO {
    
    private String managerName;
    
    private String managerPhone;
    
    private String address;
    
    private Integer level;
     
    private String certificateStatus;
    
    private Boolean vehicle;
    
    private String managerStatus;
    
    private String memo;

}
