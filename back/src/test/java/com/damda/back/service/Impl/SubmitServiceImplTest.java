package com.damda.back.service.Impl;

import com.damda.back.data.common.PayMentStatus;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.ReservationStatus;
import com.damda.back.data.common.SubmitSlice;
import com.damda.back.data.request.SubmitRequestDTO;
import com.damda.back.data.response.FormResultDTO;
import com.damda.back.data.response.Statistical;
import com.damda.back.domain.Member;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.manager.Manager;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.MemberRepository;
import com.damda.back.repository.ReservationFormRepository;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmitServiceImplTest {


    @InjectMocks
    private SubmitServiceImpl submitService;

    @Mock
    ReservationFormRepository repository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ManagerRepository managerRepository;
    @Mock
    MatchServiceImpl matchService;

    @Mock
    TalkSendServiceImpl talkSendService;




    @Test
    @DisplayName("예약폼 리스트 테스트")
    void reservation_submit_form_list_test() {
        // given
        int page = 0;
        String startDate = "2023-06-01";
        String endDate = "2023-06-30";

        Member member = Member.builder().username("김재우").build();

        ReservationSubmitForm form = Mockito.mock(ReservationSubmitForm.class);
        when(form.getStatus()).thenReturn(ReservationStatus.WAITING_FOR_MANAGER_REQUEST);
        when(form.getPayMentStatus()).thenReturn(PayMentStatus.NOT_PAID_FOR_ANYTHING);
        when(form.getCreatedAt()).thenReturn(Timestamp.valueOf("2023-06-17 12:23:58.97"));
        when(form.getMember()).thenReturn(member);


        ReservationSubmitForm form2 = Mockito.mock(ReservationSubmitForm.class);
        when(form2.getStatus()).thenReturn(ReservationStatus.WAITING_FOR_MANAGER_REQUEST);
        when(form2.getPayMentStatus()).thenReturn(PayMentStatus.NOT_PAID_FOR_ANYTHING);
        when(form2.getCreatedAt()).thenReturn(Timestamp.valueOf("2023-06-17 12:23:58.97"));
        when(form2.getMember()).thenReturn(member);

        List<ReservationSubmitForm> mockedForms = Arrays.asList(form, form2);

        Page<ReservationSubmitForm> mockedPage = new PageImpl<>(mockedForms);
        when(repository.formPaging(any(PageRequest.class),any(Timestamp.class),any(Timestamp.class)))
                .thenReturn(mockedPage);

        // when

        FormResultDTO resultDTO = submitService.submitTotalResponse(page,startDate,endDate);
        Statistical statistical = resultDTO.getStatistical();

        // then
        Assertions.assertThat(resultDTO).isNotNull();
        Assertions.assertThat(resultDTO.getContent().size()).isEqualTo(2);
        Assertions.assertThat(resultDTO.getTotal()).isEqualTo(2);
        Assertions.assertThat(resultDTO.isFirst()).isTrue();
        Assertions.assertThat(resultDTO.isLast()).isTrue();

        Assertions.assertThat(statistical.getCancellation()).isEqualTo(0L);
        Assertions.assertThat(statistical.getWating()).isEqualTo(0L);
        Assertions.assertThat(statistical.getCompleted()).isEqualTo(0L);
        Assertions.assertThat(statistical.getConfirmation()).isEqualTo(0L);
        Assertions.assertThat(statistical.getMatching()).isEqualTo(0L);

    }

    @Test
    @DisplayName("유저 제출 폼 저장하기")
    void user_submit_save() {
        // given
        Member member = Member.builder().username("김재우").build();
        Manager manager = Manager.builder()
                .id(1L)
                .name("김매니저")
                .build();


        ReservationSubmitForm form = Mockito.mock(ReservationSubmitForm.class);



        List<SubmitSlice> submitSlices = List.of(SubmitSlice.builder()
                        .answer("3시간")
                        .questionIdentify(QuestionIdentify.SERVICEDURATION)
                .build());


        SubmitRequestDTO dto = SubmitRequestDTO.builder()
                .submit(submitSlices)
                .addressFront("하남시")
                .servicePerson(3)
                .build();
        // when
        when(form.getId()).thenReturn(1L);
        when(repository.save(any())).thenReturn(form);
        when(memberRepository.findById(anyInt())).thenReturn(Optional.of(member));
        given(managerRepository.managerWithArea(any(String.class))).willReturn(List.of(manager));
        Long formId = submitService.jpaFormInsert(dto,1);

        // then
        Assertions.assertThat(formId).isNotNull();
        Assertions.assertThat(formId).isEqualTo(1L);
    }
}