package technology.tabula.extractors.custom;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.pdfextract.common.ExtractSectionsMain;

import lombok.Setter;
import technology.tabula.Page;
import technology.tabula.Table;
import technology.tabula.extractors.ExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

/**
 * @author manuel
 *
 */
public class MixedExtractionAlgorithm implements ExtractionAlgorithm {
	
	private SpreadsheetExtractionAlgorithm spreadsheetExtractionAlgorithm;
	
	@Setter
	private List<String[]> columnData;
	
	public MixedExtractionAlgorithm(SpreadsheetExtractionAlgorithm spreadsheetExtractionAlgorithm){
		this.spreadsheetExtractionAlgorithm = spreadsheetExtractionAlgorithm;
	}

	@Override
	public List<Table> extract(Page page) {
		return spreadsheetExtractionAlgorithm.extract(page);
	}
}
