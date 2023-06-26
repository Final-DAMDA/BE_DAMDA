package com.damda.back.service.Impl;

import com.damda.back.data.response.*;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.utils.JwtManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;


@ExtendWith(MockitoExtension.class)
class KaKaoMemberServiceImplTest {

    @InjectMocks
    private KaKaoMemberServiceImpl kaKaoMemberService;

    @Mock
    private JwtManager jwtManager;


    private MockRestServiceServer mockServer;



    @Test
    @DisplayName("Token Response Test")
    void token_response_test() throws URISyntaxException {

        String code = "code";

        // 예상되는 예외 객체
        CommonException expectedException = new CommonException(ErrorCode.KAKAO_TOKEN_EXPIRE);

        // when
        Throwable thrownException = Assertions.catchThrowable(() -> kaKaoMemberService.tokenResponse(code));

        // then
        Assertions.assertThat(thrownException).isInstanceOf(CommonException.class)
                .satisfies(ex -> {
                    Assertions.assertThat(ex.getMessage()).isEqualTo(expectedException.getMessage());
                });

    }

}