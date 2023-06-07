//package com.damda.back.controller;
//
//import com.damda.back.config.SecurityConfig;
//import com.damda.back.data.common.QuestionIdentify;
//import com.damda.back.data.common.SubmitSlice;
//import com.damda.back.data.request.SubmitRequestDTO;
//import com.damda.back.service.Impl.SubmitServiceImpl;
//import com.damda.back.service.SubmitService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//
//@WebMvcTest(FormControllerTest.class)
//@Import(SecurityConfig.class )
//class FormControllerTest {
//
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private SubmitService userService;
//
//    @Test
//    @DisplayName("폼 제출 테스트")
//    @WithMockUser(username = "testuser", roles = "USER")
//    void form_submit_saver_test() throws Exception {
//        // given
//        SubmitSlice slice =  SubmitSlice.builder()
//                .questionNumber(1L)
//                .questionIdentify(QuestionIdentify.SERVICEDURATION)
//                .answer("하남시")
//                .build();
//
//        SubmitSlice slice2 =  SubmitSlice.builder()
//                .questionNumber(2L)
//                .questionIdentify(QuestionIdentify.SERVICEDURATION)
//                .answer("4시간")
//                .build();
//
//        SubmitRequestDTO dto = SubmitRequestDTO.builder()
//                .submit(List.of(slice,slice2))
//                .build();
//        // when
//
//        // then
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/form/submit")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(dto)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$.status").value(200))
//                .andExpect(jsonPath("$.data").value(true));
//    }
//}