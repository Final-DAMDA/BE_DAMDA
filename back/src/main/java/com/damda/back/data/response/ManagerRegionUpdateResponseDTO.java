package com.damda.back.data.response;

import com.damda.back.data.request.ManagerRegionUpdateRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerRegionUpdateResponseDTO {

     private Map<String, List<String>> region;

}
