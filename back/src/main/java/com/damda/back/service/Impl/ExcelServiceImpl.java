package com.damda.back.service.Impl;

import com.damda.back.service.ExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelServiceImpl implements ExcelService {

    @Override
    public Workbook createExcel() {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("예약 리스트");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("");
        header.createCell(0).setCellValue("");
        header.createCell(0).setCellValue("");

        int rowNum = 1; //첫번쨰 줄부터 올라감

        //for문 돌릴예정


        return null;
    }
}
