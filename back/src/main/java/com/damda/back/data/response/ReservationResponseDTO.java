package com.damda.back.data.response;


import com.damda.back.data.common.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDTO {

    private Long questionNumber;
    private Integer questionOrder;

    private String questionTitle;

    private QuestionType questionType;

    private Map<String,Integer> category = new HashMap<>();



}
