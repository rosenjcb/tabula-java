package com.pdfextract.common;

import lombok.Data;

@Data
public class Section {
	private String regex;
	private String name;
	private Boolean isTabular; 
}
