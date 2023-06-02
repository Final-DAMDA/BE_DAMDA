package com.damda.back.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class Match {
	@Id
	@GeneratedValue
	private Long id;
	@OneToOne
	private ReservationSubmitForm reservationForm;
}
