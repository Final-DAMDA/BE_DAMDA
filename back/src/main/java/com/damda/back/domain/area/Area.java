package com.damda.back.domain.area;


import com.damda.back.domain.manager.AreaManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// TODO:
// 1. 그냥 area db 에 저장해놓기
// 2. enum으로 할지? 고민중
// 3. daum

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "area_tb")
@ToString
public class Area {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
//    @Enumerated(value = EnumType.STRING)
//    private CityEnum city;
//
//    @Enumerated(value = EnumType.STRING)
//    private DistrictEnum district;

    private String city;
    
    private String district;
    
    private Integer managerCount;

    @OneToMany(mappedBy = "areaManagerKey.area")
    @BatchSize(size = 10)
    @Builder.Default
    private List<AreaManager> areaManagerList = new ArrayList<>();
    
}
