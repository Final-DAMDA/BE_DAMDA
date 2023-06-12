package com.damda.back.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServiceComplete extends BaseEntity{
	@Id
	@GeneratedValue
	private Long id;
	@OneToOne
	@JoinColumn(name = "reservation_submit_form_id") // 외래 키
	private ReservationSubmitForm reservation;
	private Boolean submit;

}
