package com.damda.back.service.Impl;

import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.request.SubmitRequestDTO;
import com.damda.back.repository.ReservationFormRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SubmitServiceImplTest {


    @InjectMocks
    private SubmitServiceImpl submitService;

    @Mock
    ReservationFormRepository repository;


    @Test
    @DisplayName("유저 제출 폼 저장하기")
    void user_submit_save() {
        // given

        // when

        // then
    }
}