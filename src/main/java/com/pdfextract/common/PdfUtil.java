package com.pdfextract.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfUtil {

	public static TwoColumnTextStripper extractPDF(PDDocument document, Layout layout) throws IOException {
		TwoColumnTextStripper stripper = new TwoColumnTextStripper(layout);
		stripper.setSortByPosition(true);
		stripper.setStartPage(0);
		stripper.setEndPage(document.getNumberOfPages());

		Writer writer1 = new OutputStreamWriter(new ByteArrayOutputStream());
		stripper.writeText(document, writer1);
		return stripper;
	}

	public static SingleColumnTextStripper extractPDFWithSingleColumn(PDDocument document, Layout layout) throws IOException {
		SingleColumnTextStripper stripper = new SingleColumnTextStripper(layout);
		stripper.setSortByPosition(true);
		stripper.setStartPage(0);
		stripper.setEndPage(document.getNumberOfPages());

		Writer writer1 = new OutputStreamWriter(new ByteArrayOutputStream());
		stripper.writeText(document, writer1);
		return stripper;
	}
}
