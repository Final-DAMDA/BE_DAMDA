package com.damda.back.data.request;


import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.QuestionType;
import com.damda.back.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationFormRequestDTO {

        private QuestionIdentify questionIdentify;

        private QuestionType questionType;

        private String questionTitle;

        private boolean required;

        @Builder.Default
        private Map<String,Integer> category = new HashMap<>();


}
