package com.damda.back.data.request;


import com.damda.back.data.common.CategoryMapDTO;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.QuestionType;
import com.damda.back.domain.QuestionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormModifyDTO {


    @NotNull
    private QuestionIdentify questionIdentify;

    @NotNull
    private QuestionType questionType;

    @NotEmpty
    private String questionTitle;

    @NotNull
    private Integer order;

    @NotNull
    private boolean required;

    @NotNull
    private Integer page;

    @NotEmpty
    private String placeHolder;

    @Builder.Default
    private List<CategoryMapDTO> categoryList = new ArrayList<>();

}
