package com.ufw.Utilities;

import java.io.File;
import java.io.FileInputStream;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.relevantcodes.extentreports.LogStatus;
import com.ufw.Base.MasterTestClass;

public class PDF extends MasterTestClass {

	public String getPDFtext(String strPath, int page)
	{
		String parsedText = "";
		try
		{
			File file = new File(strPath);
			FileInputStream fis =  new FileInputStream(file);
			PDFParser parser = new PDFParser((RandomAccessRead) fis);
//			PDFParser parser = new PDFParser(fis);
			
			parser.parse();
			COSDocument cosDoc = parser.getDocument();
			
			PDFTextStripper pdfStripper = new PDFTextStripper();
			PDDocument pdDoc = new PDDocument(cosDoc);
			
			pdfStripper.setStartPage(page);
			pdfStripper.setEndPage(page);
			
			parsedText = pdfStripper.getText(pdDoc);			
		}
		catch(Exception e)
		{
			testReport.get().log(LogStatus.FAIL, "FAILED :: to fetch the text from PDF" + e.getMessage());
		}
		
		return parsedText;
	}
	
	public String getPDFtext(String strPath, int startPage, int endPage)
	{
		String parsedText = "";
		try
		{
			File file = new File(strPath);
			FileInputStream fis =  new FileInputStream(file);
			PDFParser parser = new PDFParser((RandomAccessRead) fis);
//			PDFParser parser = new PDFParser(fis);
			
			parser.parse();
			
			COSDocument cosDoc = parser.getDocument();			
			PDFTextStripper pdfStripper = new PDFTextStripper();
			PDDocument pdDoc = new PDDocument(cosDoc);
			
			pdfStripper.setStartPage(startPage);
			pdfStripper.setEndPage(endPage);
			
			parsedText = pdfStripper.getText(pdDoc);			
		}
		catch(Exception e)
		{
			testReport.get().log(LogStatus.FAIL, "FAILED :: to fetch the text from PDF" + e.getMessage());
		}
		
		return parsedText;
	}
	
	public int getPageCount(String strPath)
	{
		int pageCount = 0;
		try
		{
			File file = new File(strPath);
			FileInputStream fis =  new FileInputStream(file);
			PDFParser parser = new PDFParser((RandomAccessRead) fis);
//			PDFParser parser = new PDFParser(fis);
			
			parser.parse();
			
			COSDocument cosDoc = parser.getDocument();			
			PDDocument pdDoc = new PDDocument(cosDoc);
			
			pageCount = pdDoc.getNumberOfPages();

			pdDoc.close();
			cosDoc.close();
			
		}
		catch(Exception e)
		{
			testReport.get().log(LogStatus.FAIL, "FAILED :: to fetch the text from PDF" + e.getMessage());
		}
		
		return pageCount;
	}
	
}
