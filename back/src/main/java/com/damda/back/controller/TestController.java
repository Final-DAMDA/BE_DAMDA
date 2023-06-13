package com.damda.back.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.utils.JwtManager;
import com.damda.back.utils.SolapiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequiredArgsConstructor
public class TestController {

    @Value("${token.secret.key}")
    private String KEY;

    private static final int EXP = 1000 * 60 * 60 * 24;

    @Autowired
    private SolapiUtils solapiUtils;

    @GetMapping("/api/v1/test/login")
    public ResponseEntity<CommonResponse<?>> token(HttpServletResponse response){

        String token = JWT.create()
                .withSubject("JWT")
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP))
                .withClaim("id", 1)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC512(KEY));

        Cookie cookie = new Cookie("access_token",token);
        cookie.setMaxAge(86400);
        cookie.setPath("/");
      //  cookie.setHttpOnly(true);
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

    @GetMapping("/api/v1/logout")
    public void logout(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {

                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);

                }
            }
        }

    }



}
