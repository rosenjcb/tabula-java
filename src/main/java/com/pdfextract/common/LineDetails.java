package com.pdfextract.common;

import org.apache.pdfbox.pdmodel.font.PDFont;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LineDetails {
	private String line;
	private PDFont font;
	private float fontSize;
}
