package com.damda.back.data.response;


import com.damda.back.domain.ReservationSubmitForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormResultDTO {

    private Statistical statistical;

    @Builder.Default
    private List<FormSliceDTO> content = new ArrayList<>();

    private boolean first;

    private boolean last;

    private Long total;

}
