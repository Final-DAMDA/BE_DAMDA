package com.damda.back.domain;


import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.QuestionType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "question_tb")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionNumber;


    @Enumerated(EnumType.STRING)
    private QuestionIdentify questionIdentify;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    private String questionTitle;

    @Column(name = "order_number")
    private Integer order;

    @Enumerated(EnumType.STRING)
    private QuestionStatus status;


    private boolean required;


    @Builder.Default
    @OneToMany(mappedBy = "question",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Category> categoryList = new ArrayList<>();


    public void addCategory(Category category){
        category.questionConfig(this);
        this.getCategoryList().add(category);
    }


    public void changeIdentify(QuestionIdentify identify){this.questionIdentify = identify;}
    public void changeQuestionType(QuestionType questionType){this.questionType = questionType;}
    public void changeQuestionTitle(String questionTitle){this.questionTitle = questionTitle;}
    public void changeOrder(Integer order){this.order = order;}

    public void changeRequired(boolean required){this.required = required;}
    public void changeStatus(QuestionStatus status){this.status = status;}

    //TODO: 테스트 지우기
    public void cleanCategory(){
        this.categoryList.clear();
    }
}
