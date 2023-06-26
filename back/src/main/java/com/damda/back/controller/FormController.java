package com.damda.back.controller;

import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.request.FormStatusModifyRequestDTO;
import com.damda.back.data.request.ReservationFormRequestDTO;
import com.damda.back.data.request.SubmitRequestDTO;
import com.damda.back.service.ExcelService;
import com.damda.back.service.Impl.SubmitServiceImpl;
import com.damda.back.service.SubmitService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequiredArgsConstructor
public class FormController {

    private final SubmitService submitService;

    private final ExcelService excelService;


    @GetMapping("/api/v1/admin/submit/list")
    public ResponseEntity<CommonResponse<?>> adminSubmitListView(
            @RequestParam(required = false,defaultValue = "asc") String sort,
            @RequestParam(required = false,defaultValue = "0") Integer page,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate){


        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(submitService.submitTotalResponse(page,startDate,endDate,sort))
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



    }


    @GetMapping("/api/v1/zip/excel/download")
    public void downloadExcel2(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            HttpServletResponse response) throws IOException {


        Workbook workbook = excelService.createExcel(startDate, endDate);

        // 압축 파일을 생성하기 위한 ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);

        String fileName = formattedDate+"-LIST";
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

        // 엑셀 파일을 압축하여 ZipEntry로 추가
        ZipEntry zipEntry = new ZipEntry(encodedFileName+".xlsx");
        zipOutputStream.putNextEntry(zipEntry);
        workbook.write(zipOutputStream);

        // 스트림과 Workbook을 닫고 압축 파일 반환
        zipOutputStream.closeEntry();
        zipOutputStream.close();
        workbook.close();

        // ByteArray를 Response에 쓰기
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\""+encodedFileName+".zip\"");
        response.getOutputStream().write(byteArrayOutputStream.toByteArray());
        response.flushBuffer();
    }



    @PostMapping("/api/v1/user/form/submit")
    public ResponseEntity<CommonResponse<?>> reservationFormSave(
            @RequestBody SubmitRequestDTO dto,
            HttpServletRequest request){

        Integer id = Integer.parseInt(request.getAttribute("id").toString());
        submitService.jpaFormInsert(dto,id);

        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(true)
                .build();
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }

    @PutMapping("/api/v1/admin/submit/status")
    public ResponseEntity<CommonResponse<?>> statusModify(
            @RequestBody FormStatusModifyRequestDTO dto
    ){

        submitService.statusModify(dto);


        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(true)
                .build();
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }


    @PutMapping("/api/v1/status/completed/{id}")
    public ResponseEntity<CommonResponse<?>> payMentCompleted(@PathVariable Long id){
        submitService.payMentCompleted(id);


        CommonResponse<?> commonResponse = CommonResponse
                .builder()
                .codeEnum(CodeEnum.SUCCESS)
                .data(true)
                .build();
        return ResponseEntity
                .status(commonResponse.getStatus())
                .body(commonResponse);
    }


    @PutMapping("/api/v1/status/cancellation/{id}")
    public ResponseEntity<CommonResponse<?>> cancellation(@PathVariable Long id) {
        submitService.cancellation(id);

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
