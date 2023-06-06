package com.damda.back.controller;

import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.request.ReservationFormRequestDTO;
import com.damda.back.data.request.SubmitRequestDTO;
import com.damda.back.service.Impl.SubmitServiceImpl;
import com.damda.back.service.SubmitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class FormController {

    private final SubmitService submitService;


    @GetMapping("/api/v1/admin/submit/list")
    public ResponseEntity<CommonResponse<?>> adminSubmitListView(
            @RequestParam(required = false,defaultValue = "0") Integer page,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate){


        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(submitService.submitTotalResponse(page,startDate,endDate))
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }


    @PostMapping("/api/v1/user/form/submit")
    public ResponseEntity<CommonResponse<?>> reservationFormSave(
            @RequestBody SubmitRequestDTO dto){

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
