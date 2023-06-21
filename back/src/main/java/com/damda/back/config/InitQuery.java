package com.damda.back.config;


import com.damda.back.data.common.MatchResponseStatus;
import com.damda.back.data.common.PayMentStatus;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.ReservationStatus;
import com.damda.back.domain.Match;
import com.damda.back.domain.Member;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.area.Area;
import com.damda.back.domain.manager.AreaManager;
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
                    .username("testMember")
                    .password("1234")
                    .profileImage("404.jpg")
                    .build();

            memberRepository.save(member);

            Manager manager = managerRepository.save(Manager.builder()
                    .managerPhoneNumber("01040783843")
                    .managerName("김재우")
                    .currManagerStatus(ManagerStatusEnum.ACTIVE)
                    .build());

            Manager manager2 = managerRepository.save(Manager.builder()
                    .managerPhoneNumber("01039041094")
                    .managerName("고예림")
                    .currManagerStatus(ManagerStatusEnum.ACTIVE)
                    .build());

            Manager manager3 = managerRepository.save(Manager.builder()
                    .managerPhoneNumber("01082535890")
                    .managerName("김형준")
                    .currManagerStatus(ManagerStatusEnum.ACTIVE)
                    .build());


            Area area1 = Area.builder()
                    .city("서울특별시")
                    .district("강남구")
                    .managerCount(1)
                    .build();

            Area area2 = Area.builder()
                    .city("서울특별시")
                    .district("강북구")
                    .managerCount(1)
                    .build();

            Area area = Area.builder()
                    .city("경기도")
                    .district("하남시")
                    .managerCount(1)
                    .build();

            areaRepository.save(area1);
            areaRepository.save(area2);
            areaRepository.save(area);

            AreaManager.AreaManagerKey key = new AreaManager.AreaManagerKey(area,manager);
            AreaManager.AreaManagerKey key2 = new AreaManager.AreaManagerKey(area1,manager2);
            AreaManager.AreaManagerKey key3 = new AreaManager.AreaManagerKey(area2,manager3);


            AreaManager areaManager = AreaManager.builder()
                    .areaManagerKey(key)
                    .build();
            AreaManager areaManager2 = AreaManager.builder()
                    .areaManagerKey(key2)
                    .build();
            AreaManager areaManager3 = AreaManager.builder()
                    .areaManagerKey(key3)
                    .build();

            areaManagerRepository.save(areaManager);
            areaManagerRepository.save(areaManager2);
            areaManagerRepository.save(areaManager3);


            ReservationSubmitForm submitForm = ReservationSubmitForm.builder()
                    .servicePerson(2)
                    .status(ReservationStatus.WAITING_FOR_MANAGER_REQUEST)
                    .payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
                    .totalPrice(40000)
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


            ReservationAnswer answer1 = ReservationAnswer.builder()
                    .answer("3시간")
                    .questionIdentify(QuestionIdentify.SERVICEDURATION)
                    .build();

            ReservationAnswer answer2 = ReservationAnswer.builder()
                    .answer("주소")
                    .questionIdentify(QuestionIdentify.ADDRESS)
                    .build();

            ReservationAnswer answer3 = ReservationAnswer.builder()
                    .answer("01040783843")
                    .questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
                    .build();

            ReservationAnswer answer4 = ReservationAnswer.builder()
                    .answer("2023-06-017 12:00:00")
                    .questionIdentify(QuestionIdentify.SERVICEDATE)
                    .build();

            submitForm.addAnswer(answer1);
            submitForm.addAnswer(answer2);
            submitForm.addAnswer(answer3);
            submitForm.addAnswer(answer4);





            ReservationSubmitForm submitForm2 = ReservationSubmitForm.builder()
                    .servicePerson(2)
                    .status(ReservationStatus.MANAGER_MATCHING_COMPLETED)
                    .payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
                    .totalPrice(40000)
                    .deleted(false)
                    .member(member)
                    .build();

            formRepository.save(submitForm2);

            Match match2 = Match.builder()
                    .matching(true)
                    .managerId(manager.getId())
                    .reservationForm(submitForm2)
                    .managerName("김재우")
                    .build();

            matchRepository.save(match2);

            ReservationAnswer answer12 = ReservationAnswer.builder()
                    .answer("3시간")
                    .questionIdentify(QuestionIdentify.SERVICEDURATION)
                    .build();

            ReservationAnswer answer22 = ReservationAnswer.builder()
                    .answer("주소")
                    .questionIdentify(QuestionIdentify.ADDRESS)
                    .build();

            ReservationAnswer answer33 = ReservationAnswer.builder()
                    .answer("주소")
                    .questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
                    .build();

            ReservationAnswer answer44 = ReservationAnswer.builder()
                    .answer("2023-06-017 12:00:00")
                    .questionIdentify(QuestionIdentify.SERVICEDATE)
                    .build();

            submitForm2.addAnswer(answer12);
            submitForm2.addAnswer(answer22);
            submitForm2.addAnswer(answer33);
            submitForm2.addAnswer(answer44);



            ReservationSubmitForm submitForm3 = ReservationSubmitForm.builder()
                    .servicePerson(2)
                    .status(ReservationStatus.MANAGER_MATCHING_COMPLETED)
                    .payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
                    .totalPrice(40000)
                    .deleted(false)
                    .member(member)
                    .build();

            formRepository.save(submitForm3);

            Match match3 = Match.builder()
                    .matching(true)
                    .managerId(manager.getId())
                    .reservationForm(submitForm3)
                    .managerName("김재우")
                    .build();

            matchRepository.save(match3);

            ReservationAnswer answer123 = ReservationAnswer.builder()
                    .answer("3시간")
                    .questionIdentify(QuestionIdentify.SERVICEDURATION)
                    .build();

            ReservationAnswer answer223 = ReservationAnswer.builder()
                    .answer("주소")
                    .questionIdentify(QuestionIdentify.ADDRESS)
                    .build();

            ReservationAnswer answer333 = ReservationAnswer.builder()
                    .answer("주소")
                    .questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
                    .build();

            ReservationAnswer answer443 = ReservationAnswer.builder()
                    .answer("2023-06-017 12:00:00")
                    .questionIdentify(QuestionIdentify.SERVICEDATE)
                    .build();

            submitForm3.addAnswer(answer123);
            submitForm3.addAnswer(answer223);
            submitForm3.addAnswer(answer333);
            submitForm3.addAnswer(answer443);


            ReservationSubmitForm submitForm4 = ReservationSubmitForm.builder()
                    .servicePerson(2)
                    .status(ReservationStatus.MANAGER_MATCHING_COMPLETED)
                    .payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
                    .totalPrice(40000)
                    .deleted(false)
                    .member(member)
                    .build();

            formRepository.save(submitForm4);

            Match match4 = Match.builder()
                    .matching(true)
                    .managerId(manager.getId())
                    .reservationForm(submitForm2)
                    .matchStatus(MatchResponseStatus.WAITING)
                    .managerName("김재우")
                    .build();

            matchRepository.save(match4);

            ReservationAnswer answer1234 = ReservationAnswer.builder()
                    .answer("3시간")
                    .questionIdentify(QuestionIdentify.SERVICEDURATION)
                    .build();

            ReservationAnswer answer2234 = ReservationAnswer.builder()
                    .answer("주소")
                    .questionIdentify(QuestionIdentify.ADDRESS)
                    .build();

            ReservationAnswer answer3334 = ReservationAnswer.builder()
                    .answer("주소")
                    .questionIdentify(QuestionIdentify.APPLICANTCONACTINFO)
                    .build();

            ReservationAnswer answer4434 = ReservationAnswer.builder()
                    .answer("2023-06-017 12:00:00")
                    .questionIdentify(QuestionIdentify.SERVICEDATE)
                    .build();

            submitForm4.addAnswer(answer1234);
            submitForm4.addAnswer(answer2234);
            submitForm4.addAnswer(answer3334);
            submitForm4.addAnswer(answer4434);


        }

}
