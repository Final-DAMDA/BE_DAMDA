package com.damda.back.data.common;

import com.damda.back.domain.Member;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.ReservationSubmitForm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DummyEntity {

	public Member newMember(String name, String address, String gender, String phone, String image){
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return Member.builder()
				.username(name)
				.address(address)
				.status(MemberStatus.ACTIVATION)
				.password(passwordEncoder.encode("1111"))
				.gender(gender)
				.phoneNumber(phone)
				.profileImage(image)
				.build();
	}

	public ReservationSubmitForm newReservationSubmitForm(Member member){
		return ReservationSubmitForm.builder()
				.deleted(false)
				.member(member)
				.payMentStatus(PayMentStatus.NOT_PAID_FOR_ANYTHING)
				.build();
	}

	public ReservationAnswer newReservationAnswer(String answer, QuestionIdentify questionIdentify,ReservationSubmitForm reservationSubmitForm){
		return ReservationAnswer.builder()
				.answer(answer)
				.formId(reservationSubmitForm.getId())
				.reservationSubmitForm(reservationSubmitForm)
				.questionIdentify(questionIdentify)
				.build();
	}





}
