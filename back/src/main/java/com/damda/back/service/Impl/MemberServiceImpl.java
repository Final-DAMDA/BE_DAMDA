package com.damda.back.service.Impl;

import com.amazonaws.services.ec2.model.Reservation;
import com.damda.back.data.common.MemberRole;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.ReservationStatus;
import com.damda.back.data.request.MemoRequestDTO;
import com.damda.back.data.response.*;
import com.damda.back.domain.Member;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.MemberRepository;
import com.damda.back.repository.ReservationFormRepository;
import com.damda.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final ReservationFormRepository formRepository;


    @Override
    public MemberResponseDTO detail(Integer id) {
        Optional<Member> optional = memberRepository.findByIdWhereActive(id);

        if(optional.isPresent()){
            Member member = optional.get();
            MemberResponseDTO dto = MemberResponseDTO.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .address(member.getAddress())
                    .gender(member.getGender())
                    .phoneNumber(member.getPhoneNumber())
                    .profileImage(member.getProfileImage())
                    .role(member.getRole())
                    .build();

            return dto;
        }else{
            throw new CommonException(ErrorCode.NOT_FOUND_MEMBER);
        }
    }

    @Override
    public boolean existCode(String code) {
        return memberRepository.existCode(code);
    }



    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public PageUserResponseDTO listMember(String search,Integer page){
        Page<Member> memberPage = memberRepository.findByMemberListWithCode(search, PageRequest.of(page,10));
        List<UserResponseDTO> dtos = new ArrayList<>();

        memberPage.getContent().forEach(memberPE -> {
            List<ReservationSubmitForm> submitForms = memberPE.getReservationSubmitFormList();
            ReservationStatus status = submitForms.isEmpty() ? ReservationStatus.NONE : submitForms.stream().findFirst().get().getStatus();

            UserResponseDTO dto = UserResponseDTO.builder()
                    .id(memberPE.getId())
                    .name(memberPE.getUsername())
                    .address(memberPE.getAddress())
                    .phoneNumber(memberPE.getPhoneNumber())
                    .address(memberPE.getAddress())
                    .reservationStatus(status)
                    .createdAt(memberPE.getCreatedAt().toString())
                    .code(memberPE.getDiscountCode() != null ? memberPE.getDiscountCode().getCode() : null)
                    .memo(memberPE.getMemo())
                    .build();

            dto.safeFromNull();

            dtos.add(dto);
        });

        PageUserResponseDTO pageUserResponseDTO = PageUserResponseDTO.builder()
                .last(memberPage.isFirst())
                .first(memberPage.isLast())
                .total(memberPage.getTotalElements())
                .content(dtos)
                .build();

        return pageUserResponseDTO;

    }



    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public PageReservationMemberDTO reservationMemberDTOS(Integer memberId,Integer page){

        Page<ReservationSubmitForm> pageData = formRepository.submitFormDataList(memberId,PageRequest.of(page,10));

        List<ReservationSubmitForm> submitForms = pageData.getContent();
        List<ReservationMemberDTO> dtos = new ArrayList<>();

        submitForms.forEach(submitForm -> {
                String createdAtStr = submitForm.getCreatedAt().toString();
                Long idString = submitForm.getId();

                ReservationMemberDTO dto = ReservationMemberDTO.builder()
                        .createdAt(submitForm.getReservationDate())
                        .id(idString)
                        .build();

                dtos.add(dto);
        });

        PageReservationMemberDTO pageReservationMemberDTO = PageReservationMemberDTO.builder()
                .content(dtos)
                .first(pageData.isFirst())
                .last(pageData.isLast())
                .total(pageData.getTotalElements())
                .build();

        return pageReservationMemberDTO;
    }


    /**
     * @apiNote  고객관리에서 예약 폼 자세히 보기
     * */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public MemberResFormDTO memberResFormDTO(Long formId){
        ReservationSubmitForm submitForm = formRepository.submitFormWithMember(formId)
                .orElseThrow(() -> new CommonException(ErrorCode.RESERVATION_FORM_MISSING_VALUE));

        List<ReservationAnswer> answerList = submitForm.getReservationAnswerList();
        Map<QuestionIdentify,String> answerMap = answerList.stream()
                .collect(Collectors
                        .toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));

        Member member = submitForm.getMember();

        MemberResFormDTO dto = MemberResFormDTO.builder()
                .applicantName(answerMap.get(QuestionIdentify.APPLICANTNAME))
                .applicantContactInfo(answerMap.get(QuestionIdentify.APPLICANTCONACTINFO))
                .address(answerMap.get(QuestionIdentify.ADDRESS))
                .aFewServings(answerMap.get(QuestionIdentify.AFEWSERVINGS))
                .serviceDuration(answerMap.get(QuestionIdentify.SERVICEDURATION))
                .serviceDate(answerMap.get(QuestionIdentify.SERVICEDATE))
                .parkingAvailable(answerMap.get(QuestionIdentify.PARKINGAVAILABLE))
                .reservationNote(answerMap.get(QuestionIdentify.RESERVATIONNOTE))
                .reservationRequest(answerMap.get(QuestionIdentify.RESERVATIONREQUEST))
                .reservationEnter(answerMap.get(QuestionIdentify.RESERVATIONENTER))
                .discountCode(member.getDiscountCode() != null ? member.getDiscountCode().getCode() : "미발급")
                .learnedRoute(answerMap.get(QuestionIdentify.LEARNEDROUTE))
                .build();

        return dto;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void memoModify(MemoRequestDTO memo) {
        Member member = memberRepository.findById(memo.getMemberId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MEMBER));

        member.changeMemo(memo.getMemo());
    }
}
