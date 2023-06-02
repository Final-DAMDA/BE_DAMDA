package com.damda.back;

import com.damda.back.domain.area.DistrictEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackApplicationTests {

	@Test
	void contextLoads() throws JsonProcessingException {
		String s = new ObjectMapper().writeValueAsString(DistrictEnum.GAPYEONG_GUN.getParentCode());
		System.out.println("s = " + s);
	}
	
}
