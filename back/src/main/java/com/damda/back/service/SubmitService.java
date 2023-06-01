package com.damda.back.service;

import com.damda.back.data.request.SubmitRequestDTO;

public interface SubmitService {
    public boolean saverFormSubmit(SubmitRequestDTO dto, Integer memberId);
}
