package com.damda.back.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServiceComplete {
	@Id
	@GeneratedValue
	private Long id;
	@OneToOne
	private ReservationForm reservation;
	private Boolean submit;

}
