package com.damda.back.domain.area;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "area_tb")
public class Area {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Enumerated(value = EnumType.STRING)
    private CityEnum city;

    @Enumerated(value = EnumType.STRING)
    private DistrictEnum district;
    
}
