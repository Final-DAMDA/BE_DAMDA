package com.damda.back.data.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMapDTO {
    private Long id;
    private String category;

    private Integer price;
}
