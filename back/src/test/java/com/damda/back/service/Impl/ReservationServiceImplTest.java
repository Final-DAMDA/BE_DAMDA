package com.damda.back.service.Impl;

import com.damda.back.data.common.CategoryMapDTO;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.QuestionType;
import com.damda.back.data.request.FormModifyDTO;
import com.damda.back.data.request.RearrangeRequestDTO;
import com.damda.back.data.request.ReservationFormRequestDTO;
import com.damda.back.domain.Category;
import com.damda.back.domain.Question;
import com.damda.back.domain.QuestionStatus;
import com.damda.back.repository.QuestionRepository;
import com.damda.back.repository.ReservationFormRepository;
import com.damda.back.service.ReservationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest{

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private QuestionRepository questionRepository;

    Question question;
    Question question2;

    @Test
    @DisplayName("설문 데이터 리스트 저장")
    void ReservationServiceImplTest() {
        // given
        createData();

        ReservationFormRequestDTO dto = ReservationFormRequestDTO
                .builder()
                .questionTitle("사용할 시간을 선택해주세요")
                .page(1)
                .order(1)
                .questionType(QuestionType.SELECT)
                .questionIdentify(QuestionIdentify.SERVICEDURATION)
                .category(Map.of("1시간",50000,"2시간",1000000))
                .build();

        ReservationFormRequestDTO dto2 = ReservationFormRequestDTO
                .builder()
                .questionTitle("어디 사세요?")
                .page(1)
                .order(2)
                .placeHolder("주소를 입력해주세요")
                .questionType(QuestionType.STRING)
                .questionIdentify(QuestionIdentify.ADDRESS)
                .build();

        List<ReservationFormRequestDTO> dtos = List.of(dto,dto2);
        serialize(dtos);
        // when
        reservationService.reservationForm(dtos);
        // then
    }

    @Test
    @DisplayName("재배열 테스트")
    void reservation_rearrange_test() {
        // given
        List<RearrangeRequestDTO> list = reArrangeData();
        // when
        serialize(list);
        // then
        reservationService.reArrangeQuestion(list);

    }

    void createData(){
         question = Question.builder()
                .questionNumber(1L)
                .questionIdentify(QuestionIdentify.SERVICEDATE)
                .page(1)
                .order(1)
                .placeHolder("데이터를 입력해주세요")
                .questionTitle("언제 서비스 받을 건가여?")
                .questionType(QuestionType.STRING)
                .status(QuestionStatus.ACTIVATION)
                .required(true).build();

         question2 = Question.builder()
                .questionNumber(2L)
                .questionIdentify(QuestionIdentify.SERVICEDURATION)
                .page(1)
                .order(2)
                .placeHolder(null)
                .questionTitle("서비스는 몇 시간 이용하시나요?")
                .questionType(QuestionType.SELECT)
                .status(QuestionStatus.ACTIVATION)
                .required(true).build();

        RearrangeRequestDTO dto = RearrangeRequestDTO.builder()
                .questionNumber(1L)
                .questionOrder(1)
                .build();

        RearrangeRequestDTO dto2 = RearrangeRequestDTO.builder()
                .questionNumber(2L)
                .questionOrder(2)
                .build();
    }

    String serialize(Object object) {
        try{
            String response = new ObjectMapper().writeValueAsString(object);
            System.out.println(response);
            return response;
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    List reArrangeData(){
        return List.of(RearrangeRequestDTO.builder()
                        .questionNumber(2L)
                        .questionOrder(1)
                        .page(2)
                .build(),
                RearrangeRequestDTO.builder()
                        .questionNumber(1L)
                        .questionOrder(2)
                        .page(1)
                        .build());
    }

}