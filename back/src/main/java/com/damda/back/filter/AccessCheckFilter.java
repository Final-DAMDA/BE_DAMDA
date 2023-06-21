package com.damda.back.filter;


import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.damda.back.exception.TokenException;
import com.damda.back.utils.JwtManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
public class AccessCheckFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;

    private static final Set<String> ALLOWED_PATHS = new HashSet<>(Arrays.asList(
            "/api/v1/member/code",
            "/api/v1/test/login",
            "/h2-console",
            "/api/v1/admin/login"
    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if(isAllowedPath(path)){
            log.info("TOKEN PASS");
            filterChain.doFilter(request,response);
            return;
        }

        log.info("TOKEN 검증 시작");

        log.info("REQUEST INFO URI : {} ",request.getRequestURI());

        String ipAddress = request.getRemoteAddr();
        log.info("Client IP Address: {}",ipAddress);

        String hostName = request.getRemoteHost();
        log.info("요청이 온 서버의 호스트명 확인 {}",hostName);

        int portNumber = request.getRemotePort();
        log.info("요청의 포트 번호 확인: {}",portNumber);

        String requestURL = request.getRequestURL().toString();
        log.info("Request URL: {}",requestURL);

        String method = request.getMethod();
        log.info("HTTP Method: "+method);

        String userAgent = request.getHeader("User-Agent");
        log.info("User-Agent: {}",userAgent);
      try{
          Map<String,Claim> claimMap = validateAccessToken(request);

          if(claimMap == null)  {
              new TokenException(TokenException.TOKEN_ERROR.UNACCEPT).sendResponseError(response);
              return;
          }

          String id = claimMap.get("id").toString();
          String role = claimMap.get("role").asString();

          log.info("로그인한 번호 {}",id);
          log.info("로그인한 권한 {}",role);

          request.setAttribute("id",id);
          request.setAttribute("role",role);

          filterChain.doFilter(request,response);
        }catch (TokenException tokenExpiredException){
            tokenExpiredException.sendResponseError(response);
        }
    }

    public Map<String, Claim> validateAccessToken(HttpServletRequest request) throws TokenException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    String accessToken = cookie.getValue();
                    try{
                        DecodedJWT decodedJWT = jwtManager.verify(accessToken);
                        Map<String, Claim> claimMap = decodedJWT.getClaims();
                        log.info("TOKEN 통과");
                        return claimMap;
                    }catch (TokenExpiredException tokenExpiredException){//시간만료
                        log.warn("TOKEN EXPIRED - Refresh Token request Is required 403 ");
                        throw  new TokenException(TokenException.TOKEN_ERROR.EXPIRED);
                    }catch (SignatureVerificationException signatureVerificationException){ //토큰의 서명 검증이 실패한 경우
                        log.warn("TOKEN BADSIGN - Token has changed its signature changed ");
                        throw  new TokenException(TokenException.TOKEN_ERROR.BADSIGN);
                    }catch (InvalidClaimException invalidClaimException){ //토큰의 클레임 검증 실패 클레임이 없거나 예상값과 다를 때
                        log.warn("TOKEN INVALID - Invalid token.");
                        throw  new TokenException(TokenException.TOKEN_ERROR.MALFORM);
                    }catch (JWTDecodeException jwtDecodeException){ //토큰손상되면 또는 올바른 형식이 아니.면
                        log.warn("TOKEN MALFORM - Invalid token ");
                        throw  new TokenException(TokenException.TOKEN_ERROR.MALFORM);
                    }
                }
            }
        }else{
            throw new TokenException(TokenException.TOKEN_ERROR.UNACCEPT);
        }

        return null;
    }

    private boolean isAllowedPath(String path) {
        for (String allowedPath : ALLOWED_PATHS) {
            if (path.startsWith(allowedPath)) {
                return true;
            }
        }
        return false;
    }
}
