package com.damda.back.controller;


import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.request.AddCategoryRequestDTO;
import com.damda.back.data.request.FormModifyDTO;
import com.damda.back.data.request.RearrangeRequestDTO;
import com.damda.back.data.request.ReservationFormRequestDTO;
import com.damda.back.service.Impl.ReservationServiceImpl;
import com.damda.back.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {


        private final ReservationService reservationService;



        /**
         * @apiNote 서비스 가능지역 Map으로 리턴함 -> Area 테이블에 managerCount가 0이 아닌거 다 가져옴
         *
         * */
        @GetMapping("/api/v1/activity/locations")
        public ResponseEntity<CommonResponse<?>> locations(){

            CommonResponse<?> commonResponse = CommonResponse
                    .builder()
                    .codeEnum(CodeEnum.SUCCESS)
                    .data(reservationService.activityArea())
                    .build();

            return ResponseEntity
                    .status(commonResponse.getStatus())
                    .body(commonResponse);

        }

        /**
         * @apiNote 일반 유저가 예약폼을 조회
         * @apiNote  토큰 검사해야함 / 비회원은 받지 않음
         * */
        @GetMapping("/api/v1/form/list")
        public ResponseEntity<CommonResponse<?>> reservationList(){

            CommonResponse<?> commonResponse = CommonResponse
                    .builder()
                    .codeEnum(CodeEnum.SUCCESS)
                    .data(reservationService.reservationResponseDTOList())
                    .build();

            return ResponseEntity
                    .status(commonResponse.getStatus())
                    .body(commonResponse);

        }

        /**
         * @apiNote 관리자가 삭제된 예약폼까지 모두 본다. 활성화/비활성화
         * @apiNote 유저가 보는 것과 크게 다르지 않지만 isDeleted가 추가된다.
         * */
        @GetMapping("/api/v1/admin/form/list")
        public ResponseEntity<CommonResponse<?>> adminReservationList(){

            CommonResponse<?> commonResponse = CommonResponse
                    .builder()
                    .codeEnum(CodeEnum.SUCCESS)
                    .data(reservationService.adminFormDTOList())
                    .build();

            return ResponseEntity
                    .status(commonResponse.getStatus())
                    .body(commonResponse);
        }

    /**
     * @apiNote 어드민이 예약폼 데이터를 저장한다.
     *
     * @deprecated 기획에서 빠짐
     * */
        @PostMapping("/api/v1/admin/form/save")
        public ResponseEntity<CommonResponse<?>> reservationFormSave(
                @RequestBody List<ReservationFormRequestDTO> dto){

            reservationService.reservationForm(dto);

            CommonResponse<?> commonResponse = CommonResponse
                    .builder()
                    .codeEnum(CodeEnum.SUCCESS)
                    .data(true)
                    .build();

            return ResponseEntity
                    .status(commonResponse.getStatus())
                    .body(commonResponse);

        }

        @PutMapping("/api/v1/admin/form/order")
        public ResponseEntity<CommonResponse<?>> reArrange(@RequestBody List<RearrangeRequestDTO> data){
            reservationService.reArrangeQuestion(data);

            CommonResponse<?> commonResponse = CommonResponse
                    .builder()
                    .codeEnum(CodeEnum.SUCCESS)
                    .data(true)
                    .build();

            return ResponseEntity
                    .status(commonResponse.getStatus())
                    .body(commonResponse);

        }

        /**
         * @apiNote id 값을 받아서 해당 Question을 찾고 존재하는 데이털 선에서 수정을 시도한다. 새로운 데이터가 들어와도 기존에 존재하지 않으면 아무것도 안 함
         * */
        @PutMapping("/api/v1/admin/form/{id}")
        public ResponseEntity<CommonResponse<?>> reservationFormModify(
                @Valid @RequestBody FormModifyDTO dto,
                @PathVariable Long id){

            reservationService.formModify(id,dto);

            CommonResponse<?> commonResponse = CommonResponse
                    .builder()
                    .codeEnum(CodeEnum.SUCCESS)
                    .data(true)
                    .build();

            return ResponseEntity
                    .status(commonResponse.getStatus())
                    .body(commonResponse);
        }
        @PutMapping("/api/v1/admin/form/{id}/add/category")
        public ResponseEntity<CommonResponse<?>> reservationFormAddCategory(
                @PathVariable Long id,
                @RequestBody AddCategoryRequestDTO dto
                ){

            reservationService.formAddCategory(id,dto);

            CommonResponse<?> commonResponse = CommonResponse
                    .builder()
                    .codeEnum(CodeEnum.SUCCESS)
                    .data(true)
                    .build();

            return ResponseEntity
                    .status(commonResponse.getStatus())
                    .body(commonResponse);
        }

        @DeleteMapping("/api/v1/admin/form/{id}/delete/category")
        public ResponseEntity<CommonResponse<?>> reservationFormDeleteCategory(@PathVariable Long id){

            reservationService.formDeleteCategory(id);

            CommonResponse<?> commonResponse = CommonResponse
                    .builder()
                    .codeEnum(CodeEnum.SUCCESS)
                    .data(true)
                    .build();

            return ResponseEntity
                    .status(commonResponse.getStatus())
                    .body(commonResponse);
        }
        @PutMapping("/api/v1/admin/form/{id}/activation")
        public ResponseEntity<CommonResponse<?>> reservationFormActivation(@PathVariable Long id){
            reservationService.formDataActivation(id);

            CommonResponse<?> commonResponse = CommonResponse
                    .builder()
                    .codeEnum(CodeEnum.SUCCESS)
                    .data(true)
                    .build();

            return ResponseEntity
                    .status(commonResponse.getStatus())
                    .body(commonResponse);
        }

        @DeleteMapping("/api/v1/admin/form/{id}/delete")
        public ResponseEntity<CommonResponse<?>> reservationFormDelete(@PathVariable Long id){

            reservationService.formDataDelete(id);

            CommonResponse<?> commonResponse = CommonResponse
                    .builder()
                    .codeEnum(CodeEnum.SUCCESS)
                    .data(true)
                    .build();

            return ResponseEntity
                    .status(commonResponse.getStatus())
                    .body(commonResponse);
        }



}
