package com.damda.back.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCompleteResponseDTO {

	private List<String> managerNames;
	private String serviceDate;
	private String serviceAddress;
	private Integer managerAccount;
	private String serviceTime;
	private Long reservationId;

}
