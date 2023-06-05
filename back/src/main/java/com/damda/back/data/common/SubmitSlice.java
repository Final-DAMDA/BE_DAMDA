package com.damda.back.data.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubmitSlice {
    private Long questionNumber;
    private QuestionIdentify questionIdentify;
    private String answer;
}
