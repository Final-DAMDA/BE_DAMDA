package com.damda.back.service;

import com.damda.back.data.request.SubmitRequestDTO;
import com.damda.back.data.response.SubmitTotalResponse;

public interface SubmitService {
    public boolean saverFormSubmit(SubmitRequestDTO dto, Integer memberId);

    public SubmitTotalResponse submitTotalResponse(int page);
}
