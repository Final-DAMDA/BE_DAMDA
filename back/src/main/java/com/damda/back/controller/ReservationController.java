package com.damda.back.controller;


import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.request.ReservationFormRequestDTO;
import com.damda.back.service.Impl.ReservationServiceImpl;
import com.damda.back.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {


        private final ReservationService reservationService;

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


        @PostMapping("/api/v1/admin/form/save")
        public ResponseEntity<CommonResponse<?>> reservationFormSave(
                @RequestBody ReservationFormRequestDTO dto){

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
}
