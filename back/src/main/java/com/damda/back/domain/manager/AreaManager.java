package com.damda.back.domain.manager;

import com.damda.back.domain.area.Area;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "area_manager")
@Builder
public class AreaManager {
    
    @EmbeddedId
    private AreaManagerKey areaManagerKey;
    
    @Data
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AreaManagerKey implements Serializable {
        
        @ManyToOne
        @JoinColumn(name = "area_id")
        private Area area;
        
        @ManyToOne
        @JoinColumn(name = "manager_id")
        private Manager manager;
        
    }
    
}
