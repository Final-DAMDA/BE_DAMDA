package com.damda.back.domain.manager;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "area_manager")
public class AreaManger {
    
    // TODO: 복합키로 할거면 id 제거
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    private Integer managerId;
    
    @Column
    private Integer areaId;
    
}
