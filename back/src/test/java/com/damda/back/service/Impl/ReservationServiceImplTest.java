package com.damda.back.service.Impl;

import com.damda.back.repository.AreaRepository;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.QuestionRepository;
import com.damda.back.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AreaRepository areaRepository;


    @Test
    @DisplayName("")
    void ReservationServiceImplTest() {
        // given

        // when

        // then
    }
}