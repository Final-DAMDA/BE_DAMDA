package com.damda.back.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class Image {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_complete_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private ServiceComplete serviceComplete;
	private String imgType; //enum으로 바꿀예정
	private String imgName;
	private String imgUrl;
}
