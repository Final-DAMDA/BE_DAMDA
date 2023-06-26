package com.damda.back.data.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageManagerResponseDTO {


    private List<ManagerResponseDTO> content;

    private boolean first;

    private boolean last;

    private Long total;


}
