package com.damda.back.data.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description 고객에 예약을 완료한 후 고객에게 확인차 보내는 알림톡
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTalkDTO {

        private String reservationTime; //서비스 일시

        private String reservationAddress; //서비스 장소

        private String managerAmount; //매니저 일원

        private String requiredTime; //예상 소요시간

        private String estimate; //견적가
}
