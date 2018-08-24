package com.pdfextract.common;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.text.PDFTextStripper;

public abstract class TextStripper extends PDFTextStripper {
	
	public TextStripper() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	public abstract ArrayList<LineDetails> getData();
	public abstract void setLayout(Layout layout);
}
