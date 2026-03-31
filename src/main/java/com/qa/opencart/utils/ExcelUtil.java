package com.qa.opencart.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

//config-->only for config/env:url,browser,user/pwd,headless=true
//test data--> data provider,excel/csv
//constants:AppConstants


public class ExcelUtil {
	
	private static final String TEST_DATA_SHEET_PATH="./src/test/resources/testdata/OpenCartTestData.xlsx";
	private static Workbook book;
	private static Sheet sheet;

	public static Object[][] getTestData(String sheetName) {
		Object data[][]=null;
		try {
			FileInputStream ip=new FileInputStream(TEST_DATA_SHEET_PATH);
			book=WorkbookFactory.create(ip);//load file
			book.getSheet(sheetName);
			
			//Object data[][]=new Object[5][3];//5 columns 2 rows
			data=new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];
			
			for(int i=0;i<sheet.getLastRowNum();i++) {
				for(int j=0;j<sheet.getRow(0).getLastCellNum();j++) {
					data[i][j]=sheet.getRow(i+1).getCell(j).toString();
				}
			}
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch (InvalidFormatException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}


}

