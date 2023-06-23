package com.damda.back.service.Impl;


import com.damda.back.data.common.CategoryMapDTO;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.request.AddCategoryRequestDTO;
import com.damda.back.data.request.FormModifyDTO;
import com.damda.back.data.request.RearrangeRequestDTO;
import com.damda.back.data.request.ReservationFormRequestDTO;
import com.damda.back.data.response.AdminFormResponseDTO;
import com.damda.back.data.response.ReservationResponseDTO;
import com.damda.back.domain.Category;
import com.damda.back.domain.Question;
import com.damda.back.domain.QuestionStatus;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.AreaRepository;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.QuestionRepository;
import com.damda.back.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final QuestionRepository questionRepository;

    private final ManagerRepository managerRepository;

    private final AreaRepository areaRepository;


    @Transactional(isolation = Isolation.REPEATABLE_READ,readOnly = true)
    public List<ReservationResponseDTO> reservationResponseDTOList(){

        List<ReservationResponseDTO> reservationResponseDTOList = new ArrayList<>();

        questionRepository.questionList().forEach(question -> {
            List<CategoryMapDTO> list = new ArrayList<>();

            if(question.getQuestionIdentify().equals(QuestionIdentify.ADDRESS)){

            }

            ReservationResponseDTO dto =  ReservationResponseDTO.builder()
                    .questionNumber(question.getQuestionNumber())
                    .questionTitle(question.getQuestionTitle())
                    .page(question.getPage())
                    .placeHolder(question.getPlaceHolder())
                    .questionIdentify(question.getQuestionIdentify())
                    .questionOrder(question.getOrder())
                    .questionType(question.getQuestionType())
                    .required(question.isRequired())
                    .build();

            question.getCategoryList().forEach(category -> {
                CategoryMapDTO dtoData = CategoryMapDTO.builder()
                        .id(category.getId())
                        .category(category.getQuestionCategory())
                        .build();

                list.add(dtoData);
            });
            dto.setCategoryList(list);

            reservationResponseDTOList.add(dto);
        });
        return reservationResponseDTOList;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ,readOnly = true)
    public List<AdminFormResponseDTO> adminFormDTOList() {
        List<AdminFormResponseDTO> adminFormResponseDTOS = new ArrayList<>();

        questionRepository.adminQuestionList().forEach(question -> {
            List<CategoryMapDTO> list = new ArrayList<>();

            AdminFormResponseDTO dto =  AdminFormResponseDTO.builder()
                    .questionNumber(question.getQuestionNumber())
                    .questionTitle(question.getQuestionTitle())
                    .questionOrder(question.getOrder())
                    .questionIdentify(question.getQuestionIdentify())
                    .page(question.getPage())
                    .placeHolder(question.getPlaceHolder())
                    .questionType(question.getQuestionType())
                    .isDeleted(question.getStatus().equals(QuestionStatus.DEACTIVATION))
                    .required(question.isRequired())
                    .build();

            question.getCategoryList().forEach(category -> {
                CategoryMapDTO dtoData = CategoryMapDTO.builder()
                        .id(category.getId())
                        .category(category.getQuestionCategory())
                        .build();

                list.add(dtoData);
            });
            dto.setCategoryList(list);

            adminFormResponseDTOS.add(dto);
        });
        return adminFormResponseDTOS;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void reservationForm(List<ReservationFormRequestDTO> dto){
        List<Category> categoryList = new ArrayList<>();

        for (ReservationFormRequestDTO reservationFormRequestDTO : dto) {

            Question question = Question.builder()
                    .questionType(reservationFormRequestDTO.getQuestionType())
                    .questionTitle(reservationFormRequestDTO.getQuestionTitle())
                    .questionIdentify(reservationFormRequestDTO.getQuestionIdentify())
                    .page(reservationFormRequestDTO.getPage())
                    .order(reservationFormRequestDTO.getOrder())
                    .status(QuestionStatus.ACTIVATION)
                    .placeHolder(reservationFormRequestDTO.getPlaceHolder() != null ? reservationFormRequestDTO.getPlaceHolder() : "없음")
                    .required(reservationFormRequestDTO.isRequired())
                    .build();


            reservationFormRequestDTO.getCategory().forEach((category, price) -> {
                Category categoryData = Category.builder()
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


    /**
     * @apiNote questionTitle, questionType,order,required,questionIUdentify 수정가능함
     *      Category에 Price 빠지고 검증완료
     * */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void formModify(Long id, FormModifyDTO dto){
        Optional<Question> optional = questionRepository.selectQuestionJoin(id);

        if(optional.isPresent()){
            Question question = optional.get();

            //test
            question.changeIdentify(dto.getQuestionIdentify());
            question.changeQuestionType(dto.getQuestionType());
            question.changeQuestionTitle(dto.getQuestionTitle());
            question.changeRequired(dto.isRequired());
            question.changeOrder(dto.getQuestionOrder());
            question.changePlaceHolder(dto.getPlaceHolder());
            question.changePage(dto.getPage());

        }else {
            throw new CommonException(ErrorCode.NOT_FOUND_QUESTION_MODIFIED);
        }

    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void reArrangeQuestion(List<RearrangeRequestDTO> dto) {
        Map<Long,RearrangeRequestDTO> map = new HashMap<>();
        List<Question> list = questionRepository.selectWhereACTIVATION();


        try{
            for(RearrangeRequestDTO dtoData : dto){
                map.put(dtoData.getQuestionNumber(),dtoData);
            }

            for (Question question : list){
                RearrangeRequestDTO reArrangeData = map.get(question.getQuestionNumber());

                if(reArrangeData != null){
                    question.changeOrder(reArrangeData.getQuestionOrder());
                    question.changePage(reArrangeData.getPage());
                }
            }
        }catch (Exception e){
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void formDataDelete(Long id) {
        Optional<Question> optional = questionRepository.selectQuestionOne(id);

        if(optional.isPresent()){
            Question question = optional.get();

            question.changeStatus(QuestionStatus.DEACTIVATION);
        }else{
            throw new CommonException(ErrorCode.NOT_FOUND_QUESTION);
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void formDataActivation(Long id) {
        Optional<Question> optional = questionRepository.selectQuestionOneDeactivation(id);

        if(optional.isPresent()){
            Question question = optional.get();

            question.changeStatus(QuestionStatus.ACTIVATION);
        }else{
            throw new CommonException(ErrorCode.NOT_FOUND_QUESTION);
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void formAddCategory(Long id, AddCategoryRequestDTO dto) {
        Optional<Question> optional = questionRepository.selectQuestionJoin(id);
        log.info("data {}",optional);
        if(optional.isPresent()){
            Question question = optional.get();

            dto.getData().forEach((string) -> {
                Category category = Category.builder()
                        .questionCategory(string)
                        .build();

                log.info("MAP 카테고리 데이터 +> {}",dto);
                question.addCategory(category);
            });

        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void formDeleteCategory(Long id) {
            Optional<Category> optional = questionRepository.selectCategoryOne(id);

            if(optional.isPresent()){
                questionRepository.deleteCategory(id);
            }else{
                throw new CommonException(ErrorCode.NOT_FOUND_CATEGORY);
            }
    }

    public Map<String,List<String>> activityArea() {

        Map<String, List<String>> locaitonMap = new HashMap<>();

        locaitonMap.put("서울특별시", new ArrayList<>());
        locaitonMap.put("경기도", new ArrayList());

        areaRepository.searchActivityArea().forEach(area -> {
            if (locaitonMap.containsKey(area.getCity())) {
                locaitonMap.get(area.getCity()).add(area.getDistrict());
            }
        });

        return locaitonMap;
    }


}
