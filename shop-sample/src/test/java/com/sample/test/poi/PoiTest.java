package com.sample.test.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class PoiTest {

	public static void main(String[] args) throws IOException, InvalidFormatException {
		File file = new File("E:\\test.xlsx");
		// ExcelUtils.loadExcelFile(file);
		
		WorkbookFactory.create(new FileInputStream(file));
	}

}
