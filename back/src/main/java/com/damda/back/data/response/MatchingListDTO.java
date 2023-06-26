package com.damda.back.data.response;

import com.damda.back.domain.Match;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchingListDTO {
	private Long matchId;
	private Long managerId;
	private String matchingResponse;
	private String updateAt;
	private String name;
	private String phone;
	private String address;
	private List<String> activityArea;
	private String certificate;
	private Boolean driving;
	private boolean matching;
	private Integer level;

	public MatchingListDTO(Match match){
		this.matchId = match.getId();
		this.managerId=match.getManagerId();
		this.matchingResponse = match.getMatchStatus().name();
		this.updateAt = match.getUpdatedAt().toString();
		this.name = match.getManagerName();
		this.phone = match.getManager().getPhoneNumber();
		this.address = match.getManager().getAddress();
		this.driving = match.getManager().getVehicle();
		this.certificate = match.getManager().getCertificateStatus().name();
		this.matching = match.isMatching();
		this.level = match.getManager().getLevel();
	}
}
