package com.damda.back.data.response;


import com.damda.back.data.common.CategoryMapDTO;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.QuestionType;
import com.damda.back.domain.area.DistrictEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDTO {

    private Long questionNumber;
    private Integer questionOrder;

    private String questionTitle;

    private QuestionType questionType;

    private QuestionIdentify questionIdentify;

    private boolean required;

    @Builder.Default
    private List<DistrictEnum> locations=  new ArrayList<>();



    @Builder.Default
    private List<CategoryMapDTO> categoryList = new ArrayList<>();



}
