package com.damda.back.controller;

import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.request.QnARequestDTO;
import com.damda.back.data.response.QnAResponseDTO;
import com.damda.back.service.QnAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QnAController {

    private final QnAService qnAService;

    /**
     * API: QnA 등록하기
     */
    @PostMapping("/api/v1/admin/qna")
    public ResponseEntity<CommonResponse<?>> createQnA(@RequestBody QnARequestDTO dto) {

        qnAService.createQnA(dto);

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data("")
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);

    }

    /**
     * API: QnA List 가져오기
     */
    @GetMapping("/api/v1/member/qna")
    public ResponseEntity<CommonResponse<?>> qnaResponseDTOList(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                @RequestParam(name = "size", defaultValue = "4") int size) {

        Pageable pageable = PageRequest.of(page, size);

        List<QnAResponseDTO> qnaResponseDTOList = qnAService.qnaResponseDTOList();

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(qnaResponseDTOList)
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);

    }

    /**
     * API: QnA 가져오기
     */
    @GetMapping("/api/v1/member/qna/{id}")
    public ResponseEntity<CommonResponse<?>> qnaResponseDTO(@PathVariable("id") Long qnaId) {

        QnAResponseDTO qnaResponseDTO = qnAService.qnaResponseDTO(qnaId);

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(qnaResponseDTO)
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);

    }

    /**
     * API: QnA 수정하기
     */
    @PutMapping("/api/v1/admin/qna/{id}")
    public ResponseEntity<CommonResponse<?>> updateQnA(@PathVariable("id") Long qnaId, @RequestBody QnARequestDTO dto) {

        qnAService.updateQnA(qnaId, dto);

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(true)
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);

    }

    /**
     * API: QnA 삭제하기
     */
    @DeleteMapping("/api/v1/admin/qna/{id}")
    public ResponseEntity<CommonResponse<?>> deleteQnA(@PathVariable("id") Long qnaId) {

        qnAService.deleteQnA(qnaId);

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(true)
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
        
    }
}
