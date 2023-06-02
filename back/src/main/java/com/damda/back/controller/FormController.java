package com.damda.back.controller;

import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.request.ReservationFormRequestDTO;
import com.damda.back.data.request.SubmitRequestDTO;
import com.damda.back.service.Impl.SubmitServiceImpl;
import com.damda.back.service.SubmitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FormController {

    private final SubmitService submitService;

    @PostMapping("/api/v1/user/form/submit")
    public ResponseEntity<CommonResponse<?>> reservationFormSave(
            @RequestBody SubmitRequestDTO dto){

        //TODO: memberID는 REQUEST에서 꺼내오는 방식으로 해야함
        submitService.saverFormSubmit(dto,1);

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
