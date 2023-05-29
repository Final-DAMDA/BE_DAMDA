package com.damda.back.service.Impl;


import com.damda.back.data.request.ReservationFormRequestDTO;
import com.damda.back.data.response.AdminFormResponseDTO;
import com.damda.back.data.response.ReservationResponseDTO;
import com.damda.back.domain.Category;
import com.damda.back.domain.Question;
import com.damda.back.domain.QuestionStatus;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.QuestionRepository;
import com.damda.back.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final QuestionRepository questionRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ,readOnly = true)
    public List<ReservationResponseDTO> reservationResponseDTOList(){

        List<ReservationResponseDTO> reservationResponseDTOList = new ArrayList<>();

        questionRepository.questionList().forEach(question -> {
            Map<String,Integer> map = new HashMap<>();

            ReservationResponseDTO dto =  ReservationResponseDTO.builder()
                    .questionNumber(question.getQuestionNumber())
                    .questionTitle(question.getQuestionTitle())
                    .questionOrder(question.getOrder())
                    .questionType(question.getQuestionType())
                    .build();

            question.getCategoryList().forEach(category -> {
                map.put(category.getQuestionCategory(),category.getCategoryPrice());
            });
            dto.setCategory(map);

            reservationResponseDTOList.add(dto);
        });
        return reservationResponseDTOList;
    }

    @Override
    public List<AdminFormResponseDTO> adminFormDTOList() {
        List<AdminFormResponseDTO> adminFormResponseDTOS = new ArrayList<>();

        questionRepository.adminQuestionList().forEach(question -> {
            Map<String,Integer> map = new HashMap<>();

            AdminFormResponseDTO dto =  AdminFormResponseDTO.builder()
                    .questionNumber(question.getQuestionNumber())
                    .questionTitle(question.getQuestionTitle())
                    .questionOrder(question.getOrder())
                    .questionType(question.getQuestionType())
                    .isDeleted(question.getStatus().equals(QuestionStatus.DEACTIVATION))
                    .build();

            question.getCategoryList().forEach(category -> {
                map.put(category.getQuestionCategory(),category.getCategoryPrice());
            });
            dto.setCategory(map);

            adminFormResponseDTOS.add(dto);
        });
        return adminFormResponseDTOS;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void reservationForm(ReservationFormRequestDTO dto){
        List<Category> categoryList = new ArrayList<>();

        Question question = Question.builder()
                .questionType(dto.getQuestionType())
                .questionTitle(dto.getQuestionTitle())
                .questionIdentify(dto.getQuestionIdentify())
                .order(questionRepository.selectMax()+1)
                .status(QuestionStatus.ACTIVATION)
                .build();


        dto.getMap().forEach((category, price) -> {
            Category categoryData = Category.builder()
                    .categoryPrice(price)
                    .questionCategory(category)
                    .build();

            question.addCategory(categoryData);
        });

        try{
            questionRepository.save(question);
        }catch (Exception e){
            throw new CommonException(ErrorCode.RESERVATION_FORM_SAVE_FAIL);
        }
    }


}
