package com.damda.back.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnAResponseDTO {
    
    private Long qnaId;
    
    private String title;
    
    private String qnaCategory;
    
    private String contents;
    
}
