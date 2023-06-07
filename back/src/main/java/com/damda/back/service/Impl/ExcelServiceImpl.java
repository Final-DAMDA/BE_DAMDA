package com.damda.back.service.Impl;

import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.domain.Match;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.repository.ReservationFormRepository;
import com.damda.back.service.ExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelServiceImpl implements ExcelService {

    private final ReservationFormRepository formRepository;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Workbook createExcel(String startDate,String endDate) {

        Timestamp startDateTimeStamp = startDate != null ? Timestamp.valueOf(startDate + " 00:00:00") : null;
        Timestamp endDateTimeStamp = endDate != null ? Timestamp.valueOf(endDate + " 23:59:59") : null;

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("예약 리스트");
        List<ReservationSubmitForm> submitFormList  = formRepository.formList(startDateTimeStamp,endDateTimeStamp);

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("신청일자");
        header.createCell(1).setCellValue("사용자 이름");
        header.createCell(2).setCellValue("연락처");
        header.createCell(3).setCellValue("주소");
        header.createCell(4).setCellValue("예약일자");
        header.createCell(5).setCellValue("가격");
        header.createCell(6).setCellValue("소요시간");
        header.createCell(7).setCellValue("매니저 인원");
        header.createCell(8).setCellValue("매니저 매칭");
        header.createCell(9).setCellValue("서비스 상태");
        header.createCell(10).setCellValue("결제상태");

        int rowNum = 1; //첫번쨰 줄부터 올라감

        //for문 돌릴예정
        for (ReservationSubmitForm submitForm : submitFormList) {
           Row row = sheet.createRow(rowNum++);

           List<Match> matches = submitForm.getMatches();
           List<ReservationAnswer> answers =  submitForm.getReservationAnswerList();
           Map<QuestionIdentify, String> answerMap
                    = answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));

            String managerNames= "";

           for (Match match : matches) {
                managerNames += match.getManagerName()+",";
           }

           row.createCell(0).setCellValue(submitForm.getCreatedAt().toString());
           row.createCell(1).setCellValue(submitForm.getMember().getUsername());
           row.createCell(2).setCellValue(answerMap.get(QuestionIdentify.APPLICANTCONACTINFO));
           row.createCell(3).setCellValue(answerMap.get(QuestionIdentify.ADDRESS));
           row.createCell(4).setCellValue(answerMap.get(QuestionIdentify.SERVICEDATE));
           row.createCell(5).setCellValue(submitForm.getTotalPrice());
           row.createCell(6).setCellValue(answerMap.get(QuestionIdentify.SERVICEDURATION));
           row.createCell(7).setCellValue(matches.size());
           row.createCell(8).setCellValue(managerNames);
           row.createCell(9).setCellValue(submitForm.getStatus().getMsg());
           row.createCell(10).setCellValue(submitForm.getPayMentStatus().getMsg());
        }
        sheet.setColumnWidth(8, 13000);
        sheet.setColumnWidth(9, 11000);


        return workbook;
    }
}
