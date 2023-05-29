package com.damda.back.repository.custom;

import com.damda.back.domain.Question;

import java.util.List;

public interface QuestionCustomRepository {

    public Integer selectMax();

    public List<Question> questionList();

    public List<Question> adminQuestionList();

}
