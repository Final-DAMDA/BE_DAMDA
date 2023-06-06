package com.damda.back.service;

import org.apache.poi.ss.usermodel.Workbook;

import java.sql.Timestamp;

public interface ExcelService {

    public Workbook createExcel(String startDate, String endDate);
}
