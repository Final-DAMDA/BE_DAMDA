package com.damda.back.data.request;

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
public class ManagerRegionUpdateRequestDTO {
    
    private Map<String, List<String>> region;

    public Map<String, List<String>> getCity() {
        return null;
    }
    
}
