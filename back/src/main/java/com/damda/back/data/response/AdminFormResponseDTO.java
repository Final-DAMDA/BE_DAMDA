package com.damda.back.data.response;


import com.damda.back.data.common.CategoryMapDTO;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminFormResponseDTO {
    private Long questionNumber;
    private Integer questionOrder;

    private String questionTitle;

    private QuestionType questionType;

    private QuestionIdentify questionIdentify;


    private boolean isDeleted;

    private boolean required;

    @Builder.Default
    private List<CategoryMapDTO> categoryList = new ArrayList<>();

}
