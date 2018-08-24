package com.pdfextract.common;

import lombok.Data;

@Data
public class Layout {
	
	private String tableDelimiter;
	private Section[] sections;
	private Integer rightStartX;
	private Integer leftStartX;
	private Integer startY;
	private Integer endY;
	private Double tableGap;
	private String stripperStrategy;
}
