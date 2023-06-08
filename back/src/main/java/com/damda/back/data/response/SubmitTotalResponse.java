package com.damda.back.data.response;


import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class SubmitTotalResponse {

        private Statistical statistical;

        @Builder.Default
        private List<SubmitResponseDTO>  data = new ArrayList<>();
}
