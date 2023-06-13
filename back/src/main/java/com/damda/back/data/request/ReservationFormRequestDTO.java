package com.damda.back.data.request;


import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.QuestionType;
import com.damda.back.domain.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
public class ReservationFormRequestDTO {


                private QuestionIdentify questionIdentify;

                private QuestionType questionType;

                private String questionTitle;

                private boolean required;

                private Integer page;

                private Integer order;

                private String placeHolder;

                @Builder.Default
                private Map<String,Integer> category = new HashMap<>();


}
