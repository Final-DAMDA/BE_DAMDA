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
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
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

    @Transactional(isolation = Isolation.REPEATABLE_READ,readOnly = true)
    public List<ReservationResponseDTO> reservationResponseDTOList(){

        List<ReservationResponseDTO> reservationResponseDTOList = new ArrayList<>();

        questionRepository.questionList().forEach(question -> {
            List<CategoryMapDTO> list = new ArrayList<>();

            if(question.getQuestionIdentify().equals(QuestionIdentify.ADDRESS)){
                    //메서드 실행해서 DistrictEnum 리스트 가져온다.
            }

            ReservationResponseDTO dto =  ReservationResponseDTO.builder()
                    .questionNumber(question.getQuestionNumber())
                    .questionTitle(question.getQuestionTitle())
                    .questionIdentify(question.getQuestionIdentify())
                    .questionOrder(question.getOrder())
                    .questionType(question.getQuestionType())
                    .required(question.isRequired())
                    .build();

            question.getCategoryList().forEach(category -> {
                CategoryMapDTO dtoData = CategoryMapDTO.builder()
                        .id(category.getId())
                        .category(category.getQuestionCategory())
                        .price(category.getCategoryPrice())
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
                    .questionType(question.getQuestionType())
                    .isDeleted(question.getStatus().equals(QuestionStatus.DEACTIVATION))
                    .required(question.isRequired())
                    .build();

            question.getCategoryList().forEach(category -> {
                CategoryMapDTO dtoData = CategoryMapDTO.builder()
                        .id(category.getId())
                        .category(category.getQuestionCategory())
                        .price(category.getCategoryPrice())
                        .build();

                list.add(dtoData);
            });
            dto.setCategoryList(list);

            adminFormResponseDTOS.add(dto);
        });
        return adminFormResponseDTOS;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void reservationForm(ReservationFormRequestDTO dto){
        List<Category> categoryList = new ArrayList<>();

        Integer order = questionRepository.selectMax()+1;
        order = dto.getQuestionIdentify().equals(QuestionIdentify.TITILE) ? 0 : order;

        Question question = Question.builder()
                .questionType(dto.getQuestionType())
                .questionTitle(dto.getQuestionTitle())
                .questionIdentify(dto.getQuestionIdentify())
                .order(order)
                .status(QuestionStatus.ACTIVATION)
                .required(dto.isRequired())
                .build();


        dto.getCategory().forEach((category, price) -> {
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


    /**
     * @apiNote questionTitle, questionType,order,required,questionIUdentify 수정가능함
     *
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
            question.changeOrder(dto.getOrder());

            //end
            Map<Long,CategoryMapDTO> modifyDTO = new HashMap<>();

            for(CategoryMapDTO dtoData : dto.getCategoryList()){
                modifyDTO.put(dtoData.getId(), dtoData);
            }

            for(Category category : question.getCategoryList()){
                CategoryMapDTO data = modifyDTO.get(category.getId());

                if(data != null){
                    category.changeCategory(data.getCategory());
                    category.changeCategoryPrice(data.getPrice());
                }//TODO: 없는 카테고리가 들어올 경우 여기서 저장하기 만들예정
            }
        }else {
            throw new CommonException(ErrorCode.NOT_FOUND_QUESTION_MODIFIED);
        }

    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void reArrangeQuestion(List<RearrangeRequestDTO> dto) {
        Map<Long,Integer> map = new HashMap<>();
        List<Question> list = questionRepository.selectWhereACTIVATION();
        try{
            for(RearrangeRequestDTO dtoData : dto){
                map.put(dtoData.getQuestionNumber(),dtoData.getOrder());
            }

            for (Question question : list){
                Integer orderNumber = map.get(question.getQuestionNumber());

                if(orderNumber != null){
                    question.changeOrder(orderNumber);
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

            dto.getData().forEach((s, integer) -> {
                Category category = Category.builder()
                        .questionCategory(s)
                        .categoryPrice(integer)
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


}
