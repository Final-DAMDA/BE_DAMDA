package com.damda.back.controller;


import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.response.AccessTokenResponse;
import com.damda.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/api/v1/member/code")
    public ResponseEntity<CommonResponse<?>> reservationFormDelete(@RequestParam(required = false) String code){

        String token = memberService.loginProcessing(code);

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(token)
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    @GetMapping("/api/v1/member/token")
    public ResponseEntity<CommonResponse<?>> reservationFormDelete(@RequestBody AccessTokenResponse response){

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(" 토큰 받음")
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

}
