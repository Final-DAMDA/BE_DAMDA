package com.damda.back.domain;

import com.damda.back.data.common.QnACategory;
import com.damda.back.data.request.QnARequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
public class QnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;

    @Enumerated(EnumType.STRING)
    private QnACategory qnACategory;
    
    private String contents;
    
    public void updateQnA(QnARequestDTO dto) {
        this.title = dto.getTitle();
        this.qnACategory = QnACategory.valueOf(dto.getQnaCategory());
        this.contents = dto.getContents();
    }
    
}
