package com.damda.back.controller;

import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.request.ManagerApplicationDTO;
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
     * @apiNote 
     */
    @GetMapping("/api/v1/admin/manager/active")
    public ResponseEntity<CommonResponse<?>> managerList() {

        CommonResponse<Object> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(managerService.managerResponseDTOList())
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);

    }


    /**
     * @apiNote : 매니저 추가
     * @param managerApplicationDTO
     * @return
     */
    // @GetMapping("/api/v1/member/manager/waitlist")

    // @GetMapping("/api/v1/member/manager/pending")

    // @GetMapping("/api/v1/member/manager/inactive")



    @PostMapping("/api/v1/manager/form/submit")
    public ResponseEntity<CommonResponse<?>> managerCreate(HttpServletRequest request,@RequestBody ManagerApplicationDTO managerApplicationDTO) {
//        System.out.println(request.getAttribute("id").toString());
        managerService.managerCreate(managerApplicationDTO,1);
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
