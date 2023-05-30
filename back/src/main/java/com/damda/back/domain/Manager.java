package com.damda.back.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.geom.Area;
import java.time.DayOfWeek;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class Manager {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private Integer userId;
    
    @Enumerated(EnumType.STRING)
    private DayOfWeek activityDate;
    
    @Enumerated(EnumType.STRING)
    private Area activityArea;
    
    private String certificateStatus;
    
    private boolean vehicle;
    
    private boolean fieldExperience;
    
    private boolean serviceRule;
    
}
