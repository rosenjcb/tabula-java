package com.pdfextract.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdfextract.common.ExtractSections;
import com.pdfextract.common.Layout;
import com.pdfextract.common.Section;

import lombok.val;

public class Util {

	public static String extractCsvFromPdfExtract(PDDocument pdfDocument, List<String> tables, String layoutStr) {
		// Layout layout = ExtractSections.loadYaml("ccl_layout.yaml");
		try (StringWriter sw = new StringWriter();
				BufferedWriter writer = new BufferedWriter(sw);
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);) {
			csvData(pdfDocument, tables, layoutStr, csvPrinter);
			// writeCsv(csvPrinter, "test file", ss, tables);
			return sw.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	public static String extractColumnData(PDDocument pdfDocument, List<String> tables, String layoutStr) {
		JSONArray arr = new JSONArray();
		JSONObject root = new JSONObject();
		arr.add(root);
		root.put("extraction_method", "lattice");
		root.put("top", "506.75195");
		root.put("left", "72.06707");
		root.put("width", "221.339080810547");
		root.put("height", "64.64892578125");
		JSONArray data = jsonData(pdfDocument, tables, layoutStr);

		root.put("data", data);
		root.put("spec_index", "0");
		return arr.toJSONString();
	}

	private static String csvData(PDDocument pdfDocument, List<String> tables, String layoutStr,
			CSVPrinter csvPrinter) {
		try {
			ExtractSections extractSections = new ExtractSections();
			ObjectMapper m = new ObjectMapper();
			Layout layout = m.readValue(layoutStr, Layout.class);

			val arrItem = new ArrayList<String>();

			val sections = layout.getSections();

			for (Section columnHeader : sections) {
				arrItem.add(columnHeader.getName());
			}

			csvPrinter.printRecord(arrItem);

			val ss = extractSections.extractData(pdfDocument, layout);
			System.out.println("ss.size()=" + ss.size() + "tables.size()=" + tables.size());

			for (int i = 0; i < ss.size(); i++) {
				val arrItem1 = new ArrayList<String>();
				String[] row = ss.get(i);
				for (int j = 0; j < row.length; j++) {
					Section s = sections[j];
					if (!s.getIsTabular()) {
						arrItem1.add(row[j]);
					} else {
						if (i < tables.size()) {
							JSONParser parser = new JSONParser();
							JSONObject item1 = (JSONObject) parser.parse(tables.get(i) + "}");

							arrItem1.add(getJsonDataString((JSONArray) item1.get("data")));
						} else {
							arrItem1.add("data");
						}
					}
				}
				csvPrinter.printRecord(arrItem1);
			}
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static JSONArray jsonData(PDDocument pdfDocument, List<String> tables, String layoutStr) {
		try {
			ExtractSections extractSections = new ExtractSections();
			ObjectMapper m = new ObjectMapper();
			Layout layout = m.readValue(layoutStr, Layout.class);

			JSONArray data = new JSONArray();

			JSONArray arrItem = new JSONArray();

			Section[] sections = layout.getSections();

			for (Section columnHeader : sections) {
				JSONObject header = new JSONObject();
				header.put("top", "");
				header.put("left", "");
				header.put("width", "");
				header.put("height", "");
				header.put("text", columnHeader.getName());
				arrItem.add(header);
			}

			data.add(arrItem);

			List<String[]> ss = extractSections.extractData(pdfDocument, layout);
			System.out.println("ss.size()=" + ss.size() + "tables.size()=" + tables.size());

			for (int i = 0; i < ss.size(); i++) {
				JSONArray arrItem1 = new JSONArray();
				String[] row = ss.get(i);
				for (int j = 0; j < row.length; j++) {
					Section s = sections[j];
					JSONObject item = new JSONObject();
					item.put("top", "");
					item.put("left", "");
					item.put("width", "");
					item.put("height", "");
					item.put("text", row[j]);
					if (!s.getIsTabular()) {
						arrItem1.add(item);
					} else {
						if (i < tables.size()) {
							JSONParser parser = new JSONParser();
							JSONObject item1 = (JSONObject) parser.parse(tables.get(i) + "}");

							item.put("text", getJsonDataString((JSONArray) item1.get("data")));
						} else {
							item.put("text", "data");
						}
						arrItem1.add(item);
					}
				}
				data.add(arrItem1);
			}

			return data;
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static String getJsonDataString(JSONArray jsonArray) {
		// TODO Auto-generated method stub
		JSONArray tempArray = new JSONArray();
		int i = 0;
		for (Object obj : jsonArray) {
			JSONArray jArr = (JSONArray) obj;
			JSONArray jArr1 = new JSONArray();

			if (i > 0) {
				for (Object obj1 : jArr) {
					JSONObject jObj1 = (JSONObject) obj1;
					jArr1.add(jObj1.get("text"));
				}
				tempArray.add(jArr1);
			}
			i++;
		}

		return tempArray.toJSONString();
	}

	private static void writeCsv(CSVPrinter csvPrinter, String fileName, List<String[]> list, List<String> tables)
			throws IOException {
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
