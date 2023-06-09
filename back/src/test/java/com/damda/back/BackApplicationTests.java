package com.damda.back;

import com.damda.back.domain.area.DistrictEnum;
import com.damda.back.domain.manager.CertificateStatusEnum;
import com.damda.back.domain.manager.Manager;
import com.damda.back.repository.*;
import com.damda.back.service.SubmitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.damda.back.config.SecurityConfig;
import com.damda.back.data.common.*;
import com.damda.back.domain.*;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

@SpringBootTest
@Import(SecurityConfig.class)
class BackApplicationTests {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private QuestionRepository questionRepository;


	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private ReservationFormRepository reservationFormRepository;

	@Autowired
	private SubmitService submitService;

	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private MatchRepository matchRepository;


	@Test
	void contextLoads() {
	   IntStream.rangeClosed(1,10)
               .forEach(value -> {
                   Manager manager = Manager.builder()
                           .certificateStatus(CertificateStatusEnum.FIRST_RATE_ON)
                           .memo("테스트 매니저..."+value)
                           .level(1)
                           .vehicle(true)
                           .fieldExperience("테스트 매니저..."+value)
						   .mainJobStatus(false)
						   .certificateStatusEtc("테스트 매니저..."+value)
						   .managerStatus("테스트 매니저..."+value)
                           .build();

               });




	}

	@Test
	@DisplayName("테스트 데이터 넣기")
	void test_insert(){
//		Question question_1 = Question.builder()
//				.questionType(QuestionType.SELECT)
//				.questionTitle("몇인분의 분량인가요?")
//				.questionIdentify(QuestionIdentify.AFEWSERVINGS)
//				.required(true)
//				.order(1)
//				.build();
//
//		Category category_1_1 = Category.builder()
//				.questionCategory("1인분")
//				.categoryPrice(20000)
//				.build();
//		Category category_1_2 = Category.builder()
//				.questionCategory("2인분")
//				.categoryPrice(40000)
//				.build();
//		Category category_1_3 = Category.builder()
//				.questionCategory("3인분")
//				.categoryPrice(50000)
//				.build();
//
//		question_1.addCategory(category_1_1);
//		question_1.addCategory(category_1_2);
//		question_1.addCategory(category_1_3);
//
//		Question question_2 = Question.builder()
//				.questionType(QuestionType.RADIO)
//				.questionTitle("원하는 서비스 사용시간을 골라주세요")
//				.questionIdentify(QuestionIdentify.SERVICEDURATION)
//				.required(true)
//				.order(2)
//				.build();
//
//		Category category_2_1 = Category.builder()
//				.questionCategory("1시간")
//				.categoryPrice(20000)
//				.build();
//		Category category_2_2 = Category.builder()
//				.questionCategory("2시간")
//				.categoryPrice(40000)
//				.build();
//		Category category_2_3 = Category.builder()
//				.questionCategory("3시간")
//				.categoryPrice(50000)
//				.build();
//
//		question_2.addCategory(category_2_1);
//		question_2.addCategory(category_2_2);
//		question_2.addCategory(category_2_3);
//
//
//		Question question_3 = Question.builder()
//				.questionType(QuestionType.STRING)
//				.questionTitle("주소를 입력해주세요")
//				.questionIdentify(QuestionIdentify.ADDRESS)
//				.required(true)
//				.order(3)
//				.build();
//
//		Question question_4 = Question.builder()
//				.questionType(QuestionType.DATE)
//				.questionTitle("서비스를 원하시는 날짜를 골라주세요")
//				.questionIdentify(QuestionIdentify.SERVICEDATE)
//				.required(true)
//				.order(4)
//				.build();
//
//		Question question_5 = Question.builder()
//				.questionType(QuestionType.STRING)
//				.questionTitle("주차 가능한가요?")
//				.questionIdentify(QuestionIdentify.PARKINGAVAILABLE)
//				.required(true)
//				.order(5)
//				.build();
//
//		Question question_6 = Question.builder()
//				.questionType(QuestionType.STRING)
//				.questionTitle("신청하시는 분의 성함을 알려주세요?")
//				.questionIdentify(QuestionIdentify.APPLICANTNAME)
//				.required(true)
//				.order(6)
//				.build();
//
//		Question question_7 = Question.builder()
//				.questionType(QuestionType.STRING)
//				.questionTitle("연락 받으실 핸드폰 번호를 입력해주세요")
//				.questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
//				.required(true)
//				.order(7)
//				.build();
//
//
//
//		Question question_8 = Question.builder()
//				.questionType(QuestionType.SELECT)
//				.questionTitle("알게된 경로를 선택해주세요")
//				.questionIdentify(QuestionIdentify.SERVICEDURATION)
//				.required(true)
//				.order(2)
//				.build();
//
//		Category category_3_1 = Category.builder()
//				.questionCategory("인스타그램")
//				.categoryPrice(0)
//				.build();
//		Category category_3_2 = Category.builder()
//				.questionCategory("페이스북")
//				.categoryPrice(0)
//				.build();
//		Category category_3_3 = Category.builder()
//				.questionCategory("유튜브")
//				.categoryPrice(0)
//				.build();
//
//		question_8.addCategory(category_3_1);
//		question_8.addCategory(category_3_2);
//		question_8.addCategory(category_3_3);
//
//
//		questionRepository.save(question_1);
//		questionRepository.save(question_2);
//		questionRepository.save(question_3);
//		questionRepository.save(question_4);
//		questionRepository.save(question_5);
//		questionRepository.save(question_6);
//		questionRepository.save(question_7);
//		questionRepository.save(question_8);
//

	}

	@Test
	@DisplayName("")
	void data_modify() {
	    // given
//	    questionRepository.findAll().forEach(question -> {
//			question.changeOrder(question.getQuestionNumber().intValue());
//			questionRepository.save(question);
//		});
	    // when

	    // then
	}

	@Test
	@DisplayName("")
	void count_query_check() {
	    // given
		String query =
				"SELECT r.status, COUNT(r) FROM ReservationSubmitForm r WHERE r.status IN :statuses GROUP BY r.status";

		ReservationSubmitForm submitForm = ReservationSubmitForm.builder()
				.status(ReservationStatus.MANAGER_MATCHING_COMPLETED)
				.build();

	    TypedQuery<Object[]> JPQLQuery = entityManager.createQuery(query,Object[].class);

		JPQLQuery.setParameter("statuses", Set.of(ReservationStatus.MANAGER_MATCHING_COMPLETED,ReservationStatus.SERVICE_COMPLETED,ReservationStatus.WAITING_FOR_MANAGER_REQUEST));

		List<Object[]> results = JPQLQuery.getResultList();
		Map<ReservationStatus, Long> countByStatusMap = new HashMap<>();

		for(Object[] result : results){
			ReservationStatus status = (ReservationStatus) result[0];
			Long count = (Long) result[1];
			countByStatusMap.put(status,count);
		}
	    // when
		countByStatusMap.forEach((reservationStatus, aLong) -> {
			System.out.println(reservationStatus+" : "+aLong);
		});
	    // then
	}

	@Test
	@DisplayName("")
	void statis_query_test() {
	    // given
		System.out.println(reservationFormRepository.statistical());;
	    // when

	    // then
	}
	
}

