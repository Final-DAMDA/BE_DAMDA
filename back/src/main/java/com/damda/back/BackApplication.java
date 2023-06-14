package com.damda.back;

import com.damda.back.data.common.MemberRole;
import com.damda.back.data.common.MemberStatus;
import com.damda.back.domain.Member;
import com.damda.back.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.QuestionType;
import com.damda.back.domain.Category;
import com.damda.back.domain.Question;
import com.damda.back.domain.QuestionStatus;
import com.damda.back.repository.QuestionRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class BackApplication {

	 @Profile("dev")
	 @Bean
	 CommandLineRunner initData(
			 MemberRepository memberRepository,
			 PasswordEncoder passwordEncoder) {
	 	return args -> {
			memberRepository.save(Member.builder()
							.username("admin")
							.password(passwordEncoder.encode("1234"))
							.status(MemberStatus.ACTIVATION)
							.role(MemberRole.ADMIN)
							.phoneNumber("01012341234")
							.profileImage("admin.jpg")
							.address("어드민")
					.build());
	 	};

	 }

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}
	
}
