package com.damda.back.repository.custom.Impl;

import com.damda.back.data.common.MemberRole;
import com.damda.back.data.common.MemberStatus;
import com.damda.back.data.common.ReservationStatus;
import com.damda.back.data.response.UserResponseDTO;
import com.damda.back.domain.*;
import com.damda.back.repository.MemberRepository;
import com.damda.back.repository.custom.MemberCustomRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
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
        QDiscountCode discountCode = QDiscountCode.discountCode;

        Member memberEntity = queryFactory.select(member)
                .from(member)
                .leftJoin(member.discountCode,discountCode).fetchJoin()
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

    @Override
    public Optional<Member> findByAdmin(String username) {

        QMember member = QMember.member;
        QDiscountCode discountCode = QDiscountCode.discountCode;
        Member memberEntity  =  queryFactory
                .selectDistinct(member)
                .from(member)
                .leftJoin(member.discountCode,discountCode).fetchJoin()
                .where(member.status.eq(MemberStatus.ACTIVATION))
                .where(member.role.eq(MemberRole.ADMIN))
                .where(member.username.eq(username))
                .fetchOne();

        if(memberEntity != null) return Optional.of(memberEntity);
        else return Optional.empty();
    }

    public Page<Member> findByMemberListWithCode(String search, Pageable pageable){
        QMember member = QMember.member;
        QDiscountCode discountCode = QDiscountCode.discountCode;
        QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;

        BooleanExpression searchExpression = null;
        if(search != null) searchExpression = member.username.contains(search);;


        List<UserResponseDTO> dtos = new ArrayList<>();

        List<Member> list = queryFactory
                .selectDistinct(member)
                .from(member)
                .leftJoin(member.discountCode,discountCode).fetchJoin()
                .where(member.status.eq(MemberStatus.ACTIVATION))
                .where(member.role.eq(MemberRole.USER))
                .where(searchExpression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        var count = queryFactory.selectDistinct(member.count())
                .from(member);

        return PageableExecutionUtils.getPage(list,pageable,count::fetchOne);
    }

}
