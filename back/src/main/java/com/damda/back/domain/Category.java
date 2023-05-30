package com.damda.back.domain;


import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "category_tb")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // FK를 PK로 선택사항

    private String questionCategory;

    private Integer categoryPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_number")
    private Question question; // 인덱스 고려사항 속도 빠름 넣는게 좋음

    public void questionConfig(Question question){
        this.question = question;
    }


    public void changeCategory(String questionCategory){this.questionCategory = questionCategory;}

    public void changeCategoryPrice(Integer price){this.categoryPrice = price;}
}
