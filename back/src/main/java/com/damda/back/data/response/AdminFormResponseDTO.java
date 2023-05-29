package com.damda.back.data.response;


import com.damda.back.data.common.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminFormResponseDTO {
    private Long questionNumber;
    private Integer questionOrder;

    private String questionTitle;

    private QuestionType questionType;

    private boolean isDeleted;

    private Map<String,Integer> category = new HashMap<>();

}
