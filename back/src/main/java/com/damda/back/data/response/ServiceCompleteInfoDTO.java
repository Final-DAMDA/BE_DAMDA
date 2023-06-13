package com.damda.back.data.response;

import com.damda.back.data.common.PayMentStatus;
import com.damda.back.data.common.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCompleteInfoDTO {

	private Long reservationId;
	private String createdAt;
	private String name;
	private String phoneNumber;
	private String address;
	private Integer totalPrice;
	private String estimate;
	private Integer manageAmount;
	private String reservationDate;
	private List<String> managerNames;
	private ReservationStatus reservationStatus;
	private PayMentStatus payMentStatus;

}
