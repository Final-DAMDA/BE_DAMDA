package com.damda.back.service.Impl;


import com.damda.back.config.annotation.TimeChecking;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.ReservationStatus;
import com.damda.back.data.common.SubmitSlice;
import com.damda.back.data.request.SubmitRequestDTO;
import com.damda.back.data.response.Statistical;
import com.damda.back.data.response.SubmitTotalResponse;
import com.damda.back.domain.Member;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.MemberRepository;
import com.damda.back.repository.ReservationFormRepository;
import com.damda.back.service.SubmitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmitServiceImpl implements SubmitService {

        private final ReservationFormRepository reservationFormRepository;

        private final MemberRepository memberRepository;

        private final ArrayList<QuestionIdentify> identifies = new ArrayList<>();

        private final DataSource dataSource;



        @PostConstruct
        private void questionIdentifyInit(){
            identifies.add(QuestionIdentify.AFEWSERVINGS);
            identifies.add(QuestionIdentify.SERVICEDURATION);
            identifies.add(QuestionIdentify.ADDRESS);
            identifies.add(QuestionIdentify.SERVICEDATE);
            identifies.add(QuestionIdentify.PARKINGAVAILABLE);
            identifies.add(QuestionIdentify.APPLICANTNAME);
            identifies.add(QuestionIdentify.APPLICANTCONACTINFO);
            identifies.add(QuestionIdentify.LEARNEDROUTE);
        }


        @TimeChecking
        @Transactional(isolation = Isolation.REPEATABLE_READ)
        public boolean saverFormSubmit(SubmitRequestDTO dto,Integer memberId){
            String formInsertSql = "INSERT INTO reservation_submit_form (status, total_price, deleted, member_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
            String answerInsertSql = "INSERT INTO reservation_answer (answer, question_identify, form_id) VALUES (?, ?, ?)";

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement formInsertStmt = conn.prepareStatement(formInsertSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement answerInsertStmt = conn.prepareStatement(answerInsertSql)) {

                boolean isValid = dto.getSubmit().stream()
                        .map(SubmitSlice::getQuestionIdentify)
                        .collect(Collectors.toSet())
                        .containsAll(identifies);

                if (isValid) {

                    formInsertStmt.setString(1, ReservationStatus.WAITING_FOR_MANAGER_REQUEST.name());
                    formInsertStmt.setInt(2, dto.getTotalPrice());
                    formInsertStmt.setBoolean(3, false);
                    formInsertStmt.setLong(4, memberId);
                    formInsertStmt.setString(5, formattedDateTime);
                    formInsertStmt.setString(6, formattedDateTime);

                    int affectedRows = formInsertStmt.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating reservation submit form failed, no rows affected.");
                    }

                    try (ResultSet generatedKeys = formInsertStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            Long formId = generatedKeys.getLong(1);
                            List<ReservationAnswer> answers = new ArrayList<>();
                            dto.getSubmit().forEach(submitSlice -> {
                                answers.add(ReservationAnswer.builder()
                                        .formId(formId)
                                        .questionIdentify(submitSlice.getQuestionIdentify())
                                        .answer(submitSlice.getAnswer())
                                        .build());
                            });
                            bulkInsert(answerInsertStmt, answers);

                            //TODO: 알림톡 보내기 매니저들에게 매칭된 매니저들에게
                        } else {
                            throw new SQLException("Creating reservation submit form failed, no ID obtained.");
                        }
                    }
                } else {
                    throw new CommonException(ErrorCode.NO_REQUIRED_VALUE);
                }

                return true;
            } catch (SQLException e) {
                throw new CommonException(ErrorCode.SERVER_ERROR);
            }
        }



        @Transactional(isolation = Isolation.REPEATABLE_READ)
        public List<ReservationAnswer> jpaFormInsert(SubmitRequestDTO dto,Integer memberId){
            boolean isValid = dto.getSubmit().stream()
                    .map(SubmitSlice::getQuestionIdentify)
                    .collect(Collectors.toSet())
                    .containsAll(identifies);

            if(isValid){
                List<ReservationAnswer> answers = new ArrayList<>();
                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MEMBER));

                ReservationSubmitForm reservationSubmitForm = ReservationSubmitForm.builder()
                        .member(member)
                        .totalPrice(dto.getTotalPrice())
                        .status(ReservationStatus.WAITING_FOR_MANAGER_REQUEST)
                        .build();


                try{
                    ReservationSubmitForm form = reservationFormRepository.save(reservationSubmitForm);
                    dto.getSubmit().forEach(submitSlice -> {
                        answers.add( ReservationAnswer.builder()
                                .formId(form.getId())
                                .questionIdentify(submitSlice.getQuestionIdentify())
                                .answer(submitSlice.getAnswer())
                                .build());
                    });
                    return answers;
                }catch (Exception e){
                    throw new CommonException(ErrorCode.ERROR_WHILE_SUBMITTING_USER_FORM);
                 }

            }else{
                throw new CommonException(ErrorCode.NO_REQUIRED_VALUE);
            }
        }
       // @Transactional(propagation = Propagation.REQUIRED)
       public void bulkInsert(PreparedStatement pstmt, List<ReservationAnswer> reservationAnswers) {
            try {
                pstmt.clearBatch();
                for (ReservationAnswer answer : reservationAnswers) {
                    pstmt.setString(1, answer.getAnswer());
                    pstmt.setString(2, answer.getQuestionIdentify().name());
                    pstmt.setLong(3, answer.getFormId());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
       }


       /**
        * @apiNote 어드민 메인에서 보여질 데이터 통계함수와 페이징한 예약건수들을 보여준다.
        * 페이징이 필요한 데이터에서는
        * 순번(이건프론트), 신청일자, 사용자 이름, 연락처, 주소, 예약일자, 가격 ,소요시간, 매니저 인원
        *  ,매니저 매칭(누가지원했는지), 서비스 상태(ReservationStatus),  결제상태
        * */
       @Transactional(isolation = Isolation.REPEATABLE_READ)
       public SubmitTotalResponse submitTotalResponse(int page){
            //TODO: 통계함수랑 매니저 조인해서 가져온 데이터 짬뽕해서 DTO 반환예쩡 통계함수 완성함

            Map<ReservationStatus,Long> map = reservationFormRepository.statistical();


            Statistical statistical = new Statistical();
            for(Map.Entry<ReservationStatus, Long> entry : map.entrySet()){
                Long count = entry.getValue();

                switch (entry.getKey()){
                    case PAYMENT_COMPLETED -> statistical.setCompleted(count);
                    case MANAGER_MATCHING_COMPLETED -> statistical.setConfirmation(count);
                    case WAITING_FOR_ACCEPT_MATCHING -> statistical.setMatching(count);
                    case WAITING_FOR_MANAGER_REQUEST -> statistical.setWating(count);
                    case RESERVATION_CANCELLATION -> statistical.setCancellation(count);
                }
            }
            //TODO: 통계함수랑 페이징 결과를 함께 리턴한다.

            return null;
       }




}
