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
	@GeneratedValue
	private Long id;

	private String content;

	private Boolean best;
	@OneToOne
	@JoinColumn(name = "service_complete_id") // 외래 키
	private ServiceComplete serviceComplete;


}