package com.damda.back;

import com.damda.back.data.request.ManagerRegionUpdateRequestDTO;
import com.damda.back.domain.area.Area;
import com.damda.back.domain.area.DistrictEnum;
import com.damda.back.domain.area.QArea;
import com.damda.back.domain.manager.*;
import com.damda.back.repository.*;
import com.damda.back.service.SubmitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.damda.back.config.SecurityConfig;
import com.damda.back.data.common.*;
import com.damda.back.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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

	@Autowired
	private JPAQueryFactory queryFactory;


	@Test
	@Transactional
	@Commit
	void contextLoads() {
		//managerRepository.managerWithArea("하남시").forEach(System.out::println);

		HashMap<String,List<String>> map = new HashMap<>();

		map.get(0);


//		QManager manager = QManager.manager;
//		QAreaManager areaManager = QAreaManager.areaManager;
//		QArea area = QArea.area;
//
//		Manager manager1 = queryFactory.selectDistinct(manager)
//				.join(manager.areaManagers, areaManager).fetchJoin()
//				.join(areaManager.areaManagerKey.area,area).fetchJoin()
//				.where(manager.id.eq(1L)).fetchOne();
//
//		ManagerRegionUpdateRequestDTO dto = ManagerRegionUpdateRequestDTO.builder()
//				.activityCity(List.of("경기도"))
//				.activityDistrict(List.of("하남시"))
//				.build();
//
//
//		//코드로 짜보겠습니다
//
//		manager1.getAreaManagers().forEach(areaManager1 -> {	//현재 매니저가 가진 Area (하남시/ 안산시)
//			Area areaEntity = areaManager1.getAreaManagerKey().getArea();
//			String district = areaEntity.getDistrict();
//			//반대군요
//		});
//
//
//
//		dto.getActivityDistrict().forEach(s -> {
//				List<String> diList =manager1.getAreaManagers()
//						.stream()
//						.map(areaManager1 -> areaManager1.getAreaManagerKey().getArea().getDistrict()).toList();
//
//				diList.forEach(s1 -> {
//					if(dto.getActivityDistrict().contains(s1)){
//						//db에 있는 데이터가 입력으로 들어온 데이터에 포함될 경우 -> 그대로
//					}else{
//						//db에 있는 데이터가 입력으로 안 들어온 경우 -> delete //managerCount -1
//					}//
//				});
//		});
//
//
//		manager1.getAreaManagers().forEach(areaManagerPE -> {
//				Area area1 = areaManagerPE.getAreaManagerKey().getArea();
//				int 지워진= area1.getManagerCount() - 1;
//
//				if(dto.getActivityDistrict().contains(area1.getDistrict())){
//					// 혹시 수정할 때 사용자 입장에서 수정하려고 들어가면 기존에 자신의 활동지역들이 사용자한테 보여지나요?
//					//?? 그러면 수정은 입력받은데이터로 그냥 다 갈아끼는건가요 ??
//					// 만일 제가 안산시/하남시를 활동지역으로 가질경우 수정페이지에서 이 두 데이터를 보고
//					// 여기서 강동구를 추가지역으로 넣고 싶어서 강동구를 넣게 되면 API에 요청을
//					// 안산시/하남시/강동구로 들어오는건가요?
//					// 그러면 그냥 for문돌려서 안산시/하남시랑 겹치면 아무행위 안 하고 새로운 데이터면 save하면 되는거아닌가요?
//					// 활동지역을 삭제할 경우만 찾으면 답나올거 같습니다 오오
//
//					// 그니까 기존에 들어온 ㄱ바
//				}
//		});




	}
	@Test
	@DisplayName("테스트 데이터 넣기")
	void test_insert(){

		HashMap<String,String> map = new HashMap<>();


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

