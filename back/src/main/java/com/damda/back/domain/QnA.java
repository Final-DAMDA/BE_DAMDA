package com.damda.back.domain;

import com.damda.back.data.common.QnACategory;
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
    
}
