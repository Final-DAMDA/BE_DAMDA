package com.damda.back.data.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RearrangeRequestDTO {

    private Long questionNumber;
    private Integer questionOrder;

    private Integer page;


}
