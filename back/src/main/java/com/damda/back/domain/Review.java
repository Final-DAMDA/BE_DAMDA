package com.damda.back.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Review {
	@Id
	@GeneratedValue
	private Long id;

	private String content;

	private boolean best;
	@OneToOne
	private ReservationForm reservationForm;






}