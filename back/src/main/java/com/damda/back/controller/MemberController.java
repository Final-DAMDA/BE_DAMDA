package com.damda.back.controller;


import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.response.AccessTokenResponse;
import com.damda.back.data.response.MemberResponseDTO;
import com.damda.back.service.KaKaoService;
import com.damda.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final KaKaoService kaKaoService;

    private final MemberService memberService;


    @GetMapping("/api/v1/member/code")
    public ResponseEntity<CommonResponse<?>> reservationFormDelete(
            @RequestParam(required = false) String code,
            HttpServletResponse response){

        String token = kaKaoService.loginProcessing(code);

        Cookie cookie = new Cookie("access_token",token);
        cookie.setMaxAge(86400);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(true)
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .headers(headers)
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

    @GetMapping("/api/v1/auth/me")
    public ResponseEntity<CommonResponse<?>> authCheck(HttpServletRequest request){

        Integer id =  Integer.parseInt(request.getAttribute("id").toString());

        MemberResponseDTO dto = memberService.detail(id);


        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(dto)
                .build();
        return ResponseEntity.ok(commonResponse);
    }

}
