package com.damda.back.domain.area;

import lombok.*;

import javax.persistence.*;

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
    
}
