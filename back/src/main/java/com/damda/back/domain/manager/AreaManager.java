package com.damda.back.domain.manager;

import com.damda.back.domain.area.Area;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "area_manager")
@Builder
public class AreaManager {

    @EmbeddedId
    private AreaManagerKey managerId;

    @Data
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AreaManagerKey implements Serializable {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "area_id")
        private Area area;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "manager_id")
        private Manager manager;

    }

}
