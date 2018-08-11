package com.pdfextract.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class ExtractSectionsMain {

	static String[][] skipList = {};

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Layout layout = ExtractSections.loadYaml(args[0]);
		File folder = new File(args[1]);

		extractData(layout, folder, System.out);
	}

	private static void extractData(Layout layout, File folder, OutputStream out)
			throws FileNotFoundException, IOException {
		try (OutputStreamWriter osw = new OutputStreamWriter(out);
				BufferedWriter writer = new BufferedWriter(osw);
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);) {
			if (folder.isDirectory()) {
				for (final File fileEntry : folder.listFiles()) {
					if (fileEntry.isFile()) {
						writeCsv(layout, csvPrinter, fileEntry);
					}
				}
			} else {
				writeCsv(layout, csvPrinter, folder);
			}
		}
	}

	private static void writeCsv(Layout layout, CSVPrinter csvPrinter, final File fileEntry)
			throws FileNotFoundException, IOException {
		try (FileInputStream in = new FileInputStream(fileEntry)) {
			ExtractSections es = new ExtractSections();
			List<String[]> list = es.convertToCsv(in, layout);
			writeCsv(csvPrinter, fileEntry.getName(), list);
		}
	}

	public static void writeCsv(CSVPrinter csvPrinter, String fileName, List<String[]> list) throws IOException {

		for (String[] detail : list) {
			LinkedList<String> list1 = new LinkedList<>();
			list1.add(fileName);
			list1.addAll(Arrays.asList(detail));
			csvPrinter.printRecord(list1);
		}
		csvPrinter.flush();
	}
}
