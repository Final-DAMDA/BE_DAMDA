package com.damda.back.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInfo {
	private String serviceDate;
	private String serviceDuration;
	private String servicePerPerson;
	private String location;
}
