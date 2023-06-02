package com.damda.back.service.Impl;

import com.damda.back.data.response.AccessTokenResponse;
import com.damda.back.data.response.KaKaoAccessDTO;
import com.damda.back.data.response.KakaoAccountDTO;
import com.damda.back.data.response.MemberResponseDTO;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.service.KaKaoService;
import com.damda.back.utils.JwtManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class KaKaoMemberServiceImpl implements KaKaoService {

    @Value("${kakao.client.key}")
    private String clientId;
    @Value("${kakao.redirect.uri}")
    private String redirectUri;
    private final JwtManager jwtManager;



    public KaKaoMemberServiceImpl(JwtManager jwtManager){
        this.jwtManager = jwtManager;
    }

    @Override
    public String loginProcessing(String code){
       AccessTokenResponse accessTokenResponse =  tokenResponse(code);
       KaKaoAccessDTO kaKaoAccessDTO = infoResponse(accessTokenResponse.getAccessToken());
       KakaoAccountDTO accountDTO = kaKaoAccessDTO.getKakaoAccount();

       String profileImage = kaKaoAccessDTO.getProperties().getThumbnailImage() != null ? kaKaoAccessDTO.getProperties().getThumbnailImage() : "404.jpg" ;
       return jwtManager.jwtToken(accountDTO.getName(),accountDTO.getGender(),accountDTO.getPhoneNumber(),profileImage);

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
