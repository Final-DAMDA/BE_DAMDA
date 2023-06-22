package com.damda.back.domain;

import com.damda.back.data.common.MatchResponseStatus;
import com.damda.back.domain.manager.Manager;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "match_tb")
public class Match extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private boolean matching;

	private String managerName;


	@Enumerated(EnumType.STRING)
	private MatchResponseStatus matchStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "form_id")
	private ReservationSubmitForm reservationForm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id",insertable = false,updatable = false)
	private Manager manager;

	@Column(name = "manager_id")
	private Long managerId;

	public void matchStatusYes(){
		if(this.matchStatus!=MatchResponseStatus.WAITING){
			throw new CommonException(ErrorCode.NOT_FOUND_MATCH);
		}
		this.matchStatus=MatchResponseStatus.YES;
	}
	public void matchStatusNO(){
		if(this.matchStatus!=MatchResponseStatus.WAITING){
			throw new CommonException(ErrorCode.NOT_FOUND_MATCH);
		}
		this.matchStatus=MatchResponseStatus.NO;
	}
	public void matchingOrder(){
		this.matching = true;
	}
}
