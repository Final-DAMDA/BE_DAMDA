package com.damda.back.repository.custom;

import com.damda.back.data.common.ReservationStatus;
import com.damda.back.data.response.Statistical;
import com.damda.back.domain.Match;
import com.damda.back.domain.Member;
import com.damda.back.domain.ReservationSubmitForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ReservationFormCustomRepository {

    /**
     * @apiNote 통계함수 키로 Status value로 몇개인지
     * */
    public Map<ReservationStatus, Long> statistical();

    public Page<ReservationSubmitForm> formPaging(Pageable pageable, Timestamp startDate, Timestamp endDate);

    public List<String> matches(List<Long> ids);

    public List<Long> ids(Long id);

    public List<ReservationSubmitForm> formList(Timestamp startDate,Timestamp endDate);

}
