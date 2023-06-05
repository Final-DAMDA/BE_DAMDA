package com.damda.back.domain.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activity_day")
public class ActivityDay {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;
    
    private boolean isOkMonday;
    
    private boolean isOkTuesday;

    private boolean isOkWednesday;

    private boolean isOkThursday;

    private boolean isOkFriday;

    private boolean isOkSaturday;

    private boolean isOkSunday;
    
}
