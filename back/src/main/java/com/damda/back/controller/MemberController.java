package com.damda.back.controller;


import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @GetMapping("/api/v1/member/code")
    public ResponseEntity<CommonResponse<?>> reservationFormDelete(@RequestParam(required = false) String code){



        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(code != null ? "코드 들어옴" : "코드 안 들어옴")
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }
}
