package com.damda.back.service.Impl;


import com.damda.back.config.annotation.TimeChecking;
import com.damda.back.data.common.PayMentStatus;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.ReservationStatus;
import com.damda.back.data.common.SubmitSlice;
import com.damda.back.data.request.FormStatusModifyRequestDTO;
import com.damda.back.data.request.MatchingCompletedDTO;
import com.damda.back.data.request.SubmitRequestDTO;
import com.damda.back.data.response.FormResultDTO;
import com.damda.back.data.response.FormSliceDTO;
import com.damda.back.data.response.Statistical;
import com.damda.back.domain.*;
import com.damda.back.domain.manager.Manager;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.MatchRepository;
import com.damda.back.repository.MemberRepository;
import com.damda.back.repository.ReservationFormRepository;
import com.damda.back.service.CodeService;
import com.damda.back.service.MatchService;
import com.damda.back.service.SubmitService;
import com.damda.back.service.TalkSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubmitServiceImpl implements SubmitService {

        private final ReservationFormRepository reservationFormRepository;

        private final MemberRepository memberRepository;

        private final TalkSendService talkSendService;

        private final MatchRepository matchRepository;
        private final MatchService matchService;

        private final ManagerRepository managerRepository;

        private final CodeService codeService;


        private final ArrayList<QuestionIdentify> identifies = new ArrayList<>();




        private final JdbcTemplate jdbcTemplate;

        private final String CANCELURL = "https://api.solapi.com/messages/v4/groups/{groupId}/schedule";



        @PostConstruct
        private void questionIdentifyInit(){
            identifies.add(QuestionIdentify.AFEWSERVINGS); //몇인분량의 옷 (투입인원아님)/
            identifies.add(QuestionIdentify.SERVICEDURATION); //서비스 사용시간=/
            identifies.add(QuestionIdentify.ADDRESS); //서비스 주소=/
            identifies.add(QuestionIdentify.SERVICEDATE); //서비스 날짜와 시간=/
            identifies.add(QuestionIdentify.PARKINGAVAILABLE); //주차 가능여부=/
            identifies.add(QuestionIdentify.APPLICANTNAME); //신청인 이름/
            identifies.add(QuestionIdentify.APPLICANTCONACTINFO); //신청인 전화번호/
            identifies.add(QuestionIdentify.LEARNEDROUTE); // 알게된 경로
            identifies.add(QuestionIdentify.RESERVATIONENTER); //들어가기 위해 필요한 자료=/ -> 없다면 대체 문자열 보내줘야함 프론트에서
            identifies.add(QuestionIdentify.RESERVATIONNOTE); // 알아야 할 사항=/ -> 없다면 대체 문자열 보내줘야함 프론트에서
            identifies.add(QuestionIdentify.RESERVATIONREQUEST); // 요청사항=/ -> 없다면 대체 문자열 보내줘야함 프론트에서




        }



        @TimeChecking
        @Transactional(isolation = Isolation.REPEATABLE_READ,rollbackFor = CommonException.class)
        public Long saverFormSubmit(SubmitRequestDTO dto,Integer memberId){



            String formInsertSql = "INSERT INTO reservation_submit_form (status, total_price, deleted, member_id, pay_ment_status ,created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
            String answerInsertSql = "INSERT INTO reservation_answer (answer, question_identify, form_id) VALUES (?, ?, ?)";

            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);

            boolean isValid = dto.getSubmit().stream()
                    .map(SubmitSlice::getQuestionIdentify)
                    .collect(Collectors.toSet())
                    .containsAll(identifies);

            String dateAnswer = dto.getSubmit().stream()
                    .filter(submitSlice -> submitSlice.getQuestionIdentify().equals(QuestionIdentify.SERVICEDATE))
                    .findFirst().orElseThrow(() -> new CommonException(ErrorCode.DATE_FORMAT_EXCEPTION)).getAnswer();

            validDateFormat(dateAnswer);

            try {
                jdbcTemplate.getDataSource().getConnection().setAutoCommit(false);

                KeyHolder keyHolder = new GeneratedKeyHolder();

                int affectedRows = jdbcTemplate.update(con -> {
                    PreparedStatement formInsertStmt = con.prepareStatement(formInsertSql, Statement.RETURN_GENERATED_KEYS);
                    formInsertStmt.setString(1, ReservationStatus.WAITING_FOR_MANAGER_REQUEST.name());
                    formInsertStmt.setInt(2, dto.getTotalPrice());
                    formInsertStmt.setBoolean(3, false);
                    formInsertStmt.setLong(4, memberId);
                    formInsertStmt.setString(5, PayMentStatus.NOT_PAID_FOR_ANYTHING.name());
                    formInsertStmt.setTimestamp(6, timestamp);
                    formInsertStmt.setTimestamp(7, timestamp);
                    return formInsertStmt;
                }, keyHolder);

                if (affectedRows == 0) {
                    throw new SQLException("Creating reservation submit form failed, no rows affected.");
                }

                Long formId = keyHolder.getKey().longValue();
                List<Object[]> batchArgs = new ArrayList<>();

                dto.getSubmit().forEach(submitSlice -> {
                    Object[] values = { submitSlice.getAnswer(), submitSlice.getQuestionIdentify(), formId };
                    batchArgs.add(values);
                });

                int[] result = jdbcTemplate.batchUpdate(answerInsertSql, batchArgs, new int[] {Types.VARCHAR, Types.VARCHAR, Types.BIGINT});
                boolean success = true;
                for (int i : result) {
                    if (i == 0) {
                        success = false;
                        break;
                    }
                }
                if (success) {
                    log.info("예약 데이터 저장완료 key {}",formId);
                    jdbcTemplate.getDataSource().getConnection().commit();
                    return formId;
                } else {
                    jdbcTemplate.getDataSource().getConnection().rollback();
                    throw new SQLException("예약 Insert 하다가 실패하여 rollback함");
                }
            } catch (SQLException e) {
                System.out.println(e);
                throw new CommonException(ErrorCode.SERVER_ERROR);
            } catch (CommonException c) {
                System.out.println(c);
                throw new CommonException(ErrorCode.SERVER_ERROR);
            } finally {
                try {
                    jdbcTemplate.getDataSource().getConnection().setAutoCommit(true); //커밋모드 다시 되돌림
                } catch (SQLException e) {
                }
            }
        }


    /**
     * 실질적으로 쓰는 insert 메소드
     * 예약Insert시 실행되는 메소드
     */
    @TimeChecking
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Long jpaFormInsert(SubmitRequestDTO dto,Integer memberId){

        Set<QuestionIdentify> submittedIdentifies = new HashSet<>();
        boolean hasDuplicate = dto.getSubmit().stream()
                .map(SubmitSlice::getQuestionIdentify)
                .anyMatch(q -> !submittedIdentifies.add(q));

         boolean isValid = !hasDuplicate && submittedIdentifies.containsAll(identifies);



        if(isValid){
            List<ReservationAnswer> answers = new ArrayList<>();
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MEMBER));

            ReservationSubmitForm reservationSubmitForm = ReservationSubmitForm.builder()
                    .member(member)
                    .totalPrice(dto.getTotalPrice())
                    .status(ReservationStatus.WAITING_FOR_MANAGER_REQUEST)
                    .payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
                    .servicePerson(dto.getServicePerson())
                    .build();

        //    try{
                dto.getSubmit().forEach(submitSlice -> {
                    if(!StringUtils.hasText(submitSlice.getAnswer())) throw new CommonException(ErrorCode.RESERVATION_FORM_MISSING_VALUE);

                    if(submitSlice.getQuestionIdentify().equals(QuestionIdentify.SERVICEDATE)){
                        reservationSubmitForm.changeReservationDate(submitSlice.getAnswer());
                    }

                    reservationSubmitForm.addAnswer(ReservationAnswer.builder()
                            .questionIdentify(submitSlice.getQuestionIdentify())
                            .answer(submitSlice.getAnswer())
                            .build());

                });

                ReservationSubmitForm form = reservationFormRepository.save(reservationSubmitForm);
                //TODO: 매칭로직 추가


                log.info("{} 지역 매니저들을 조회 시도",dto.getAddressFront());
                List<Manager> managerList = managerRepository.managerWithArea(dto.getAddressFront());

                if(managerList.isEmpty()) throw new CommonException(ErrorCode.ACTIVITY_MANAGER_NOT_FOUND);
                log.info("해당 지역에 활동가능한 매니저 {}",managerList);
                matchService.matchingListUp(reservationSubmitForm,managerList);


                talkSendService.sendReservationSubmitAfter(form.getId(),dto.getAddressFront(),form.getReservationAnswerList(),dto.getTotalPrice(),dto.getServicePerson(),managerList);


                return form.getId();
