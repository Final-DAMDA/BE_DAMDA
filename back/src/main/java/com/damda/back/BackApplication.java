package com.damda.back;

import com.damda.back.config.InitQuery;
import com.damda.back.data.common.MemberRole;
import com.damda.back.data.common.MemberStatus;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.QuestionType;
import com.damda.back.domain.Category;
import com.damda.back.domain.Member;
import com.damda.back.domain.Question;
import com.damda.back.domain.QuestionStatus;
import com.damda.back.repository.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootApplication
public class BackApplication {

	 @Profile("dev")
	 @Bean
	 CommandLineRunner initData(
	 		QuestionRepository questionRepository,
			MemberRepository memberRepository,
			PasswordEncoder passwordEncoder,
			ManagerRepository managerRepository,
			EntityManager entityManager,
			AreaRepository areaRepository,
			AreaManagerRepository areaManagerRepository,
			InitQuery initQuery
	 ) {
	 	return args -> {insertQuery(questionRepository,
				memberRepository,
				passwordEncoder,
				managerRepository,
				entityManager,
				areaRepository,
				areaManagerRepository);

			 	initQuery.initData();
		 };
	 }

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}



	public void insertQuery(QuestionRepository questionRepository,
							MemberRepository memberRepository,
							PasswordEncoder passwordEncoder,
							ManagerRepository managerRepository,
							EntityManager em,
							AreaRepository areaRepository,
							AreaManagerRepository areaManagerRepository){


		 memberRepository.save(Member.builder()
						 .username("admin")
						 .password(passwordEncoder.encode("1234"))
						 .phoneNumber("01012341234")
						 .status(MemberStatus.ACTIVATION)
						 .role(MemberRole.ADMIN)
						 .profileImage("404.jpg")
				 .build());


		 //--------------Question Insert


		questionRepository.save(
				Question.builder()
						.page(1)
						.order(0)
						.placeHolder("없음")
						.questionIdentify(QuestionIdentify.TITLE)
						.questionType(QuestionType.TITLE)
						.required(false)
						.status(QuestionStatus.ACTIVATION)
						.questionTitle("담다컴퍼니 입니다.")
						.build()
		);
		Question question1 = questionRepository.save(
				Question.builder()
						.page(1)
						.order(1)
						.placeHolder("없음")
						.questionIdentify(QuestionIdentify.AFEWSERVINGS)
						.questionType(QuestionType.SELECT)
						.required(true)
						.questionTitle("정리가 필요한 옷들이 몇 인 분량인가요?")
						.status(QuestionStatus.ACTIVATION)
						.build()
		);

		List<Category> categoryList = List.of(
				Category.builder().questionCategory("1인").build(),
				Category.builder().questionCategory("2인").build(),
				Category.builder().questionCategory("3인").build(),
				Category.builder().questionCategory("4인").build());

		categoryList.forEach(question1::addCategory);

		questionRepository.save(question1);


		Question question2 = questionRepository.save(
				Question.builder()
						.page(1)
						.order(2)
						.placeHolder("없음")
						.questionIdentify(QuestionIdentify.SERVICEDURATION)
						.questionType(QuestionType.RADIO)
						.status(QuestionStatus.ACTIVATION)
						.required(true)
						.questionTitle("필요한 서비스 시간을 선택해주세요")
						.build()
		);

		List<Category> categories = List.of(
				Category.builder().questionCategory("4시간").build(),
				Category.builder().questionCategory("5시간").build(),
				Category.builder().questionCategory("6시간").build());

		categories.forEach(question2::addCategory);

		questionRepository.save(question2);


//
//		Question question3 = questionRepository.save(
//				Question.builder()
//						.page(1)
//						.order(3)
//						.placeHolder("없음")
//						.questionIdentify(QuestionIdentify.SALEAGENT)
//						.questionType(QuestionType.SELECT)
//						.status(QuestionStatus.ACTIVATION)
//						.required(true)
//						.questionTitle("판매 대행 옵션을 선택해주세요.")
//						.build()
//		);
//
//		List<Category> categories1 = List.of(
//				Category.builder().questionCategory("O").build(),
//				Category.builder().questionCategory("X").build()
//		);
//
//		categories1.forEach(question3::addCategory);


		questionRepository.save(
				Question.builder()
						.page(2)
						.order(1)
						.placeHolder("서울 마포구 백범로31 길 21 서울창업허브")
						.questionIdentify(QuestionIdentify.ADDRESS)
						.questionType(QuestionType.STRING)
						.status(QuestionStatus.ACTIVATION)
						.required(true)
						.questionTitle("서비스 받으실 주소를 알려주세요")
						.build()
		);
		questionRepository.save(
				Question.builder()
						.page(2)
						.order(2)
						.placeHolder("없음")
						.questionIdentify(QuestionIdentify.SERVICEDATE)
						.questionType(QuestionType.DATE)
						.status(QuestionStatus.ACTIVATION)
						.required(true)
						.questionTitle("서비스를 희망하시는 날짜와 시작 시간을 알려주세요.")
						.build()
		);
		questionRepository.save(
				Question.builder()
						.page(2)
						.order(3)
						.placeHolder("차량 두 대까지 3시간 주차 가능")
						.questionIdentify(QuestionIdentify.PARKINGAVAILABLE)
						.questionType(QuestionType.STRING)
						.status(QuestionStatus.ACTIVATION)
						.required(true)
						.questionTitle("서비스 장소에 주차가 가능한가요?")
						.build()
		);
		questionRepository.save(
				Question.builder()
						.page(2)
						.order(4)
						.placeHolder("홍길동")
						.questionIdentify(QuestionIdentify.APPLICANTNAME)
						.questionType(QuestionType.STRING)
						.status(QuestionStatus.ACTIVATION)
						.required(true)
						.questionTitle("신청인의 연락처를 알려주세요")
						.build()
		);
		questionRepository.save(
				Question.builder()
						.page(2)
						.order(5)
						.placeHolder("01012341234")
						.questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
						.questionType(QuestionType.STRING)
						.status(QuestionStatus.ACTIVATION)
						.required(true)
						.questionTitle("신청인의 연락처를 알려주세요.")
						.build()
		);
		questionRepository.save(
				Question.builder()
						.page(2)
						.order(6)
						.placeHolder("공용 현관 비밀번호 0000")
						.questionIdentify(QuestionIdentify.RESERVATIONENTER)
						.questionType(QuestionType.STRING)
						.status(QuestionStatus.ACTIVATION)
						.required(false)
						.questionTitle("열다 매니저가 출입하기 위해 필요한 정보가 있다면 알려주세요.")
						.build()
		);
		questionRepository.save(
				Question.builder()
						.page(2)
						.order(7)
						.placeHolder("영문과 숫자로 구성되어 있는 코드 입력")
						.questionIdentify(QuestionIdentify.SALECODE)
						.questionType(QuestionType.STRING)
						.status(QuestionStatus.ACTIVATION)
						.questionTitle(" 추천인 코드 또는 프로모션 코드가 있다면 입력해주세요.")
						.required(false)
						.build()
		);

		questionRepository.save(
				Question.builder()
						.page(2)
						.order(8)
						.placeHolder("고양이랑 강아지가 있어요.")
						.questionIdentify(QuestionIdentify.RESERVATIONNOTE)
						.questionType(QuestionType.STRING)
						.status(QuestionStatus.ACTIVATION)
						.questionTitle(" 매니저가 알아야 할 정보가 있다면 알려주세요.")
						.required(false)
						.build()
		);
		questionRepository.save(
				Question.builder()
						.page(2)
						.order(9)
						.placeHolder("바지걸이가 많이 필요할 것 같아요")
						.questionIdentify(QuestionIdentify.RESERVATIONREQUEST)
						.questionType(QuestionType.STRING)
						.status(QuestionStatus.ACTIVATION)
						.questionTitle(" 매니저가 알아야 할 정보가 있다면 알려주세요.")
						.required(false)
						.build()
		);
		Question question4 = questionRepository.save(
				Question.builder()
						.page(2)
						.order(10)
						.placeHolder("없음")
						.questionIdentify(QuestionIdentify.LEARNEDROUTE)
						.questionType(QuestionType.STRING)
						.status(QuestionStatus.ACTIVATION)
						.questionTitle("열다 서비스를 알게 된 경로를 알려주세요.")
						.required(true)
						.build()
		);

		List<Category> categories2 = List.of(
				Category.builder().questionCategory("인터넷 검색").build(),
				Category.builder().questionCategory("프로모션").build(),
				Category.builder().questionCategory("SNS 유료광고").build(),
				Category.builder().questionCategory("열다 인스타그램 계정").build(),
				Category.builder().questionCategory("지인추천").build(),
				Category.builder().questionCategory("기타").build()
		);




		categories2.forEach(question4::addCategory);
		questionRepository.save(question4);



	}
}
