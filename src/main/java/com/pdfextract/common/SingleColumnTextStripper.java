package com.pdfextract.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.TextPosition;

import lombok.Getter;
import lombok.Setter;

public class SingleColumnTextStripper extends TextStripper {

	/*private final int rightStartX = 640;
	private final int startY = 100;
	private final int endY = 730;

	private final float tableGap = 10f;

	public static final String tableDelimiter = "#####";*/
	
	@Setter
	private Layout layout;

	@Getter
	private ArrayList<LineDetails> data = new ArrayList<>();

	private ArrayList<LineDetails> pageLeftData = new ArrayList<>();

	public SingleColumnTextStripper() throws IOException {
		super();
	}

	@Override
	protected void endPage(PDPage page) throws IOException {
		super.endPage(page);

		data.addAll(pageLeftData);

		pageLeftData.clear();
	}

	@Override
	protected void writeString(String line, List<TextPosition> textPositions) throws IOException {
		extractString(textPositions);
		//System.out.println(line);
	}

	private void extractString(List<TextPosition> textPositions) {
		StringBuffer leftBit = new StringBuffer();
		PDFont currentLeftFont = null;
		float currentLeftFontSize = 0;

		boolean leftStart = false;
		float prevX = 0f;

		for (TextPosition textPosition : textPositions) {
			if (textPosition.getY() > layout.getStartY() && textPosition.getY() < layout.getEndY()) {
				if(textPosition.getX() > (layout.getLeftStartX() + 200) ){
					if(leftBit.toString().equals("")){
						leftBit.append(layout.getTableDelimiter());
					}
				}
				if (textPosition.getX() < layout.getRightStartX()) {
					leftBit.append(addDelimIfNeeded(textPosition.getUnicode(), leftBit.toString(), prevX, textPosition.getX()));
					if (!leftStart) {
						leftStart = true;
						currentLeftFont = textPosition.getFont();
						currentLeftFontSize = textPosition.getFontSize();
					}
				} 
				prevX = textPosition.getX();
			}
		}

		//System.out.print(leftBit.toString().trim() + "***************" + rightBit.toString().trim() + "***************" );
		pageLeftData.add(LineDetails.builder().line(leftBit.toString().trim()).font(currentLeftFont)
				.fontSize(currentLeftFontSize).build());
	}

	private Object addDelimIfNeeded(String unicode, String actualData, float prevX, float x) {
		// System.out.println(unicode + "***" + prevX + "***" + x);
		if (prevX > 0) {
			float gap = x - prevX;
			if (gap > layout.getTableGap() && !actualData.equals("")) {
				return layout.getTableDelimiter() + unicode;
				//return "" + unicode;
			}
		}
		return unicode;
	}
}
