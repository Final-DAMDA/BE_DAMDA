package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.*;
import com.damda.back.repository.custom.QuestionCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Integer selectMax(){
        QQuestion question = QQuestion.question;

        Integer max = queryFactory.select(question.order.max())
                .from(question)
                .fetchOne();

        if(max != null) return max;
        else return 0;
    }

    @Override
    public List<Question> questionList() {
        QQuestion question = QQuestion.question;
        QCategory category = QCategory.category;

        List<Question> list = queryFactory.selectDistinct(question)
                .from(question)
                .leftJoin(question.categoryList,category)
                .fetchJoin()
                .where(question.status.eq(QuestionStatus.ACTIVATION))
                .orderBy(question.order.asc())
                .fetch();

        return list;
    }

    @Override
    public List<Question> adminQuestionList() {
        QQuestion question = QQuestion.question;
        QCategory category = QCategory.category;

        List<Question> list = queryFactory.selectDistinct(question)
                .from(question)
                .leftJoin(question.categoryList,category)
                .fetchJoin()
                .orderBy(question.order.asc())
                .fetch();

        return list;
    }

    @Override
    public Optional<Question> selectQuestionJoin(Long id) {
        QQuestion question = QQuestion.question;
        QCategory category = QCategory.category;

        Question questionEntity = queryFactory.selectDistinct(question)
                .from(question)
                .leftJoin(question.categoryList, category)
                .fetchJoin()
                .where(question.status.eq(QuestionStatus.ACTIVATION))
                .where(question.questionNumber.eq(id))
                .fetchOne();

        if (questionEntity != null) return Optional.of(questionEntity);
        else return Optional.empty();
    }

    @Override
    public Category findByIdForCategory(Long id) {
        QCategory category = QCategory.category;

        Category categoryEntity = queryFactory.select(category)
                .from(category)
                .where(category.id.eq(id))
                .fetchOne();

        return categoryEntity;
    }

    @Override
    public List<Question> selectWhereACTIVATION() {
        QQuestion question = QQuestion.question;

        return queryFactory.selectDistinct(question)
                .from(question)
                .where(question.status.eq(QuestionStatus.ACTIVATION))
                .fetch();
    }

    @Override
    public Optional<Question> selectQuestionOne(Long id) {
        QQuestion question = QQuestion.question;

        Question questionEntity = queryFactory.selectDistinct(question)
                .from(question)
                .where(question.questionNumber.eq(id))
                .where(question.status.eq(QuestionStatus.ACTIVATION))
                .fetchOne();

        if(questionEntity != null) return Optional.of(questionEntity);
        else return Optional.empty();
    }

    @Override
    public Optional<Question> selectQuestionOneDeactivation(Long id) {
        QQuestion question = QQuestion.question;

        Question questionEntity = queryFactory.selectDistinct(question)
                .from(question)
                .where(question.questionNumber.eq(id))
                .where(question.status.eq(QuestionStatus.DEACTIVATION))
                .fetchOne();

        if(questionEntity != null) return Optional.of(questionEntity);
        else return Optional.empty();
    }

    @Override
    public Optional<Category> selectCategoryOne(Long id) {
        QCategory category = QCategory.category;

        Category categoryEntity = queryFactory
                .select(category)
                .from(category)
                .where(category.id.eq(id))
                .fetchOne();

        if(categoryEntity != null) return Optional.of(categoryEntity);
        return Optional.empty();
    }

    @Override
    public void deleteCategory(Long id) {
        QCategory category = QCategory.category;
        queryFactory
                .delete(category)
                .where(category.id.eq(id))
                .execute();
    }


}
