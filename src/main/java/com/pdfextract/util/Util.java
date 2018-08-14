package com.pdfextract.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVPrinter;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.pdfextract.common.ExtractSections;
import com.pdfextract.common.Layout;

public class Util {

	public static List<String[]> extractFromPdfExtract(File pdfFile) {
		ExtractSections extractSections = new ExtractSections();
		Layout layout = ExtractSections.loadYaml("ccl_layout.yaml");

		try {
			return extractSections.convertToCsv(new FileInputStream(pdfFile), layout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<String[]> extractFromPdfExtract(PDDocument pdfDocument ) {
		ExtractSections extractSections = new ExtractSections();
		Layout layout = ExtractSections.loadYaml("ccl_layout.yaml");

		try {
			return extractSections.extractData(pdfDocument, layout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void write(CSVPrinter csvPrinter, Appendable out, List<String[]> columnData) {
		// TODO Auto-generated method stub
		for (String[] detail : columnData) {
			LinkedList<String> list1 = new LinkedList<>();
			list1.add("fileName");
			list1.addAll(Arrays.asList(detail));
			try {
				csvPrinter.printRecord(list1);
				csvPrinter.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
