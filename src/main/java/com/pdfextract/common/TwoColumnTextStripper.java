package com.pdfextract.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import lombok.Getter;

public class TwoColumnTextStripper extends PDFTextStripper {

	/*private final int rightStartX = 320;
	private final int startY = 90;
	private final int endY = 723;

	private final float tableGap = 17f;

	public static final String tableDelimiter = "#####";*/

	@Getter
	private ArrayList<LineDetails> data = new ArrayList<>();

	private ArrayList<LineDetails> pageLeftData = new ArrayList<>();
	private ArrayList<LineDetails> pageRightData = new ArrayList<>();
	private Layout layout;

	public TwoColumnTextStripper(Layout layout) throws IOException {
		super();
		this.layout = layout;
	}

	@Override
	protected void endPage(PDPage page) throws IOException {
		super.endPage(page);

		data.addAll(pageLeftData);
		data.addAll(pageRightData);

		pageLeftData.clear();
		pageRightData.clear();
	}

	@Override
	protected void writeString(String line, List<TextPosition> textPositions) throws IOException {
		extractString(textPositions);
		//System.out.println(line);
	}

	private void extractString(List<TextPosition> textPositions) {
		StringBuffer leftBit = new StringBuffer();
		StringBuffer rightBit = new StringBuffer();
		PDFont currentLeftFont = null;
		PDFont currentRightFont = null;
		float currentLeftFontSize = 0;
		float currentRightFontSize = 0;

		boolean leftStart = false;
		boolean rightStart = false;
		float prevX = 0f;
		for (TextPosition textPosition : textPositions) {
			if (textPosition.getY() > layout.getStartY() && textPosition.getY() < layout.getEndY()) {
				if (textPosition.getX() < layout.getRightStartX()) {
					leftBit.append(addDelimIfNeeded(textPosition.getUnicode(), leftBit.toString(), prevX, textPosition.getX()));
					if (!leftStart) {
						leftStart = true;
						currentLeftFont = textPosition.getFont();
						currentLeftFontSize = textPosition.getFontSize();
					}
				} else {
					rightBit.append(addDelimIfNeeded(textPosition.getUnicode(), rightBit.toString(), prevX, textPosition.getX()));
					if (!rightStart) {
						rightStart = true;
						currentRightFont = textPosition.getFont();
						currentRightFontSize = textPosition.getFontSize();
					}
				}
				prevX = textPosition.getX();
			}
		}

		//System.out.print(leftBit.toString().trim() + "***************" + rightBit.toString().trim() + "***************" );
		pageLeftData.add(LineDetails.builder().line(leftBit.toString().trim()).font(currentLeftFont)
				.fontSize(currentLeftFontSize).build());
		pageRightData.add(LineDetails.builder().line(rightBit.toString().trim()).font(currentRightFont)
				.fontSize(currentRightFontSize).build());
	}

	private Object addDelimIfNeeded(String unicode, String actualData, float prevX, float x) {
		// System.out.println(unicode + "***" + prevX + "***" + x);
		if (prevX > 0) {
			float gap = x - prevX;
			if (gap > layout.getTableGap() && !actualData.equals("")) {
				//return tableDelimiter + unicode;
				return "" + unicode;
			}
		}
		return unicode;
	}
}
