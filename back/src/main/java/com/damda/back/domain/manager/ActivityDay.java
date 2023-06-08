package com.damda.back.domain.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activity_day")
@Builder
public class ActivityDay {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(mappedBy = "activityDay")
    private Manager manager;
    
    private boolean isOkMonday;
    
    private boolean isOkTuesday;

    private boolean isOkWednesday;

    private boolean isOkThursday;

    private boolean isOkFriday;

    private boolean isOkSaturday;

    private boolean isOkSunday;

    public void addManager(Manager manager){
        this.manager = manager;
    }
    
}
