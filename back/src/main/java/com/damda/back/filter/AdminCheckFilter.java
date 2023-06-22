package com.damda.back.filter;

import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Slf4j
public class AdminCheckFilter extends OncePerRequestFilter {

    private static final Set<String> ALLOWED_PATHS = new HashSet<>(Arrays.asList(
            "/api/v1/member/code",
            "/api/v1/test/login",
            "/h2-console",
            "/api/v1/admin/login",
            "/api/v1/user/form/submit",
            "/api/v1/auth/me",
            "/api/v1/user/review/list",
            "/api/v1/review/best"
    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if(isAllowedPath(path)){
            log.info("ADMIN NOT CHECKING");
            filterChain.doFilter(request,response);
            return;
        }


        log.info("ADMIN CHECKING");
        String role = request.getAttribute("role").toString();

        if(!role.equals("ADMIN")){
            log.warn("권한 없는 사용자가 어드민 리소스에 접근 시도함");
            CommonResponse<?> commonResponse = CommonResponse.builder()
                    .codeEnum(CodeEnum.NO_PERMISSIONS)
                    .data("권한없음")
                    .build();

            response.getWriter().println(new ObjectMapper().writeValueAsString(commonResponse));
        }else{
            log.info("어드민 접근");
            filterChain.doFilter(request,response);
        }
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
