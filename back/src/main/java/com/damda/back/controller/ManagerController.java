package com.damda.back.controller;

import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.request.ManagerApplicationDTO;
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
    
    @PostMapping("/api/v1/manager/form/submit")
    public ResponseEntity<CommonResponse<?>> managerCreate(HttpServletRequest request, @RequestBody ManagerApplicationDTO managerApplicationDTO) {
        //        System.out.println(request.getAttribute("id").toString());
        managerService.managerCreate(managerApplicationDTO, 1);
        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data("")
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

}
