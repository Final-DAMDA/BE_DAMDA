package com.damda.back.service;

import com.damda.back.data.common.ReservationStatus;
import com.damda.back.data.request.FormStatusModifyRequestDTO;
import com.damda.back.data.request.SubmitRequestDTO;
import com.damda.back.data.response.FormResultDTO;
import com.damda.back.data.response.SubmitTotalResponse;

public interface SubmitService {
    public Long saverFormSubmit(SubmitRequestDTO dto, Integer memberId);

    public FormResultDTO submitTotalResponse(int page,String startDate,String endDate);

    public Long jpaFormInsert(SubmitRequestDTO dto,Integer memberId);

    public void statusModify(FormStatusModifyRequestDTO dto);

    public void payMentCompleted(Long id);

    public void cancellation(Long id);

}
