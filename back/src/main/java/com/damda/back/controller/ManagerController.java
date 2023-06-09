package com.damda.back.controller;

import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.common.RegionModify;
import com.damda.back.data.request.ManagerApplicationDTO;
import com.damda.back.data.request.ManagerRegionUpdateRequestDTO;
import com.damda.back.data.request.ManagerStatusUpdateRequestDTO;
import com.damda.back.data.request.ManagerUpdateRequestDTO;
import com.damda.back.data.response.PageManagerResponseDTO;
import com.damda.back.domain.manager.ManagerStatusEnum;
import com.damda.back.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
                .data(true)
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    /**
     * API: 매니저 가져오기(상태별)
     */
    @GetMapping("/api/v1/admin/manager") // ../api/v1/admin/manager?status=ACTIVE
    public ResponseEntity<CommonResponse<?>> activeManagerList(@RequestParam(required = false) ManagerStatusEnum status,
                                                               @RequestParam(name = "page", defaultValue = "0") int page,
                                                               @RequestParam(name = "size", defaultValue = "7") int size) {

        Pageable pageable = PageRequest.of(page, size);

        PageManagerResponseDTO pageManagerResponseDTO = managerService.managerResponseDTOList(status, pageable);
        CommonResponse<Object> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(pageManagerResponseDTO)
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
    @PutMapping("/api/v1/admin/manager/{id}/info")
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
     * API: 매니저 활동지역 추가
     */
    @PutMapping("/api/v1/admin/region/add/{id}")
    public ResponseEntity<CommonResponse<?>> managerRegionAdd(@PathVariable("id") Long managerId, @RequestBody Map<RegionModify, String> region) {
        managerService.activityRegionADD(managerId, region);
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
     * API: 매니저 활동지역 삭제
     */
    @PutMapping("/api/v1/admin/region/delete/{id}")
    public ResponseEntity<CommonResponse<?>> managerRegionDelete(@PathVariable("id") Long managerId, @RequestBody Map<RegionModify, String> region) {
        managerService.activityRegionDelete(managerId, region);
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
     * API: 매니저 상태 변경
     */
    @PutMapping("/api/v1/admin/manager/{id}/status")
    public ResponseEntity<CommonResponse<?>> managerStatusUpdate(@PathVariable("id") Long managerId, @RequestBody ManagerStatusUpdateRequestDTO dto) {

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(managerService.managerStatusUpdate(dto, managerId))
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

}
