package com.damda.back.controller;

import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.request.ManagerApplicationDTO;
import com.damda.back.data.request.ManagerRegionUpdateRequestDTO;
import com.damda.back.data.request.ManagerUpdateRequestDTO;
import com.damda.back.domain.manager.ManagerStatusEnum;
import com.damda.back.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    /**
     * API: 매니저 생성하기
     */
    @PostMapping("/api/v1/manager/form/submit")
    public ResponseEntity<CommonResponse<?>> managerCreate(HttpServletRequest request, @RequestBody ManagerApplicationDTO managerApplicationDTO) {

        managerService.managerCreate(managerApplicationDTO, Integer.parseInt(request.getAttribute("id").toString()));

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
     * API: 매니저 가져오기(상태별)
     */
    @GetMapping("/api/v1/admin/manager") // ../api/v1/admin/manager?status=ACTIVE
    public ResponseEntity<CommonResponse<?>> activeManagerList(@RequestParam String status) {

        CommonResponse<Object> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(managerService.managerResponseDTOList(ManagerStatusEnum.valueOf(status)))
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);

    }

    /**
     * API: 매니저 지원 폼 가져오기
     */
    @GetMapping("/api/v1/admin/manager/{id}")
    public ResponseEntity<CommonResponse<?>> managerApplicationForm(@PathVariable("id") Long managerId) {

        CommonResponse<Object> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(managerService.managerResponseDTO(managerId))
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);

    }

    /**
     * API: 매니저 정보 변경(활동지역 외)
     */
    @PostMapping("/api/v1/admin/manager/{id}/info")
    public ResponseEntity<CommonResponse<?>> managerUpdate(@PathVariable("id") Long managerId, @RequestBody ManagerUpdateRequestDTO managerUpdateRequestDTO) {

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(managerService.managerUpdate(managerUpdateRequestDTO, managerId))
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    /**
     * API: 매니저 활동지역 변경
     */
    @PostMapping("/api/v1/admin/manager/{id}/region")
    public ResponseEntity<CommonResponse<?>> managerRegionUpdate(@PathVariable("id") Long managerId, @RequestBody ManagerRegionUpdateRequestDTO managerRegionUpdateRequestDTO) {

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(managerService.managerRegionUpdate(managerRegionUpdateRequestDTO, managerId))
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

}
