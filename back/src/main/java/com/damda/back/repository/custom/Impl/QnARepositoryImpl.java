package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.QQnA;
import com.damda.back.domain.QnA;
import com.damda.back.repository.custom.QnACustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class QnARepositoryImpl implements QnACustomRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<QnA> findQnA() {
        QQnA qQnA = QQnA.qnA;
        
        return queryFactory.selectFrom(qQnA)
                .fetch();        
    }
}
