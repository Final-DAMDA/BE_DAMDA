package com.damda.back.data.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoRequestDTO {

    private Integer memberId;

    private String memo;
}
