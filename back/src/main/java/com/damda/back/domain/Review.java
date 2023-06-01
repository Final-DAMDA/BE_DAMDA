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
	private String title;
	private String content;

	@OneToMany(mappedBy = "review" , cascade = CascadeType.ALL)
	private List<ReviewImage> reviewImage;



}