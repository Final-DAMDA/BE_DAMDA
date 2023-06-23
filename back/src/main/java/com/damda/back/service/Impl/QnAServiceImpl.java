package com.damda.back.service.Impl;

import com.damda.back.data.common.QnACategory;
import com.damda.back.data.request.QnARequestDTO;
import com.damda.back.data.response.QnAResponseDTO;
import com.damda.back.domain.QnA;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.QnARepository;
import com.damda.back.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService {

    private final QnARepository qnARepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void createQnA(QnARequestDTO dto) {

        QnA qna = QnA.builder()
                .title(dto.getTitle())
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
    public void updateQnA(Long qnaId, QnARequestDTO dto) {

        QnA qnA = qnARepository.findById(qnaId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_QNA));

        qnA.updateQnA(dto);

    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteQnA(Long qnaId) {

        Optional<QnA> optional = qnARepository.findById(qnaId);

        if (optional.isPresent()) {
            qnARepository.deleteById(qnaId);
        } else {
            throw new CommonException(ErrorCode.NOT_FOUND_QNA);
        }
        
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public QnAResponseDTO qnaResponseDTO(Long qnaId) {

        Optional<QnA> optional = qnARepository.findById(qnaId);

        if (optional.isPresent()) {
            QnA qna = optional.get();

            QnAResponseDTO dto = QnAResponseDTO.builder()
                    .qnaId(qna.getId())
                    .title(qna.getTitle())
                    .qnaCategory(String.valueOf(qna.getQnACategory()))
                    .contents(qna.getContents())
                    .build();

            return dto;
        } else {
            throw new CommonException(ErrorCode.NOT_FOUND_MEMBER);
        }
    }

}
