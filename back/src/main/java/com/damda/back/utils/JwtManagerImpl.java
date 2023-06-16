package com.damda.back.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.damda.back.data.common.MemberRole;
import com.damda.back.data.common.MemberStatus;
import com.damda.back.domain.Member;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtManagerImpl implements JwtManager{
    @Value("${token.secret.key}")
    private String KEY;
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final String UNKNOWN = "조회되지않음";
    private static final String SUBJECT = "JWT";
    private static final int EXP = 1000 * 60 * 60 * 24;

    public JwtManagerImpl(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();

    }
    public String jwtToken(String name,String gender,String phoneNumber,String profileImage){
        if(phoneNumber.equals(UNKNOWN) || name.equals(UNKNOWN)) throw new CommonException(ErrorCode.KAKAO_REQUIRED_VALUES_IS_EMPTY);

        phoneNumber = "0"+phoneNumber.substring(4);

        Optional<Member> memberOptional = memberRepository.findByPhoneNumber(phoneNumber);


        if(memberOptional.isEmpty()){

            Member member = Member.builder()
                    .role(MemberRole.USER)
                    .username(name)
                    .profileImage(profileImage)
                    .password(passwordEncoder.encode("0000"))
                    .status(MemberStatus.ACTIVATION)
                    .gender(gender)
                    .phoneNumber(phoneNumber)
                    .build();

            Member memberPE = memberRepository.save(member);

            String jwt = JWT.create()
                    .withSubject(SUBJECT)
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXP))
                    .withClaim("id", memberPE.getId())
                    .withClaim("role",memberPE.getRole().name())
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .sign(Algorithm.HMAC512(KEY));

            return jwt;
        }

        Member memberPE = memberOptional.get();

        String jwt = JWT.create()
                .withSubject(SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP))
                .withClaim("id", memberPE.getId())
                .withClaim("role",memberPE.getRole().name())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC512(KEY));

        return jwt;
    }

    @Override
    public String jwtTokenAdmin(Integer id,String role) {


        String jwt = JWT.create()
                .withSubject(SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP))
                .withClaim("id", id)
                .withClaim("role","ADMIN")
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC512(KEY));
        System.out.println(jwt);


        return jwt;
    }

    public DecodedJWT verify(String jwt) throws SignatureVerificationException, TokenExpiredException {

        DecodedJWT decodedJWT = JWT
                .require(Algorithm.HMAC512(KEY))
                .build()
                .verify(jwt);

        return decodedJWT;
    }
}
