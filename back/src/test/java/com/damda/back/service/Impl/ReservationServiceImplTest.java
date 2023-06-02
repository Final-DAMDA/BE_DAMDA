package com.damda.back.service.Impl;

import com.damda.back.data.common.CategoryMapDTO;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.QuestionType;
import com.damda.back.data.request.FormModifyDTO;
import com.damda.back.data.request.RearrangeRequestDTO;
import com.damda.back.domain.Category;
import com.damda.back.domain.Question;
import com.damda.back.domain.QuestionStatus;
import com.damda.back.repository.QuestionRepository;
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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {


    @InjectMocks
    ReservationServiceImpl reservationService;

    @Mock
    QuestionRepository questionRepository;

    @Test
    @DisplayName("수정 쿼리 확인하기")
    void form_modify_query_check() throws JsonProcessingException {
        // given
        Question question = Question.builder()
                .questionNumber(1L)
                .questionIdentify(QuestionIdentify.SERVICEDATE)
                .questionTitle("이것이 문항이다.")
                .questionType(QuestionType.SELECT)
                .required(true).build();


        FormModifyDTO dto = FormModifyDTO.builder()
                .order(3)
                .questionTitle("수정된 문항입니다.22")
                .questionType(QuestionType.SELECT)
                .required(true)
                .questionIdentify(QuestionIdentify.SERVICEDATE)
                .build();

        List<CategoryMapDTO> dtoList = new ArrayList<>();
        CategoryMapDTO categoryMapDTO = CategoryMapDTO.builder()
                .id(4L)
                .category("하남시22")
                .price(1234)
                .build();
        CategoryMapDTO categoryMapDTO2 = CategoryMapDTO.builder()
                .id(6L)
                .category("구로구22")
                .price(1234)
                .build();

        dtoList.add(categoryMapDTO);
        dtoList.add(categoryMapDTO2);

        dto.setCategoryList(dtoList);

        // when
        when(questionRepository.selectQuestionJoin(1L)).thenReturn(Optional.of(question));

        // then
        reservationService.formModify(1L,dto);
    }

    @Test
    @DisplayName("재정렬하기")
    void form_rearrange() throws JsonProcessingException {
        // given
        Question question = Question.builder()
                .questionNumber(1L)
                .questionIdentify(QuestionIdentify.SERVICEDATE)
                .order(2)
                .questionTitle("이것이 문항이다.")
                .questionType(QuestionType.SELECT)
                .status(QuestionStatus.ACTIVATION)
                .required(true).build();

        Question question2 = Question.builder()
                .questionNumber(2L)
                .questionIdentify(QuestionIdentify.SERVICEDATE)
                .questionTitle("이것이 문항이다.")
                .order(3)
                .status(QuestionStatus.ACTIVATION)
                .questionType(QuestionType.SELECT)
                .required(true).build();

        RearrangeRequestDTO dto = RearrangeRequestDTO.builder()
                .questionNumber(1L)
                .order(1)
                .build();

        RearrangeRequestDTO dto2 = RearrangeRequestDTO.builder()
                .questionNumber(2L)
                .order(2)
                .build();

        System.out.println(new ObjectMapper().writeValueAsString(List.of(dto,dto2)));
        when(questionRepository.selectWhereACTIVATION()).thenReturn(List.of(question,question2));

        // when
        reservationService.reArrangeQuestion(List.of(dto,dto2));
        // then
    }


    @Test
    @DisplayName("예약폼 삭제")
    void reservation_form_delete_test() {
        // given
        Question question = Question.builder()
                .questionNumber(1L)
                .questionIdentify(QuestionIdentify.SERVICEDATE)
                .order(2)
                .status(QuestionStatus.ACTIVATION)
                .questionTitle("이것이 문항이다.")
                .questionType(QuestionType.SELECT)
                .required(true).build();

        // when
        when(questionRepository.selectQuestionOne(1L)).thenReturn(Optional.of(question));

        // then
        reservationService.formDataDelete(1L);
    }

    @Test
    @DisplayName("삭제된 거 다시 활성화하기")
    void reservation_form_activation_test() {
        // given
        Question question = Question.builder()
                .questionNumber(1L)
                .questionIdentify(QuestionIdentify.SERVICEDATE)
                .order(2)
                .status(QuestionStatus.ACTIVATION)
                .questionTitle("이것이 문항이다.")
                .questionType(QuestionType.SELECT)
                .required(true).build();

        // when
        when(questionRepository.selectQuestionOneDeactivation(1L)).thenReturn(Optional.of(question));

        // then
        reservationService.formDataActivation(1L);
    }


    @Test
    @DisplayName("카테고리 삭제")
    void reservation_category_remove() {
        // given
        Category category = Category.builder()
                .categoryPrice(3000)
                .questionCategory("강남구")
                .id(1L)
                .build();
        // when
        given(questionRepository.selectCategoryOne(1L)).willReturn(Optional.of(category));
        // then
        reservationService.formDeleteCategory(1L);
    }


}