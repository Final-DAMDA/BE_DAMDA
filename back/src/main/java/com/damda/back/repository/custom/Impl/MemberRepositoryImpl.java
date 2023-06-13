package com.damda.back.repository.custom.Impl;

import com.damda.back.data.common.MemberStatus;
import com.damda.back.domain.Member;
import com.damda.back.domain.QDiscountCode;
import com.damda.back.domain.QMember;
import com.damda.back.repository.MemberRepository;
import com.damda.back.repository.custom.MemberCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<Member> findByPhoneNumber(String phoneNumber){
        QMember member = QMember.member;

        Member memberEntity = queryFactory.select(member)
                .from(member)
                .where(member.phoneNumber.eq(phoneNumber))
                .fetchOne();

        if(memberEntity != null) return Optional.of(memberEntity);
        else return Optional.empty();
    }

    @Override
    public Optional<Member> findByIdWhereActive(Integer id) {
        QMember member = QMember.member;

        Member memberEntity = queryFactory.select(member)
                .from(member)
                .where(member.id.eq(id))
                .where(member.status.eq(MemberStatus.ACTIVATION))
                .fetchOne();

        if(memberEntity != null) return Optional.of(memberEntity);
        else return Optional.empty();
    }


    public boolean existCode(String code){
        QDiscountCode discountCode = QDiscountCode.discountCode;
        BooleanExpression booleanExpression = discountCode.code.eq(code);

        boolean data =queryFactory.selectOne()
                .from(discountCode)
                .where(booleanExpression)
                .fetchOne() != null;

        return data;
    }




}