//                }catch (Exception e){
//                    throw new CommonException(ErrorCode.ERROR_WHILE_SUBMITTING_USER_FORM);
//                 }

        }else{
            throw new CommonException(ErrorCode.NO_REQUIRED_VALUE);
        }
    }


       public void validDateFormat(String answer){
           String expectedFormat = "yyyy-MM-dd HH:mm:ss";
           SimpleDateFormat dateFormat = new SimpleDateFormat(expectedFormat);
           try {
               dateFormat.parse(answer);
           } catch (ParseException e) {
               throw new CommonException(ErrorCode.DATE_FORMAT_EXCEPTION);
           }
       }


       /**
        * @apiNote 어드민 메인에서 보여질 데이터 통계함수와 페이징한 예약건수들을 보여준다.
        * 페이징이 필요한 데이터에서는
        * 순번(이건프론트), 신청일자, 사용자 이름, 연락처, 주소, 예약일자, 가격 ,소요시간, 매니저 인원
        *  ,매니저 매칭(누가지원했는지), 서비스 상태(ReservationStatus),  결제상태
        * */
       @Transactional(isolation = Isolation.REPEATABLE_READ,readOnly = true)
       public FormResultDTO submitTotalResponse(int page,String startDate,String endDate,String sort){
            //TODO: 통계함수랑 매니저 조인해서 가져온 데이터 짬뽕해서 DTO 반환예쩡 통계함수 완성함

           Timestamp startDateTimeStamp = startDate != null ? Timestamp.valueOf(startDate + " 00:00:00") : null;
           Timestamp endDateTimeStamp = endDate != null ? Timestamp.valueOf(endDate + " 23:59:59") : null;

            Map<ReservationStatus,Long> map = reservationFormRepository.statistical();
            List<FormSliceDTO> dtos = new ArrayList<>();

            Statistical statistical = new Statistical();
            for(Map.Entry<ReservationStatus, Long> entry : map.entrySet()){
                Long count = entry.getValue() == null ? 0 : entry.getValue();

                switch (entry.getKey()){
                    case SERVICE_COMPLETED -> statistical.setCompleted(count);
                    case MANAGER_MATCHING_COMPLETED -> statistical.setConfirmation(count);
                    case WAITING_FOR_ACCEPT_MATCHING -> statistical.setMatching(count);
                    case WAITING_FOR_MANAGER_REQUEST -> statistical.setWaiting(count);
                    case RESERVATION_CANCELLATION -> statistical.setCancellation(count);
                }
            }
            statistical.nullInit();

            Page<ReservationSubmitForm> submitFormPage =
                    reservationFormRepository.formPaging(PageRequest.of(page,10),startDateTimeStamp,endDateTimeStamp,sort);

           for (ReservationSubmitForm submitForm : submitFormPage) {
                    dtos.add(asDTO(submitForm));
           }
           FormResultDTO resultDTO = FormResultDTO.builder()
                   .total(submitFormPage.getTotalElements())
                   .content(dtos)
                   .first(submitFormPage.isFirst())
                   .last(submitFormPage.isLast())
                   .statistical(statistical)
                   .build();

            return resultDTO;
       }



       /**
        * @Deprecated 상태값 변경에 제한이 생겼기 때문에 해당 메소드는 사용안함
        * */
       @Transactional(isolation = Isolation.REPEATABLE_READ)
       public void statusModify(FormStatusModifyRequestDTO dto){
           ReservationSubmitForm form = reservationFormRepository.submitFormWithAnswer(dto.getId()).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RESERIVATION));
           List<ReservationAnswer> answers = form.getReservationAnswerList();

           Map<QuestionIdentify, String> answerMap
                   = answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));


           if(dto.getStatus().equals(ReservationStatus.MANAGER_MATCHING_COMPLETED) && !form.getStatus().equals(ReservationStatus.MANAGER_MATCHING_COMPLETED)){
               //매니저 매칭완료로 변경시
               List<Long> managerList = new ArrayList<>();
               List<String> phoneNumbers = new ArrayList<>();

               MatchingCompletedDTO data = MatchingCompletedDTO.builder()
                       .reservationDate(answerMap.get(QuestionIdentify.SERVICEDATE))
                       .reservationAddress(answerMap.get(QuestionIdentify.ADDRESS))
                       .reservationHour(answerMap.get(QuestionIdentify.SERVICEDURATION))
                       .build();

               form.changeStatus(dto.getStatus());
               form.getMatches().forEach(match -> {
                   managerList.add(match.getManagerId());
               });

               managerRepository.managers(managerList).forEach(manager -> {
                    log.info("수신자들 {} : {}",manager.getManagerName(),manager.getPhoneNumber());
                    phoneNumbers.add(manager.getPhoneNumber());
               });
               if(!phoneNumbers.isEmpty()) talkSendService.sendManagerWithCustomer(data,phoneNumbers);
           }else if(dto.getStatus().equals(ReservationStatus.SERVICE_COMPLETED)){ // 서비스완료시 입금 완료시로 변경해야함

               Member member = form.getMember();
               String discountCode = member.getDiscountCode();

               if(discountCode == null){
                   String code = "";
                   code = codeService.codePublish();

                   while (memberRepository.existCode(code)){
                       log.info("반복된 코드로 재발급요청");
                       code = codeService.codePublish();
                   }

                   log.info("발급된 할인코드 {}",code);

                   member.changeCode(code);
                   form.changeStatus(dto.getStatus());
               }else{
                   log.info("기존 코드 그대로 사용됨 {}",member.getDiscountCode());
               }
     //          talkSendService.sendCustomenrCompleted(
    //                   answerMap.get(QuestionIdentify.APPLICANTCONACTINFO),form.getId());

               form.changeStatus(dto.getStatus());
               //TODO: 서비스 완료시 고객에게 설문 조사 링크 보내야함 - 알림톡
           }else if(dto.getStatus().equals(ReservationStatus.RESERVATION_CANCELLATION)){

               form.changeStatus(dto.getStatus());

           }
       }



       /**
        * @apiNote 서비스 완료인 상태에서 form 엔티티가 결제완료로 변경 하는 것
        * */
        @Transactional(isolation = Isolation.REPEATABLE_READ)
        public void payMentCompleted(Long id){
            ReservationSubmitForm form = reservationFormRepository.submitFormWithAnswer(id).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RESERIVATION));
            List<ReservationAnswer> answers = form.getReservationAnswerList();

            if(!form.getStatus().equals(ReservationStatus.SERVICE_COMPLETED)) throw new CommonException(ErrorCode.STATUS_BAN_REQUEST);

            Map<QuestionIdentify, String> answerMap
                    = answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));

            form.paymentCompleted();



            Member member = form.getMember();
            String phoneNumber = member.getPhoneNumber() != null ? member.getPhoneNumber() : answerMap.get(QuestionIdentify.APPLICANTCONACTINFO);

            log.info("서비스 완료 알림톡을 보내는 번호 {}",phoneNumber);
            String discountCode = member.getDiscountCode();

            if(discountCode == null) {
                String code = "";
                code = codeService.codePublish();

                while (memberRepository.existCode(code)) {
                    log.info("반복된 코드로 재발급요청");
                    code = codeService.codePublish();
                }

                log.info("발급된 할인코드 {}", code);

                member.changeCode(code);
                talkSendService.sendCustomenrCompleted(phoneNumber,form.getId());

            }else {
                log.info("결제완료 톡 발송");
                talkSendService.sendCustomenrCompleted(phoneNumber,form.getId());
            }
        }

        @Transactional(isolation = Isolation.REPEATABLE_READ)
        public void cancellation(Long id) {
            ReservationSubmitForm form = reservationFormRepository.submitFormWithAnswer(id)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RESERIVATION));
            List<ReservationAnswer> answers = form.getReservationAnswerList();

            cancellationValid(form.getStatus());

            Map<QuestionIdentify, String> answerMap
                    = answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));

            form.cancellation();

            List<Match> managerList = matchRepository.matches(form.getId());
            List<String> managers = managerList.stream()
                    .filter(match ->  match.isMatching()) //매칭이 되었었던 매니저들
                    .map(Match::getManager)
                    .map(Manager::getPhoneNumber).collect(Collectors.toList());

            log.info("요청 보낸 번호 {}",managers);
            try {

                Optional<GroupIdCode> groupIdCode = reservationFormRepository.submitFormWithGroupId(form.getId());

                if(groupIdCode.isPresent()) {
                    GroupIdCode groupIdCodePE = groupIdCode.get();

                    for (String groupId : groupIdCodePE.nullCheckList()) {
                        String url = UriComponentsBuilder.fromUriString(CANCELURL).buildAndExpand(groupId).toUriString();
                        try{
                            ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.DELETE, null, String.class);
                        }catch (HttpClientErrorException httpClientErrorException){
                            log.info("예약취소 실패 {}",groupId);
                            continue;
                        }
                    }
                }

                if(!managers.isEmpty()) talkSendService.sendCancellation(managers, answerMap,form.getServicePerson());
            } catch (NurigoMessageNotReceivedException e) {
                throw new CommonException(ErrorCode.RESERVATION_CANCEL_EXCEPTION);
            } catch (NurigoEmptyResponseException e) {
                throw new CommonException(ErrorCode.RESERVATION_CANCEL_EXCEPTION);
            } catch (NurigoUnknownException e) {
                throw new CommonException(ErrorCode.RESERVATION_CANCEL_EXCEPTION);
            }


        }


        public FormSliceDTO asDTO(ReservationSubmitForm submitForm){
            FormSliceDTO dto = new FormSliceDTO();

            List<String> managerNames = new ArrayList<>();

            if(submitForm.getStatus().equals(ReservationStatus.MANAGER_MATCHING_COMPLETED)) {
                managerNames = submitForm.getMatches().stream()
                        .filter(match -> match.isMatching()).map(Match::getManagerName).collect(Collectors.toList());
            }else{
                managerNames = submitForm.getMatches().stream().map(Match::getManagerName).toList();

            }

            Member member = submitForm.getMember();
            List<ReservationAnswer> answers =  submitForm.getReservationAnswerList();
            Map<QuestionIdentify, String> answerMap
                    = answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));



            dto.setId(submitForm.getId());
            dto.setAddress(answerMap.get(QuestionIdentify.ADDRESS));
            dto.setName(member.getUsername());
            dto.setCreatedAt(submitForm.getCreatedAt().toString());
            dto.setTotalPrice(submitForm.getTotalPrice());
            dto.setEstimate(answerMap.get(QuestionIdentify.SERVICEDURATION));
            dto.setPhoneNumber(answerMap.get(QuestionIdentify.APPLICANTCONACTINFO));
            dto.setReservationStatus(submitForm.getStatus());
            dto.setPayMentStatus(submitForm.getPayMentStatus());
            dto.setReservationDate(answerMap.get(QuestionIdentify.SERVICEDATE));
            dto.setManagerNames(managerNames);
            dto.setManageAmount(submitForm.getServicePerson());
            return dto;
        }


    public void cancellationValid(ReservationStatus status) {
        List<ReservationStatus> cancellationStatus = List.of(
                ReservationStatus.MANAGER_MATCHING_COMPLETED,
                ReservationStatus.WAITING_FOR_MANAGER_REQUEST,
                ReservationStatus.WAITING_FOR_ACCEPT_MATCHING
        );

        if (!cancellationStatus.contains(status)) {
            throw new CommonException(ErrorCode.STATUS_BAD_REQUEST);
        }
    }

}
