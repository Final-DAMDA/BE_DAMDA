package com.damda.back.data.response;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Statistical {
    private Long matching; // 유저의 예약을 ㅎ수락한 매니저들이 1명 이상 존재하는 상태
    private Long wating; // 예약하고 알림톡을 매니저들에게 보낸 상태
    private Long confirmation; //서비스 예약 확정 (어드민이 매니저들을 결정한 상태)
    private Long completed; //서비스 완료
    private Long cancellation; //예약 취소

    public void nullInit(){
        this.matching = this.matching != null ? this.matching : 0;
        this.wating = this.wating != null ? this.wating : 0;
        this.confirmation = this.confirmation != null ? this.confirmation : 0;
        this.completed = this.completed != null ? this.completed : 0;
        this.cancellation = this.cancellation != null ? this.cancellation : 0;
    }
}
