package com.pdfextract.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdfextract.common.ExtractSections;
import com.pdfextract.common.Layout;

import technology.tabula.Table;

public class Util {

	public static String extractCsvFromPdfExtract(PDDocument pdfDocument, List<String> tables, String layoutStr) {
		ExtractSections extractSections = new ExtractSections();
		//Layout layout = ExtractSections.loadYaml("ccl_layout.yaml");
		try {
			ObjectMapper m = new ObjectMapper();
			Layout layout = m.readValue(layoutStr, Layout.class);

			List<String[]> ss = extractSections.extractData(pdfDocument, layout);

			try (StringWriter sw = new StringWriter();
		            BufferedWriter writer = new BufferedWriter(sw);
					CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);) {
					writeCsv(csvPrinter, "test file", ss, tables);
					return sw.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	    	return "";

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static void writeCsv(CSVPrinter csvPrinter, String fileName, List<String[]> list, List<String> tables) throws IOException {
		for (String[] detail : list) {
			LinkedList<String> list1 = new LinkedList<>();
			list1.add(fileName);
			list1.addAll(Arrays.asList(detail));
			csvPrinter.printRecord(list1);
		}
		csvPrinter.flush();
		
		for (String table : tables) {
			csvPrinter.printRecord(table);
		}
		csvPrinter.flush();

	}
}
