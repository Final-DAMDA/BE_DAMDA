package com.damda.back.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.damda.back.data.common.MemberRole;
import com.damda.back.data.common.MemberStatus;
import com.damda.back.domain.Member;
import com.damda.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtManagerImpl implements JwtManager{
    @Value("${token.secret.key}")
    private String KEY;
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private static final String SUBJECT = "JWT";
    private static final int EXP = 1000 * 60 * 60 * 24;

    public String jwtToken(String name,String gender,String phoneNumber){
        phoneNumber = "0"+phoneNumber.substring(4);

        Optional<Member> memberOptional = memberRepository.findByPhoneNumber(phoneNumber);


        if(memberOptional.isEmpty()){

            Member member = Member.builder()
                    .role(MemberRole.USER)
                    .username(name)
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
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .sign(Algorithm.HMAC512(KEY));

            return jwt;
        }

        Member memberPE = memberOptional.get();

        String jwt = JWT.create()
                .withSubject(SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP))
                .withClaim("id", memberPE.getId())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC512(KEY));

        return jwt;
    }
}
