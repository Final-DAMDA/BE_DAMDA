package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.QCategory;
import com.damda.back.domain.QQuestion;
import com.damda.back.domain.Question;
import com.damda.back.domain.QuestionStatus;
import com.damda.back.repository.custom.QuestionCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                .innerJoin(question.categoryList,category)
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
                .innerJoin(question.categoryList,category)
                .fetchJoin()
                .orderBy(question.order.asc())
                .fetch();

        return list;
    }
}
