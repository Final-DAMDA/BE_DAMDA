package com.damda.back.data.response;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageUserResponseDTO {


        private List<UserResponseDTO> content;

        private boolean first;

        private boolean last;

        private Long total;

}
