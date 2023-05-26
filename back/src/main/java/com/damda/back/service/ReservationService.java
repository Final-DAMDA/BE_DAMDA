package com.damda.back.service;

import com.damda.back.data.request.ReservationFormRequestDTO;
import com.damda.back.data.response.AdminFormResponseDTO;
import com.damda.back.data.response.ReservationResponseDTO;

import java.util.List;

public interface ReservationService {
    public void reservationForm(ReservationFormRequestDTO dto);

    public List<ReservationResponseDTO> reservationResponseDTOList();

    public List<AdminFormResponseDTO> adminFormDTOList();

}
