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
public class Review extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String content;
	private Boolean best;
	private boolean submit;
	@OneToOne
	@JoinColumn(name = "reservation_submit_form_id") // 외래 키
	private ReservationSubmitForm reservationSubmitForm;
	@OneToMany(mappedBy = "review" , cascade = CascadeType.ALL)
	private List<Image> reviewImage;


}