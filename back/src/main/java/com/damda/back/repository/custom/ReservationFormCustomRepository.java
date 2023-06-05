package com.damda.back.repository.custom;

import com.damda.back.data.common.ReservationStatus;
import com.damda.back.data.response.Statistical;

import java.util.Map;

public interface ReservationFormCustomRepository {

    /**
     * @apiNote 통계함수 키로 Status value로 몇개인지
     * */
    public Map<ReservationStatus, Long> statistical();
}
