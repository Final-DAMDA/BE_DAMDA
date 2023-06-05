package com.damda.back.domain;

import com.damda.back.domain.manager.Manager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "match_tb")
public class Match {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private boolean matching;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "form_id")
	private ReservationSubmitForm reservationForm;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id")
	private Manager manager;



}
