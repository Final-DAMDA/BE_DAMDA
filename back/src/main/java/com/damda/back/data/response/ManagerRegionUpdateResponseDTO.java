package com.damda.back.data.response;

import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.Manager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerRegionUpdateResponseDTO {

     private List<String> activityCity;

     private List<String> activityDistrict;

//     public ManagerRegionUpdateResponseDTO(/* List<AreaManager> areaManagerList */) {
//         this.activityCity = areaManagerList;
//         this.activityDistrict = areaManagerList;
//     }

}
