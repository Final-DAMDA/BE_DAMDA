package com.damda.back;

import com.damda.back.data.common.MemberRole;
import com.damda.back.domain.Member;
import com.damda.back.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackApplication  {



	@Autowired
	private MemberRepository memberRepository;
	public static void main(String[] args) {

		SpringApplication.run(BackApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		// 더미 데이터 생성
//		createDummyData();
//	}
//	private void createDummyData() {
//		Member member = Member.builder()
//							.gender("여")
//							.address("경기도 시흥시")
//							.profileImage("image.png")
//							.role(MemberRole.USER)
//							.password("aaa")
//							.username("hi")
//							.phoneNumber("010-1234-4321")
//							.build();
//		memberRepository.save(member);
//
//	}
}
