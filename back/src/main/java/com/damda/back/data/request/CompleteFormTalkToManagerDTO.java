package com.damda.back.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteFormTalkToManagerDTO {
	private List<String> phoneNumber;
	private String link;
	private LocalDateTime sendTime;
}
