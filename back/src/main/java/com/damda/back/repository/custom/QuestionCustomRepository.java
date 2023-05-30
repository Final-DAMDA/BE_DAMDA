package com.damda.back.repository.custom;

import com.damda.back.domain.Category;
import com.damda.back.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionCustomRepository {

    public Integer selectMax();

    public List<Question> questionList();

    public List<Question> adminQuestionList();

    public Optional<Question> selectQuestionJoin(Long id);

    public Category findByIdForCategory(Long id);

    public List<Question> selectWhereACTIVATION();

    public Optional<Question> selectQuestionOne(Long id);

    public Optional<Question> selectQuestionOneDeactivation(Long id);

    public Optional<Category> selectCategoryOne(Long id);

    public void deleteCategory(Long id);

}
