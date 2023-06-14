package com.damda.back.data.request;


import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.SubmitSlice;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitRequestDTO {

    private List<SubmitSlice> submit;
    private String addressFront;
    private Integer totalPrice;

}

