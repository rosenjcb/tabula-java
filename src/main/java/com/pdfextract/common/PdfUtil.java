package com.pdfextract.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfUtil {

	public static TextStripper extractPDF(PDDocument document, Layout layout) throws IOException {
		TextStripper stripper;
		try {
			System.out.println("***" + layout.getStripperStrategy());
			stripper = (TextStripper) Class.forName(layout.getStripperStrategy()).newInstance();
			stripper.setLayout(layout);

			stripper.setSortByPosition(true);
			stripper.setStartPage(0);
			stripper.setEndPage(document.getNumberOfPages());

			Writer writer1 = new OutputStreamWriter(new ByteArrayOutputStream());
			stripper.writeText(document, writer1);
			return stripper;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
