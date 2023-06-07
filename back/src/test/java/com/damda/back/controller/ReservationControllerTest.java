//package com.damda.back.controller;
//
//import com.damda.back.config.SecurityConfig;
//import com.damda.back.data.common.QuestionIdentify;
//import com.damda.back.data.common.QuestionType;
//import com.damda.back.data.request.ReservationFormRequestDTO;
//import com.damda.back.service.Impl.ReservationServiceImpl;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@Import(SecurityConfig.class)
//@WebMvcTest(ReservationController.class)
//class ReservationControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    ReservationServiceImpl reservationService;
//
//
//    @Test
//    @DisplayName("")
//    void reservation_post_test() throws Exception {
//        // given
//        ObjectMapper objectMapper = new ObjectMapper();
//        // when
//        Map<String,Integer> map = new HashMap<>();
//
//        map.put("강동구",5000);
//        map.put("성동구",20000);
//        map.put("강남구",400000);
//
//        ReservationFormRequestDTO dto = ReservationFormRequestDTO.builder()
//                .questionTitle("서비스를 원하는 지역이 어디인가요?")
//                .questionIdentify(QuestionIdentify.SERVICEDATE)
//                .questionType(QuestionType.SELECT)
//                .category(map)
//                .build();
//
//        // then
//        mockMvc.perform(post("/api/v1/admin/form/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$.data").value(true));
//
//        }
//}