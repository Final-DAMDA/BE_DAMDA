package com.damda.back;

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

import java.util.List;

@SpringBootApplication
public class BackApplication {

	@Profile("prod")
	@Bean
	CommandLineRunner initData(
			QuestionRepository questionRepository
	) {
		return args -> {
			questionRepository.save(
					Question.builder()
							.questionIdentify(QuestionIdentify.TITILE)
							.questionTitle("옷장 정리 서비스 예약 - 대제목")
							.order(0)
							.status(QuestionStatus.ACTIVATION)
							.questionType(QuestionType.TITLE)
							.required(false)
							.build()
			);
			Question questionTest = Question.builder()
					.questionIdentify(QuestionIdentify.AFEWSERVINGS)
					.questionTitle("정리가 필요한 옷들이 몇 인 분량인가요?")
					.order(1)
					.status(QuestionStatus.ACTIVATION)
					.questionType(QuestionType.SELECT)
					.required(true)
					.build();

			questionTest.addCategory(Category.builder()
					.questionCategory("1 인분")
					.categoryPrice(40000)
					.build());

			questionTest.addCategory(Category.builder()
					.questionCategory("2 인분")
					.categoryPrice(80000)
					.build());
			questionTest.addCategory(Category.builder()
					.questionCategory("3 인분")
					.categoryPrice(120000)
					.build());
			questionRepository.save(
					questionTest
			);

			Question questionTest2 = Question.builder()
					.questionIdentify(QuestionIdentify.SERVICEDURATION)
					.questionTitle("필요한 서비스 시간을 선택해주세요")
					.order(2)
					.questionType(QuestionType.RADIO)
					.status(QuestionStatus.ACTIVATION)
					.required(true)
					.build();

			questionTest2.addCategory(Category.builder()
					.questionCategory("3시간")
					.categoryPrice(20000)
					.build());

			questionTest2.addCategory(Category.builder()
					.questionCategory("4시간")
					.categoryPrice(30000)
					.build());

			questionTest2.addCategory(Category.builder()
					.questionCategory("5시간")
					.categoryPrice(50000)
					.build());


			questionRepository.save(
					questionTest2
			);
			questionRepository.save(
					Question.builder()
							.questionIdentify(QuestionIdentify.ADDRESS)
							.questionTitle("서비스를 받으실 주소를 알려주세요")
							.questionType(QuestionType.ADDRESS)
							.order(3)
							.status(QuestionStatus.ACTIVATION)
							.required(true)
							.build()
			);
			questionRepository.save(
					Question.builder()
							.questionIdentify(QuestionIdentify.SERVICEDATE)
							.questionTitle("서비스를 희망하시는 날짜와 시작시간을 알려주세요")
							.order(4)
							.questionType(QuestionType.DATE)
							.status(QuestionStatus.ACTIVATION)
							.required(true)
							.build()
			);
			questionRepository.save(
					Question.builder()
							.questionIdentify(QuestionIdentify.PARKINGAVAILABLE)
							.questionTitle("서비스 장소에 주차가 가능한가요?")
							.questionType(QuestionType.STRING)
							.order(5)
							.status(QuestionStatus.ACTIVATION)
							.required(true)
							.build()
			);
			questionRepository.save(
					Question.builder()
							.questionIdentify(QuestionIdentify.APPLICANTNAME)
							.questionTitle("신청인의 이름을 알려주세요")
							.order(6)
							.questionType(QuestionType.STRING)
							.status(QuestionStatus.ACTIVATION)
							.required(true)
							.build()
			);
			questionRepository.save(
					Question.builder()
							.questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
							.questionTitle("신청인의 연락처를 알려주세요")
							.order(7)
							.questionType(QuestionType.STRING)
							.status(QuestionStatus.ACTIVATION)
							.required(true)
							.build()
			);

			questionRepository.save(
					Question.builder()
							.questionIdentify(QuestionIdentify.OPTIONAL)
							.questionTitle("열다 매니저가 출입하기 위해 피룡한 정보가 있다면 알려주세요")
							.order(8)
							.questionType(QuestionType.STRING)
							.status(QuestionStatus.ACTIVATION)
							.required(false)
							.build()
			);

			questionRepository.save(
					Question.builder()
							.questionIdentify(QuestionIdentify.RECOMENDEDCODE)
							.questionTitle("추천인 코드 또는 프로모션 코드가 있다면 입력해주세요")
							.order(9)
							.questionType(QuestionType.STRING)
							.status(QuestionStatus.ACTIVATION)
							.required(false)
							.build()
			);
			questionRepository.save(
					Question.builder()
							.questionIdentify(QuestionIdentify.OPTIONAL)
							.questionTitle("매니저가 알아야 할 정보가 있다면 입력해주세요")
							.order(10)
							.questionType(QuestionType.STRING)
							.status(QuestionStatus.ACTIVATION)
							.required(false)
							.build()
			);
			questionRepository.save(
					Question.builder()
							.questionIdentify(QuestionIdentify.OPTIONAL)
							.questionTitle("요청 또는 문의사항이 있다면 자유롭게 입력해주세요")
							.order(11)
							.questionType(QuestionType.STRING)
							.status(QuestionStatus.ACTIVATION)
							.required(false)
							.build()
			);
			questionRepository.save(
					Question.builder()
							.questionIdentify(QuestionIdentify.LEARNEDROUTE)
							.questionTitle("열다 서비스를 알게 된 경로를 알려주세요")
							.order(12)
							.questionType(QuestionType.STRING)
							.status(QuestionStatus.ACTIVATION)
							.required(true)
							.build()
			);

			questionRepository.save(
					Question.builder() //선택시 응답 true 같은 값보내면됨
							.questionIdentify(QuestionIdentify.REQUIREDGUIDELINES)
							.questionTitle("필수 안내사항")
							.order(12)
							.questionType(QuestionType.SELECT)
							.status(QuestionStatus.ACTIVATION)
							.required(true)
							.build()
			);

		};
	}


	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}

}
