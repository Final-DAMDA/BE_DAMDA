package com.damda.back.controller;


import com.damda.back.config.annotation.UserBlocking;
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
import org.springframework.web.bind.annotation.GetMapping;
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
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
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


    @UserBlocking
    @GetMapping("/api/v1/admin/auth")
    public ResponseEntity<CommonResponse<?>> authAdmin(){
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
