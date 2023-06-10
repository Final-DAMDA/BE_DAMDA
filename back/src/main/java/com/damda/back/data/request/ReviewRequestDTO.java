package com.damda.back.data.request;

import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.Review;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {

	private String title;
	private String content;
	private List<MultipartFile> before;
	private List<MultipartFile> after;


}
