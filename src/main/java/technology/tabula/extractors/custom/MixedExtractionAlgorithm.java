package technology.tabula.extractors.custom;

import java.util.List;

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
	
	public MixedExtractionAlgorithm(SpreadsheetExtractionAlgorithm spreadsheetExtractionAlgorithm){
		this.spreadsheetExtractionAlgorithm = spreadsheetExtractionAlgorithm;
	}

	@Override
	public List<Table> extract(Page page) {
		// TODO Auto-generated method stub
		return spreadsheetExtractionAlgorithm.extract(page);
	}
}
