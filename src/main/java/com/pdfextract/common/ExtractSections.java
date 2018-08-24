package com.pdfextract.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ExtractSections {

	public List<String[]> extractData(PDDocument document, Layout layout) throws IOException {
		Section[] sections = layout.getSections();
		List<LineDetails> data = null;

		TextStripper stripper = PdfUtil.extractPDF(document, layout);
		data = stripper.getData();

		ArrayList<String[]> data1 = new ArrayList<>();
		String[] eccnDetail = null;

		short flag = -2;

		for (LineDetails ln : data) {

			short locFlag = checkSectionChange(flag, sections, ln);
			if (locFlag != -1) {
				flag = locFlag;
			}
			if (flag == -2) {
				continue;
			}

			if (flag == 1 && locFlag == 1) {
				if (eccnDetail != null) {
					data1.add(eccnDetail);
				}
				eccnDetail = new String[sections.length];
			}

			if (eccnDetail != null) {
				String line = cleanse(
						ln.getLine().trim().equals("") ? "\n" : ln.getLine().trim().replaceAll("\"", "\'"));

				for (short i = 1; i <= sections.length; i++) {
					if (flag == i) {
						String summary = eccnDetail[i - 1];
						String summary1 = summary != null ? summary : "";
						eccnDetail[i - 1] = summary1 + "\n" + line;
						break;
					}
				}

			}
		}
		if (eccnDetail != null) {
			data1.add(eccnDetail);
		}
		return data1;
	}

	private short checkSectionChange(short flag, Section[] sections, LineDetails ln) {

		if ((flag == -2 || flag == sections.length || flag == 1) && patternMatch(sections[0].getRegex(), ln)) {
			return 1;
		}
		for (short i = 1; i <= sections.length; i++) {
			if (i < 2 && (flag == 1 && patternMatch(sections[1].getRegex(), ln))) {
				// System.out.println(" secondSection=" + secondSection + "
				// Actualline=" + line);
				return 2;
			} else if ((flag == (i - 2) || flag == (i - 1)) && patternMatch(sections[i - 1].getRegex(), ln)) {
				// System.out.println(" thirdSection=" + thirdSection + "
				// Actualline=" + line);
				return i;
			}
		}

		return -1;
	}

	private boolean patternMatch(String pattern, LineDetails ln) {
		// TODO Auto-generated method stub
		String line = ln.getLine();
		String[] split = pattern.split("#####");
		if (split.length == 1) {
			return Pattern.matches(pattern, line);
		} else {
			boolean a = Pattern.matches(split[0], line);
			if (ln.getFont() != null) {
				boolean b = ln.getFont().getName().contains(split[1]);
				return a && b;
			}
		}
		return false;
	}

	private static String cleanse(String line) {
		char[] arr = line.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			char c = arr[i];
			int ic = (int) c;

			if (ic == 8220 || ic == 8221) {
				arr[i] = '\'';
			}
		}
		return new String(arr);
	}

	public static String extractAttributes(String phrase, String pattern) {
		Pattern r = Pattern.compile(pattern);

		Matcher matcher = r.matcher(phrase);
		if (matcher.find()) {
			return matcher.group();
		}
		return "";
	}

	public static Layout loadYaml(String file) {
		Layout layout = null;
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

		try (InputStream in = new FileInputStream(file)) {
			layout = mapper.readValue(in, Layout.class);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return layout;
	}
}
