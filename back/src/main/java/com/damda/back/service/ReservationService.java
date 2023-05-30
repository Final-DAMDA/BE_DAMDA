package com.damda.back.service;

import com.damda.back.data.request.AddCategoryRequestDTO;
import com.damda.back.data.request.FormModifyDTO;
import com.damda.back.data.request.RearrangeRequestDTO;
import com.damda.back.data.request.ReservationFormRequestDTO;
import com.damda.back.data.response.AdminFormResponseDTO;
import com.damda.back.data.response.ReservationResponseDTO;

import java.util.List;

public interface ReservationService {
    public void reservationForm(ReservationFormRequestDTO dto);

    public List<ReservationResponseDTO> reservationResponseDTOList();

    public List<AdminFormResponseDTO> adminFormDTOList();

    public void formModify(Long id, FormModifyDTO dto);

    public void reArrangeQuestion(List<RearrangeRequestDTO> dto);

    public void formDataDelete(Long id);

    public void formDataActivation(Long id);

    public void formAddCategory(Long id, AddCategoryRequestDTO dto);

    public void formDeleteCategory(Long id);

}
