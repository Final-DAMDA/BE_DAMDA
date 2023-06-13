package com.damda.back.data.request;


import com.damda.back.data.common.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormStatusModifyRequestDTO {

        private Long id;

        private ReservationStatus status;

}
