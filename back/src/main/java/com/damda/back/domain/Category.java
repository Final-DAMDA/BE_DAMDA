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
    private Long id;

    private String questionCategory;

    private Integer categoryPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_number")
    private Question question;

    public void questionConfig(Question question){
        this.question = question;
    }

}
