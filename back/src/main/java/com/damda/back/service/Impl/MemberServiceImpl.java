package com.damda.back.service.Impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.damda.back.data.response.AccessTokenResponse;
import com.damda.back.data.response.KaKaoAccessDTO;
import com.damda.back.data.response.KakaoAccountDTO;
import com.damda.back.domain.Member;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.MemberRepository;
import com.damda.back.service.MemberService;
import com.damda.back.utils.JwtManager;
import com.damda.back.utils.JwtManagerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;


@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    @Value("${kakao.client.key}")
    private String clientId;
    @Value("${kakao.redirect.uri}")
    private String redirectUri;
    private final JwtManager jwtManager;


    public MemberServiceImpl(JwtManager jwtManager){
        this.jwtManager = jwtManager;
    }

    @Override
    public String loginProcessing(String code){
       AccessTokenResponse accessTokenResponse =  tokenResponse(code);
       KaKaoAccessDTO kaKaoAccessDTO = infoResponse(accessTokenResponse.getAccessToken());
       KakaoAccountDTO accountDTO = kaKaoAccessDTO.getKakaoAccount();

        return jwtManager.jwtToken(accountDTO.getName(),accountDTO.getGender(),accountDTO.getPhoneNumber());
    }

    public AccessTokenResponse tokenResponse(String code){
        String KAKAO_URL = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", clientId);
        parameters.add("redirect_uri", redirectUri);
        parameters.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AccessTokenResponse> responseEntity = restTemplate.exchange(
                KAKAO_URL,
                HttpMethod.POST,
                requestEntity,
                AccessTokenResponse.class
        );
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            AccessTokenResponse responseBody = responseEntity.getBody();
            return responseBody;
        } else {
            throw new CommonException(ErrorCode.KAKAO_TOKEN_EXPIRE);
        }
    }

    public KaKaoAccessDTO infoResponse(String accessToken){

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<KaKaoAccessDTO> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), KaKaoAccessDTO.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            KaKaoAccessDTO jsonResponse = response.getBody();
            log.info("토큰을 통해 받아온 사용자 정보 {}",jsonResponse);
            return jsonResponse;
        } else {
            throw new CommonException(ErrorCode.KAKAO_TOKEN_EXPIRE);
        }
    }





}
