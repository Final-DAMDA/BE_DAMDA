package com.damda.back.controller;

import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.request.ReservationFormRequestDTO;
import com.damda.back.data.request.SubmitRequestDTO;
import com.damda.back.service.ExcelService;
import com.damda.back.service.Impl.SubmitServiceImpl;
import com.damda.back.service.SubmitService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class FormController {

    private final SubmitService submitService;

    private final ExcelService excelService;


    @GetMapping("/api/v1/admin/submit/list")
    public ResponseEntity<CommonResponse<?>> adminSubmitListView(
            @RequestParam(required = false,defaultValue = "0") Integer page,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate){


        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(submitService.submitTotalResponse(page,startDate,endDate))
                .build();

        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    @GetMapping("/api/v1/excel/download")
    public void downloadExcel(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            HttpServletResponse response) throws IOException {

        Workbook workbook = excelService.createExcel(startDate, endDate);
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);

        String fileName = formattedDate+"-예약폼리스트.xlsx";
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);


        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename="+encodedFileName+".xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();


        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(true)
                .build();

    }

    @PostMapping("/api/v1/user/form/submit")
    public ResponseEntity<CommonResponse<?>> reservationFormSave(
            @RequestBody SubmitRequestDTO dto,
            HttpServletRequest request){
        Integer id = Integer.parseInt(request.getAttribute("id").toString());
        submitService.saverFormSubmit(dto,id);

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
