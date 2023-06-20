package com.damda.back.controller;


import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.response.AccessTokenResponse;
import com.damda.back.data.response.MemberResponseDTO;
import com.damda.back.data.response.TokenWithImageDTO;
import com.damda.back.service.KaKaoService;
import com.damda.back.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final KaKaoService kaKaoService;

    private final MemberService memberService;

    // http://localhost:8080/api/v1/member/code
    @GetMapping("/api/v1/member/code")
    public void reservationFormDelete(
            @RequestParam(required = false) String code,
            HttpServletResponse response) throws IOException {

        TokenWithImageDTO tokenWithImageDTO = kaKaoService.loginProcessing(code);

        ResponseCookie cookie = ResponseCookie.from("access_token", tokenWithImageDTO.getToken())
                .maxAge(24 * 60 * 60)
                .path("/")
                .secure(true)// https 환경에서만 쿠키가 발동합니다.
                .sameSite("None")// 동일 사이트과 크로스 사이트에 모두 쿠키 전송이 가능합니다
                // HTTPS 환경에서 None으로 변경예정
                //       .httpOnly(true)// 브라우저에서 쿠키에 접근할 수 없도록 제한
                .build();

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(tokenWithImageDTO.getProfileImage())
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
        response.getWriter().println(new ObjectMapper().writeValueAsString(commonResponse));
    }


    @GetMapping("/api/v1/member/token")
    public ResponseEntity<CommonResponse<?>> reservationFormDelete(@RequestBody AccessTokenResponse response) {

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
    public ResponseEntity<CommonResponse<?>> authCheck(HttpServletRequest request) {

        Integer id = Integer.parseInt(request.getAttribute("id").toString());

        MemberResponseDTO dto = memberService.detail(id);

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(dto)
                .build();
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/api/v1/verify/code")
    public ResponseEntity<CommonResponse<?>> verifyCode(@RequestParam String code) {

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(memberService.existCode(code))
                .build();

        return ResponseEntity.ok(commonResponse);
    }


    @GetMapping("/api/v1/member/list")
    public ResponseEntity<CommonResponse<?>> memberList(){

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(memberService.listMember())
                .build();

        return ResponseEntity.ok(commonResponse);
    }

}
