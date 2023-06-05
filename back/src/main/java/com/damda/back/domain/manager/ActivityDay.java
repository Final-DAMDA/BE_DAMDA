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
    private Integer id;
    
    @Column
    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;
    
    @Column
    private boolean isOkMonday;
    
    @Column
    private boolean isOkTuesday;

    @Column
    private boolean isOkWednesday;

    @Column
    private boolean isOkThursday;

    @Column
    private boolean isOkFriday;

    @Column
    private boolean isOkSaturday;

    @Column
    private boolean isOkSunday;
    
}
