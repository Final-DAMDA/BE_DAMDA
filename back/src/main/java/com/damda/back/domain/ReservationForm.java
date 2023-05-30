package com.damda.back.domain;


import com.damda.back.data.common.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.security.DenyAll;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationForm extends BaseEntity{

    @Id
    private Long id;

    private ReservationStatus status;



}
