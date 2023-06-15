package com.damda.back.controller;


import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.request.AdminLoginRequestDTO;
import com.damda.back.service.AdminService;
import com.damda.back.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class AdminController {


    private final AdminService adminService;

    @PostMapping("/api/v1/admin/login")
    public void adminLogin(@RequestBody AdminLoginRequestDTO dto, HttpServletResponse response) throws IOException {

        String token = adminService.loginProcessing(dto);
        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .maxAge(24 * 60 * 60)
                .path("/")
                .secure(true)// https 환경에서만 쿠키가 발동합니다.
                .sameSite("None")// 동일 사이트과 크로스 사이트에 모두 쿠키 전송이 가능합니다
                //HTTPS 환경에서 None으로 변경예정
               // .httpOnly(true)// 브라우저에서 쿠키에 접근할 수 없도록 제한
                .build();


        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(true)
                .build();


        response.setHeader("Set-Cookie",cookie.toString());
        response.setContentType("application/json");
        response.getWriter().println(new ObjectMapper().writeValueAsString(commonResponse));
    }



}
