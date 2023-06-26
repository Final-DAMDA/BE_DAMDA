package com.damda.back.config;


import com.damda.back.data.common.*;
import com.damda.back.domain.Match;
import com.damda.back.domain.Member;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.area.Area;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.CertificateStatusEnum;
import com.damda.back.domain.manager.Manager;
import com.damda.back.domain.manager.ManagerStatusEnum;
import com.damda.back.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitQuery {

	private final ReservationFormRepository formRepository;

	private final ManagerRepository managerRepository;

	private final AreaRepository areaRepository;

	private final AreaManagerRepository areaManagerRepository;

	private final MatchRepository matchRepository;

	private final MemberRepository memberRepository;

	@Transactional
	public void initData(){

		Member member = Member.builder()
				.username("홍길동1")
				.password("1234")
				.role(MemberRole.USER)
				.status(MemberStatus.ACTIVATION)
				.profileImage("404.jpg")
				.build();

		memberRepository.save(member);

		Member member2 = Member.builder()
				.username("홍길동2")
				.password("1234")
				.role(MemberRole.USER)
				.status(MemberStatus.ACTIVATION)
				.profileImage("404.jpg")
				.build();

		memberRepository.save(member2);

		Member member3 = Member.builder()
				.username("김재우")
				.password("1234")
				.role(MemberRole.USER)
				.status(MemberStatus.ACTIVATION)
				.profileImage("404.jpg")
				.build();

		memberRepository.save(member3);

		Member member4 = Member.builder()
				.username("고예림")
				.password("1234")
				.role(MemberRole.USER)
				.status(MemberStatus.ACTIVATION)
				.profileImage("404.jpg")
				.build();

		memberRepository.save(member4);

		Member member5 = Member.builder()
				.username("김형준")
				.password("1234")
				.role(MemberRole.USER)
				.status(MemberStatus.ACTIVATION)
				.profileImage("404.jpg")
				.build();

		memberRepository.save(member5);

		Manager manager = managerRepository.save(Manager.builder()
				.phoneNumber("01040783843")
				.managerName("김재우")
				.currManagerStatus(ManagerStatusEnum.ACTIVE)
				.certificateStatus(CertificateStatusEnum.ETC)
				.level(1)
				.member(member3)
				.build());

		Manager manager2 = managerRepository.save(Manager.builder()
				.phoneNumber("01039041094")
				.managerName("고예림")
				.currManagerStatus(ManagerStatusEnum.ACTIVE)
				.certificateStatus(CertificateStatusEnum.ETC)
				.level(1)
				.member(member4)
				.build());

		Manager manager3 = managerRepository.save(Manager.builder()
				.phoneNumber("01082535890")
				.managerName("김형준")
				.currManagerStatus(ManagerStatusEnum.ACTIVE)
				.certificateStatus(CertificateStatusEnum.ETC)
				.member(member5)
				.level(1)
				.build());


		Area area1 = Area.builder()
				.city("서울특별시")
				.district("강남구")
				.managerCount(3)
				.build();

		Area area2 = Area.builder()
				.city("서울특별시")
				.district("강북구")
				.managerCount(1)
				.build();

		Area area = Area.builder()
				.city("경기도")
				.district("하남시")
				.managerCount(0)
				.build();

		areaRepository.save(area1);
		areaRepository.save(area2);
		areaRepository.save(area);

		AreaManager.AreaManagerKey key = new AreaManager.AreaManagerKey(area1,manager);
		AreaManager.AreaManagerKey key2 = new AreaManager.AreaManagerKey(area1,manager2);
		AreaManager.AreaManagerKey key3 = new AreaManager.AreaManagerKey(area1,manager3);
		AreaManager.AreaManagerKey key4 = new AreaManager.AreaManagerKey(area2,manager2);


		AreaManager areaManager = AreaManager.builder()
				.areaManagerKey(key)
				.build();
		AreaManager areaManager2 = AreaManager.builder()
				.areaManagerKey(key2)
				.build();
		AreaManager areaManager3 = AreaManager.builder()
				.areaManagerKey(key3)
				.build();
		AreaManager areaManager4 = AreaManager.builder()
				.areaManagerKey(key4)
				.build();
		areaManagerRepository.save(areaManager);
		areaManagerRepository.save(areaManager2);
		areaManagerRepository.save(areaManager3);
		areaManagerRepository.save(areaManager4);

		ReservationSubmitForm submitForm = ReservationSubmitForm.builder()
				.servicePerson(2)
				.status(ReservationStatus.WAITING_FOR_MANAGER_REQUEST)
				.payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
				.totalPrice(40000)
				.reservationDate("2023-06-17 12:00:00")
				.deleted(false)
				.member(member)
				.build();

		formRepository.save(submitForm);

		Match match = Match.builder()
				.matching(true)
				.reservationForm(submitForm)
				.managerId(manager.getId())
				.managerName("김재우")
				.build();

		matchRepository.save(match);

		/**
		 *  테스트 응답 11가지
		 * */

		ReservationAnswer answer0 = ReservationAnswer.builder()
				.answer("3인분")
				.questionIdentify(QuestionIdentify.AFEWSERVINGS)
				.build();
		ReservationAnswer answer02 = ReservationAnswer.builder()
				.answer("인스타그램")
				.questionIdentify(QuestionIdentify.LEARNEDROUTE)
				.build();
		ReservationAnswer answer01 = ReservationAnswer.builder()
				.answer("홍길동")
				.questionIdentify(QuestionIdentify.APPLICANTNAME)
				.build();

		ReservationAnswer answer1 = ReservationAnswer.builder()
				.answer("3시간")
				.questionIdentify(QuestionIdentify.SERVICEDURATION)
				.build();

		ReservationAnswer answer2 = ReservationAnswer.builder()
				.answer("서울특별시 서초대로 23-100 아파트 333호")
				.questionIdentify(QuestionIdentify.ADDRESS)
				.build();

		ReservationAnswer answer3 = ReservationAnswer.builder()
				.answer("01039041094")
				.questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
				.build();

		ReservationAnswer answer4 = ReservationAnswer.builder()
				.answer("2023-06-17 12:00:00")
				.questionIdentify(QuestionIdentify.SERVICEDATE)
				.build();


		ReservationAnswer answer5 = ReservationAnswer.builder()
				.answer("주차 가능합니다~!")
				.questionIdentify(QuestionIdentify.PARKINGAVAILABLE)
				.build();

		ReservationAnswer answer6 = ReservationAnswer.builder()
				.answer("공동현관 비밀번호는 404 입니다")
				.questionIdentify(QuestionIdentify.RESERVATIONENTER)
				.build();

		ReservationAnswer answer7 = ReservationAnswer.builder()
				.answer("고양이와 강아지가 있어요~!")
				.questionIdentify(QuestionIdentify.RESERVATIONNOTE)
				.build();

		ReservationAnswer answer8 = ReservationAnswer.builder()
				.answer("잘 부탁드립니다~!~!")
				.questionIdentify(QuestionIdentify.RESERVATIONREQUEST)
				.build();


		submitForm.addAnswer(answer0);
		submitForm.addAnswer(answer01);
		submitForm.addAnswer(answer02);
		submitForm.addAnswer(answer1);
		submitForm.addAnswer(answer2);
		submitForm.addAnswer(answer3);
		submitForm.addAnswer(answer4);
		submitForm.addAnswer(answer5);
		submitForm.addAnswer(answer6);
		submitForm.addAnswer(answer7);
		submitForm.addAnswer(answer8);





		ReservationSubmitForm submitForm2 = ReservationSubmitForm.builder()
				.servicePerson(2)
				.status(ReservationStatus.WAITING_FOR_MANAGER_REQUEST)
				.payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
				.totalPrice(40000)
				.reservationDate("2023-06-17 12:00:00")
				.deleted(false)
				.member(member)
				.build();

		formRepository.save(submitForm2);

		Match match2 = Match.builder()
				.matching(true)
				.reservationForm(submitForm)
				.managerId(manager.getId())
				.managerName("김재우")
				.build();

		matchRepository.save(match2);

		/**
		 *  테스트 응답 11가지
		 * */

		ReservationAnswer answer11 = ReservationAnswer.builder()
				.answer("3인분")
				.questionIdentify(QuestionIdentify.AFEWSERVINGS)
				.build();
		ReservationAnswer answer12 = ReservationAnswer.builder()
				.answer("인스타그램")
				.questionIdentify(QuestionIdentify.LEARNEDROUTE)
				.build();
		ReservationAnswer answer13 = ReservationAnswer.builder()
				.answer("홍길동")
				.questionIdentify(QuestionIdentify.APPLICANTNAME)
				.build();

		ReservationAnswer answer14 = ReservationAnswer.builder()
				.answer("3시간")
				.questionIdentify(QuestionIdentify.SERVICEDURATION)
				.build();

		ReservationAnswer answer15 = ReservationAnswer.builder()
				.answer("서울특별시 서초대로 23-100 아파트 333호")
				.questionIdentify(QuestionIdentify.ADDRESS)
				.build();

		ReservationAnswer answer16 = ReservationAnswer.builder()
				.answer("01040783843")
				.questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
				.build();

		ReservationAnswer answer17 = ReservationAnswer.builder()
				.answer("2023-06-17 12:00:00")
				.questionIdentify(QuestionIdentify.SERVICEDATE)
				.build();


		ReservationAnswer answer18 = ReservationAnswer.builder()
				.answer("주차 가능합니다~!")
				.questionIdentify(QuestionIdentify.PARKINGAVAILABLE)
				.build();

		ReservationAnswer answer19 = ReservationAnswer.builder()
				.answer("공동현관 비밀번호는 404 입니다")
				.questionIdentify(QuestionIdentify.RESERVATIONENTER)
				.build();

		ReservationAnswer answer20 = ReservationAnswer.builder()
				.answer("고양이와 강아지가 있어요~!")
				.questionIdentify(QuestionIdentify.RESERVATIONNOTE)
				.build();

		ReservationAnswer answer21 = ReservationAnswer.builder()
				.answer("잘 부탁드립니다~!~!")
				.questionIdentify(QuestionIdentify.RESERVATIONREQUEST)
				.build();


		submitForm2.addAnswer(answer11);
		submitForm2.addAnswer(answer12);
		submitForm2.addAnswer(answer13);
		submitForm2.addAnswer(answer14);
		submitForm2.addAnswer(answer15);
		submitForm2.addAnswer(answer16);
		submitForm2.addAnswer(answer17);
		submitForm2.addAnswer(answer18);
		submitForm2.addAnswer(answer19);
		submitForm2.addAnswer(answer20);
		submitForm2.addAnswer(answer21);




		ReservationSubmitForm submitForm3 = ReservationSubmitForm.builder()
				.servicePerson(2)
				.status(ReservationStatus.WAITING_FOR_MANAGER_REQUEST)
				.payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
				.totalPrice(40000)
				.reservationDate("2023-06-17 12:00:00")
				.deleted(false)
				.member(member)
				.build();

		formRepository.save(submitForm3);

		Match match3 = Match.builder()
				.matching(true)
				.reservationForm(submitForm)
				.managerId(manager.getId())
				.managerName("김재우")
				.build();

		matchRepository.save(match3);


		ReservationAnswer answer111 = ReservationAnswer.builder()
				.answer("3인분")
				.questionIdentify(QuestionIdentify.AFEWSERVINGS)
				.build();
		ReservationAnswer answer121 = ReservationAnswer.builder()
				.answer("인스타그램")
				.questionIdentify(QuestionIdentify.LEARNEDROUTE)
				.build();
		ReservationAnswer answer131 = ReservationAnswer.builder()
				.answer("홍길동")
				.questionIdentify(QuestionIdentify.APPLICANTNAME)
				.build();

		ReservationAnswer answer141 = ReservationAnswer.builder()
				.answer("3시간")
				.questionIdentify(QuestionIdentify.SERVICEDURATION)
				.build();

		ReservationAnswer answer151 = ReservationAnswer.builder()
				.answer("서울특별시 서초대로 23-100 아파트 333호")
				.questionIdentify(QuestionIdentify.ADDRESS)
				.build();

		ReservationAnswer answer161 = ReservationAnswer.builder()
				.answer("01040783843")
				.questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
				.build();

		ReservationAnswer answer171 = ReservationAnswer.builder()
				.answer("2023-06-17 12:00:00")
				.questionIdentify(QuestionIdentify.SERVICEDATE)
				.build();


		ReservationAnswer answer181 = ReservationAnswer.builder()
				.answer("주차 가능합니다~!")
				.questionIdentify(QuestionIdentify.PARKINGAVAILABLE)
				.build();

		ReservationAnswer answer191 = ReservationAnswer.builder()
				.answer("공동현관 비밀번호는 404 입니다")
				.questionIdentify(QuestionIdentify.RESERVATIONENTER)
				.build();

		ReservationAnswer answer201 = ReservationAnswer.builder()
				.answer("고양이와 강아지가 있어요~!")
				.questionIdentify(QuestionIdentify.RESERVATIONNOTE)
				.build();

		ReservationAnswer answer211 = ReservationAnswer.builder()
				.answer("잘 부탁드립니다~!~!")
				.questionIdentify(QuestionIdentify.RESERVATIONREQUEST)
				.build();


		submitForm3.addAnswer(answer111);
		submitForm3.addAnswer(answer121);
		submitForm3.addAnswer(answer131);
		submitForm3.addAnswer(answer141);
		submitForm3.addAnswer(answer151);
		submitForm3.addAnswer(answer161);
		submitForm3.addAnswer(answer171);
		submitForm3.addAnswer(answer181);
		submitForm3.addAnswer(answer191);
		submitForm3.addAnswer(answer201);
		submitForm3.addAnswer(answer211);




		ReservationSubmitForm submitForm4 = ReservationSubmitForm.builder()
				.servicePerson(2)
				.status(ReservationStatus.WAITING_FOR_MANAGER_REQUEST)
				.payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
				.totalPrice(40000)
				.reservationDate("2023-06-17 12:00:00")
				.deleted(false)
				.member(member)
				.build();

		formRepository.save(submitForm4);



		ReservationAnswer answer1111 = ReservationAnswer.builder()
				.answer("3인분")
				.questionIdentify(QuestionIdentify.AFEWSERVINGS)
				.build();
		ReservationAnswer answer1211 = ReservationAnswer.builder()
				.answer("인스타그램")
				.questionIdentify(QuestionIdentify.LEARNEDROUTE)
				.build();
		ReservationAnswer answer1311 = ReservationAnswer.builder()
				.answer("홍길동")
				.questionIdentify(QuestionIdentify.APPLICANTNAME)
				.build();

		ReservationAnswer answer1411 = ReservationAnswer.builder()
				.answer("3시간")
				.questionIdentify(QuestionIdentify.SERVICEDURATION)
				.build();

		ReservationAnswer answer1511 = ReservationAnswer.builder()
				.answer("서울특별시 서초대로 23-100 아파트 333호")
				.questionIdentify(QuestionIdentify.ADDRESS)
				.build();

		ReservationAnswer answer1611 = ReservationAnswer.builder()
				.answer("01040783843")
				.questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
				.build();

		ReservationAnswer answer1711 = ReservationAnswer.builder()
				.answer("2023-06-17 12:00:00")
				.questionIdentify(QuestionIdentify.SERVICEDATE)
				.build();


		ReservationAnswer answer1811 = ReservationAnswer.builder()
				.answer("주차 가능합니다~!")
				.questionIdentify(QuestionIdentify.PARKINGAVAILABLE)
				.build();

		ReservationAnswer answer1911 = ReservationAnswer.builder()
				.answer("공동현관 비밀번호는 404 입니다")
				.questionIdentify(QuestionIdentify.RESERVATIONENTER)
				.build();

		ReservationAnswer answer2011 = ReservationAnswer.builder()
				.answer("고양이와 강아지가 있어요~!")
				.questionIdentify(QuestionIdentify.RESERVATIONNOTE)
				.build();

		ReservationAnswer answer2111 = ReservationAnswer.builder()
				.answer("잘 부탁드립니다~!~!")
				.questionIdentify(QuestionIdentify.RESERVATIONREQUEST)
				.build();


		submitForm4.addAnswer(answer1111);
		submitForm4.addAnswer(answer1211);
		submitForm4.addAnswer(answer1311);
		submitForm4.addAnswer(answer1411);
		submitForm4.addAnswer(answer1511);
		submitForm4.addAnswer(answer1611);
		submitForm4.addAnswer(answer1711);
		submitForm4.addAnswer(answer1811);
		submitForm4.addAnswer(answer1911);
		submitForm4.addAnswer(answer2011);
		submitForm4.addAnswer(answer2111);



		ReservationSubmitForm submitForm5 = ReservationSubmitForm.builder()
				.servicePerson(2)
				.status(ReservationStatus.WAITING_FOR_MANAGER_REQUEST)
				.payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
				.totalPrice(40000)
				.reservationDate("2023-06-17 12:00:00")
				.deleted(false)
				.member(member)
				.build();

		formRepository.save(submitForm5);



		ReservationAnswer answer11111 = ReservationAnswer.builder()
				.answer("3인분")
				.questionIdentify(QuestionIdentify.AFEWSERVINGS)
				.build();
		ReservationAnswer answer12111 = ReservationAnswer.builder()
				.answer("인스타그램")
				.questionIdentify(QuestionIdentify.LEARNEDROUTE)
				.build();
		ReservationAnswer answer13111 = ReservationAnswer.builder()
				.answer("홍길동")
				.questionIdentify(QuestionIdentify.APPLICANTNAME)
				.build();

		ReservationAnswer answer14111 = ReservationAnswer.builder()
				.answer("3시간")
				.questionIdentify(QuestionIdentify.SERVICEDURATION)
				.build();

		ReservationAnswer answer15111 = ReservationAnswer.builder()
				.answer("서울특별시 서초대로 23-100 아파트 333호")
				.questionIdentify(QuestionIdentify.ADDRESS)
				.build();

		ReservationAnswer answer16111 = ReservationAnswer.builder()
				.answer("01040783843")
				.questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
				.build();

		ReservationAnswer answer17111 = ReservationAnswer.builder()
				.answer("2023-06-17 12:00:00")
				.questionIdentify(QuestionIdentify.SERVICEDATE)
				.build();


		ReservationAnswer answer18111 = ReservationAnswer.builder()
				.answer("주차 가능합니다~!")
				.questionIdentify(QuestionIdentify.PARKINGAVAILABLE)
				.build();

		ReservationAnswer answer19111 = ReservationAnswer.builder()
				.answer("공동현관 비밀번호는 404 입니다")
				.questionIdentify(QuestionIdentify.RESERVATIONENTER)
				.build();

		ReservationAnswer answer20111 = ReservationAnswer.builder()
				.answer("고양이와 강아지가 있어요~!")
				.questionIdentify(QuestionIdentify.RESERVATIONNOTE)
				.build();

		ReservationAnswer answer21111 = ReservationAnswer.builder()
				.answer("잘 부탁드립니다~!~!")
				.questionIdentify(QuestionIdentify.RESERVATIONREQUEST)
				.build();


		submitForm5.addAnswer(answer11111);
		submitForm5.addAnswer(answer12111);
		submitForm5.addAnswer(answer13111);
		submitForm5.addAnswer(answer14111);
		submitForm5.addAnswer(answer15111);
		submitForm5.addAnswer(answer16111);
		submitForm5.addAnswer(answer17111);
		submitForm5.addAnswer(answer18111);
		submitForm5.addAnswer(answer19111);
		submitForm5.addAnswer(answer20111);
		submitForm5.addAnswer(answer21111);



		ReservationSubmitForm submitForm6 = ReservationSubmitForm.builder()
				.servicePerson(2)
				.status(ReservationStatus.WAITING_FOR_MANAGER_REQUEST)
				.payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
				.totalPrice(40000)
				.reservationDate("2023-06-17 12:00:00")
				.deleted(false)
				.member(member)
				.build();

		formRepository.save(submitForm6);



		ReservationAnswer answer111111 = ReservationAnswer.builder()
				.answer("3인분")
				.questionIdentify(QuestionIdentify.AFEWSERVINGS)
				.build();
		ReservationAnswer answer121111 = ReservationAnswer.builder()
				.answer("인스타그램")
				.questionIdentify(QuestionIdentify.LEARNEDROUTE)
				.build();
		ReservationAnswer answer131111 = ReservationAnswer.builder()
				.answer("홍길동")
				.questionIdentify(QuestionIdentify.APPLICANTNAME)
				.build();

		ReservationAnswer answer141111 = ReservationAnswer.builder()
				.answer("3시간")
				.questionIdentify(QuestionIdentify.SERVICEDURATION)
				.build();

		ReservationAnswer answer151111 = ReservationAnswer.builder()
				.answer("서울특별시 서초대로 23-100 아파트 333호")
				.questionIdentify(QuestionIdentify.ADDRESS)
				.build();

		ReservationAnswer answer161111 = ReservationAnswer.builder()
				.answer("01040783843")
				.questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
				.build();

		ReservationAnswer answer171111 = ReservationAnswer.builder()
				.answer("2023-06-17 12:00:00")
				.questionIdentify(QuestionIdentify.SERVICEDATE)
				.build();


		ReservationAnswer answer181111 = ReservationAnswer.builder()
				.answer("주차 가능합니다~!")
				.questionIdentify(QuestionIdentify.PARKINGAVAILABLE)
				.build();

		ReservationAnswer answer191111 = ReservationAnswer.builder()
				.answer("공동현관 비밀번호는 404 입니다")
				.questionIdentify(QuestionIdentify.RESERVATIONENTER)
				.build();

		ReservationAnswer answer201111 = ReservationAnswer.builder()
				.answer("고양이와 강아지가 있어요~!")
				.questionIdentify(QuestionIdentify.RESERVATIONNOTE)
				.build();

		ReservationAnswer answer211111 = ReservationAnswer.builder()
				.answer("잘 부탁드립니다~!~!")
				.questionIdentify(QuestionIdentify.RESERVATIONREQUEST)
				.build();


		submitForm6.addAnswer(answer111111);
		submitForm6.addAnswer(answer121111);
		submitForm6.addAnswer(answer131111);
		submitForm6.addAnswer(answer141111);
		submitForm6.addAnswer(answer151111);
		submitForm6.addAnswer(answer161111);
		submitForm6.addAnswer(answer171111);
		submitForm6.addAnswer(answer181111);
		submitForm6.addAnswer(answer191111);
		submitForm6.addAnswer(answer201111);
		submitForm6.addAnswer(answer211111);


		ReservationSubmitForm submitForm7 = ReservationSubmitForm.builder()
				.servicePerson(2)
				.status(ReservationStatus.WAITING_FOR_MANAGER_REQUEST)
				.payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
				.totalPrice(40000)
				.reservationDate("2023-06-17 12:00:00")
				.deleted(false)
				.member(member)
				.build();

		formRepository.save(submitForm7);



		ReservationAnswer answer1111111 = ReservationAnswer.builder()
				.answer("3인분")
				.questionIdentify(QuestionIdentify.AFEWSERVINGS)
				.build();
		ReservationAnswer answer1211111 = ReservationAnswer.builder()
				.answer("인스타그램")
				.questionIdentify(QuestionIdentify.LEARNEDROUTE)
				.build();
		ReservationAnswer answer1311111 = ReservationAnswer.builder()
				.answer("홍길동")
				.questionIdentify(QuestionIdentify.APPLICANTNAME)
				.build();

		ReservationAnswer answer1411111 = ReservationAnswer.builder()
				.answer("3시간")
				.questionIdentify(QuestionIdentify.SERVICEDURATION)
				.build();

		ReservationAnswer answer1511111 = ReservationAnswer.builder()
				.answer("서울특별시 서초대로 23-100 아파트 333호")
				.questionIdentify(QuestionIdentify.ADDRESS)
				.build();

		ReservationAnswer answer1611111 = ReservationAnswer.builder()
				.answer("01040783843")
				.questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
				.build();

		ReservationAnswer answer1711111 = ReservationAnswer.builder()
				.answer("2023-06-17 12:00:00")
				.questionIdentify(QuestionIdentify.SERVICEDATE)
				.build();


		ReservationAnswer answer1811111 = ReservationAnswer.builder()
				.answer("주차 가능합니다~!")
				.questionIdentify(QuestionIdentify.PARKINGAVAILABLE)
				.build();

		ReservationAnswer answer1911111 = ReservationAnswer.builder()
				.answer("공동현관 비밀번호는 404 입니다")
				.questionIdentify(QuestionIdentify.RESERVATIONENTER)
				.build();

		ReservationAnswer answer2011111 = ReservationAnswer.builder()
				.answer("고양이와 강아지가 있어요~!")
				.questionIdentify(QuestionIdentify.RESERVATIONNOTE)
				.build();

		ReservationAnswer answer2111111 = ReservationAnswer.builder()
				.answer("잘 부탁드립니다~!~!")
				.questionIdentify(QuestionIdentify.RESERVATIONREQUEST)
				.build();


		submitForm7.addAnswer(answer1111111);
		submitForm7.addAnswer(answer1211111);
		submitForm7.addAnswer(answer1311111);
		submitForm7.addAnswer(answer1411111);
		submitForm7.addAnswer(answer1511111);
		submitForm7.addAnswer(answer1611111);
		submitForm7.addAnswer(answer1711111);
		submitForm7.addAnswer(answer1811111);
		submitForm7.addAnswer(answer1911111);
		submitForm7.addAnswer(answer2011111);
		submitForm7.addAnswer(answer2111111);



		ReservationSubmitForm submitForm8 = ReservationSubmitForm.builder()
				.servicePerson(2)
				.status(ReservationStatus.WAITING_FOR_MANAGER_REQUEST)
				.payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
				.totalPrice(40000)
				.reservationDate("2023-06-17 12:00:00")
				.deleted(false)
				.member(member)
				.build();

		formRepository.save(submitForm8);



		ReservationAnswer answer11111111 = ReservationAnswer.builder()
				.answer("3인분")
				.questionIdentify(QuestionIdentify.AFEWSERVINGS)
				.build();
		ReservationAnswer answer12111111 = ReservationAnswer.builder()
				.answer("인스타그램")
				.questionIdentify(QuestionIdentify.LEARNEDROUTE)
				.build();
		ReservationAnswer answer13111111 = ReservationAnswer.builder()
				.answer("홍길동")
				.questionIdentify(QuestionIdentify.APPLICANTNAME)
				.build();

		ReservationAnswer answer14111111 = ReservationAnswer.builder()
				.answer("3시간")
				.questionIdentify(QuestionIdentify.SERVICEDURATION)
				.build();

		ReservationAnswer answer15111111 = ReservationAnswer.builder()
				.answer("서울특별시 서초대로 23-100 아파트 333호")
				.questionIdentify(QuestionIdentify.ADDRESS)
				.build();

		ReservationAnswer answer16111111 = ReservationAnswer.builder()
				.answer("01040783843")
				.questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
				.build();

		ReservationAnswer answer17111111 = ReservationAnswer.builder()
				.answer("2023-06-17 12:00:00")
				.questionIdentify(QuestionIdentify.SERVICEDATE)
				.build();


		ReservationAnswer answer18111111 = ReservationAnswer.builder()
				.answer("주차 가능합니다~!")
				.questionIdentify(QuestionIdentify.PARKINGAVAILABLE)
				.build();

		ReservationAnswer answer19111111 = ReservationAnswer.builder()
				.answer("공동현관 비밀번호는 404 입니다")
				.questionIdentify(QuestionIdentify.RESERVATIONENTER)
				.build();

		ReservationAnswer answer20111111 = ReservationAnswer.builder()
				.answer("고양이와 강아지가 있어요~!")
				.questionIdentify(QuestionIdentify.RESERVATIONNOTE)
				.build();

		ReservationAnswer answer21111111 = ReservationAnswer.builder()
				.answer("잘 부탁드립니다~!~!")
				.questionIdentify(QuestionIdentify.RESERVATIONREQUEST)
				.build();


		submitForm8.addAnswer(answer11111111);
		submitForm8.addAnswer(answer12111111);
		submitForm8.addAnswer(answer13111111);
		submitForm8.addAnswer(answer14111111);
		submitForm8.addAnswer(answer15111111);
		submitForm8.addAnswer(answer16111111);
		submitForm8.addAnswer(answer17111111);
		submitForm8.addAnswer(answer18111111);
		submitForm8.addAnswer(answer19111111);
		submitForm8.addAnswer(answer20111111);
		submitForm8.addAnswer(answer21111111);




		ReservationSubmitForm submitForm9 = ReservationSubmitForm.builder()
				.servicePerson(2)
				.status(ReservationStatus.WAITING_FOR_MANAGER_REQUEST)
				.payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
				.totalPrice(40000)
				.reservationDate("2023-06-17 12:00:00")
				.deleted(false)
				.member(member)
				.build();

		formRepository.save(submitForm9);



		ReservationAnswer answer111111111 = ReservationAnswer.builder()
				.answer("3인분")
				.questionIdentify(QuestionIdentify.AFEWSERVINGS)
				.build();
		ReservationAnswer answer121111111 = ReservationAnswer.builder()
				.answer("인스타그램")
				.questionIdentify(QuestionIdentify.LEARNEDROUTE)
				.build();
		ReservationAnswer answer131111111 = ReservationAnswer.builder()
				.answer("홍길동")
				.questionIdentify(QuestionIdentify.APPLICANTNAME)
				.build();

		ReservationAnswer answer141111111 = ReservationAnswer.builder()
				.answer("3시간")
				.questionIdentify(QuestionIdentify.SERVICEDURATION)
				.build();

		ReservationAnswer answer151111111 = ReservationAnswer.builder()
				.answer("서울특별시 서초대로 23-100 아파트 333호")
				.questionIdentify(QuestionIdentify.ADDRESS)
				.build();

		ReservationAnswer answer161111111 = ReservationAnswer.builder()
				.answer("01040783843")
				.questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
				.build();

		ReservationAnswer answer171111111 = ReservationAnswer.builder()
				.answer("2023-06-17 12:00:00")
				.questionIdentify(QuestionIdentify.SERVICEDATE)
				.build();


		ReservationAnswer answer181111111 = ReservationAnswer.builder()
				.answer("주차 가능합니다~!")
				.questionIdentify(QuestionIdentify.PARKINGAVAILABLE)
				.build();

		ReservationAnswer answer191111111 = ReservationAnswer.builder()
				.answer("공동현관 비밀번호는 404 입니다")
				.questionIdentify(QuestionIdentify.RESERVATIONENTER)
				.build();

		ReservationAnswer answer201111111 = ReservationAnswer.builder()
				.answer("고양이와 강아지가 있어요~!")
				.questionIdentify(QuestionIdentify.RESERVATIONNOTE)
				.build();

		ReservationAnswer answer211111111 = ReservationAnswer.builder()
				.answer("잘 부탁드립니다~!~!")
				.questionIdentify(QuestionIdentify.RESERVATIONREQUEST)
				.build();


		submitForm9.addAnswer(answer111111111);
		submitForm9.addAnswer(answer121111111);
		submitForm9.addAnswer(answer131111111);
		submitForm9.addAnswer(answer141111111);
		submitForm9.addAnswer(answer151111111);
		submitForm9.addAnswer(answer161111111);
		submitForm9.addAnswer(answer171111111);
		submitForm9.addAnswer(answer181111111);
		submitForm9.addAnswer(answer191111111);
		submitForm9.addAnswer(answer201111111);
		submitForm9.addAnswer(answer211111111);



		ReservationSubmitForm submitForm10 = ReservationSubmitForm.builder()
				.servicePerson(2)
				.status(ReservationStatus.WAITING_FOR_MANAGER_REQUEST)
				.payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
				.totalPrice(40000)
				.reservationDate("2023-06-17 12:00:00")
				.deleted(false)
				.member(member)
				.build();

		formRepository.save(submitForm10);



		ReservationAnswer answer1111111111 = ReservationAnswer.builder()
				.answer("3인분")
				.questionIdentify(QuestionIdentify.AFEWSERVINGS)
				.build();
		ReservationAnswer answer1211111111 = ReservationAnswer.builder()
				.answer("인스타그램")
				.questionIdentify(QuestionIdentify.LEARNEDROUTE)
				.build();
		ReservationAnswer answer1311111111 = ReservationAnswer.builder()
				.answer("홍길동")
				.questionIdentify(QuestionIdentify.APPLICANTNAME)
				.build();

		ReservationAnswer answer1411111111 = ReservationAnswer.builder()
				.answer("3시간")
				.questionIdentify(QuestionIdentify.SERVICEDURATION)
				.build();

		ReservationAnswer answer1511111111 = ReservationAnswer.builder()
				.answer("서울특별시 서초대로 23-100 아파트 333호")
				.questionIdentify(QuestionIdentify.ADDRESS)
				.build();

		ReservationAnswer answer1611111111 = ReservationAnswer.builder()
				.answer("01040783843")
				.questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
				.build();

		ReservationAnswer answer1711111111 = ReservationAnswer.builder()
				.answer("2023-06-17 12:00:00")
				.questionIdentify(QuestionIdentify.SERVICEDATE)
				.build();


		ReservationAnswer answer1811111111 = ReservationAnswer.builder()
				.answer("주차 가능합니다~!")
				.questionIdentify(QuestionIdentify.PARKINGAVAILABLE)
				.build();

		ReservationAnswer answer1911111111 = ReservationAnswer.builder()
				.answer("공동현관 비밀번호는 404 입니다")
				.questionIdentify(QuestionIdentify.RESERVATIONENTER)
				.build();

		ReservationAnswer answer2011111111 = ReservationAnswer.builder()
				.answer("고양이와 강아지가 있어요~!")
				.questionIdentify(QuestionIdentify.RESERVATIONNOTE)
				.build();

		ReservationAnswer answer2111111111 = ReservationAnswer.builder()
				.answer("잘 부탁드립니다~!~!")
				.questionIdentify(QuestionIdentify.RESERVATIONREQUEST)
				.build();


		submitForm10.addAnswer(answer1111111111);
		submitForm10.addAnswer(answer1211111111);
		submitForm10.addAnswer(answer1311111111);
		submitForm10.addAnswer(answer1411111111);
		submitForm10.addAnswer(answer1511111111);
		submitForm10.addAnswer(answer1611111111);
		submitForm10.addAnswer(answer1711111111);
		submitForm10.addAnswer(answer1811111111);
		submitForm10.addAnswer(answer1911111111);
		submitForm10.addAnswer(answer2011111111);
		submitForm10.addAnswer(answer2111111111);




	}

}