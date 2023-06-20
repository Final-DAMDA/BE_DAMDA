package com.damda.back.service;

import com.damda.back.data.request.QnARequestDTO;
import com.damda.back.data.response.QnAResponseDTO;

import java.util.List;

public interface QnAService {
    void createQnA(QnARequestDTO dto);

    List<QnAResponseDTO> qnaResponseDTOList();

    void updateQnA(QnARequestDTO dto);

    void deleteQnA(Long qnaId);
}
