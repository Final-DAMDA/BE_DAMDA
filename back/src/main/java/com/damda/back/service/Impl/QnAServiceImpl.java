package com.damda.back.service.Impl;

import com.damda.back.data.common.QnACategory;
import com.damda.back.data.request.QnARequestDTO;
import com.damda.back.data.response.QnAResponseDTO;
import com.damda.back.domain.QnA;
import com.damda.back.repository.QnARepository;
import com.damda.back.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService {

    private final QnARepository qnARepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void createQnA(QnARequestDTO dto) {

        QnA qna = QnA.builder()
                .title(dto.getQnaTitle())
                .qnACategory(QnACategory.valueOf(dto.getQnaCategory()))
                .contents(dto.getContents())
                .build();

        qnARepository.save(qna);

    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public List<QnAResponseDTO> qnaResponseDTOList() {

        List<QnAResponseDTO> qnaResponseDTOList = new ArrayList<>();

        qnARepository.findQnA().forEach(qnA -> {

            QnAResponseDTO dto = QnAResponseDTO.builder()
                    .qnaId(qnA.getId())
                    .title(qnA.getTitle())
                    .qnaCategory(String.valueOf(qnA.getQnACategory()))
                    .contents(qnA.getContents())
                    .build();

            qnaResponseDTOList.add(dto);

        });

        return qnaResponseDTOList;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateQnA(QnARequestDTO dto) {

        QnA qna = QnA.builder()
                .title(dto.getQnaTitle())
                .qnACategory(QnACategory.valueOf(dto.getQnaCategory()))
                .contents(dto.getContents())
                .build();

        qnARepository.save(qna);

    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteQnA(Long qnaId) {

        qnARepository.deleteById(qnaId);
        
    }

}
