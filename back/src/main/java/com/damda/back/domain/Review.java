package com.damda.back.domain;

import com.damda.back.data.request.ReviewRequestDTO;
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



	@Column(columnDefinition = "TEXT")
	private String content;
	private Boolean best;
	private boolean status;
	@OneToOne
	@JoinColumn(name = "reservation_submit_form_id") // 외래 키
	private ReservationSubmitForm reservationSubmitForm;
	@OneToMany(mappedBy = "review" , cascade = CascadeType.ALL)
	private List<Image> reviewImage;

	public void reviewUpload(ReviewRequestDTO reviewRequestDTO){
		this.content=reviewRequestDTO.getContent();
		this.title=reviewRequestDTO.getTitle();
		this.status=true;
		this.best=false;
	}
	public void reviewDelete(){
		this.content=null;
		this.title=null;
		this.status=false;
		this.best=false;
	}

	public void setBestReview(boolean best){
		this.best=best;
	}

}