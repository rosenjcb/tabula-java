package technology.tabula;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.pdfextract.util.Util;

public class ObjectExtractor {

    private final PDDocument pdfDocument;

    public ObjectExtractor(PDDocument pdfDocument) {
    	System.out.println("****ObjectExtractor****");
        this.pdfDocument = pdfDocument;
    }

    protected Page extractPage(Integer pageNumber) throws IOException {

        if (pageNumber > this.pdfDocument.getNumberOfPages() || pageNumber < 1) {
            throw new java.lang.IndexOutOfBoundsException(
                    "Page number does not exist");
        }

        PDPage p = this.pdfDocument.getPage(pageNumber - 1);

        ObjectExtractorStreamEngine se = new ObjectExtractorStreamEngine(p);
        se.processPage(p);


        TextStripper pdfTextStripper = new TextStripper(this.pdfDocument, pageNumber);

        pdfTextStripper.process();

        Utils.sort(pdfTextStripper.textElements, Rectangle.ILL_DEFINED_ORDER);

        float w, h;
        int pageRotation = p.getRotation();
        if (Math.abs(pageRotation) == 90 || Math.abs(pageRotation) == 270) {
            w = p.getCropBox().getHeight();
            h = p.getCropBox().getWidth();
        } else {
            w = p.getCropBox().getWidth();
            h = p.getCropBox().getHeight();
        }

        return new Page(0, 0, w, h, pageRotation, pageNumber, p, pdfTextStripper.textElements,
                se.rulings, pdfTextStripper.minCharWidth, pdfTextStripper.minCharHeight, pdfTextStripper.spatialIndex);
    }

    public PageIterator extract(Iterable<Integer> pages) {
        return new PageIterator(this, pages);
    }

    public PageIterator extract() {
        return extract(Utils.range(1, this.pdfDocument.getNumberOfPages() + 1));
    }

    public Page extract(int pageNumber) {
        return extract(Utils.range(pageNumber, pageNumber + 1)).next();
    }

    public void close() throws IOException {
        this.pdfDocument.close();
    }

    public String extractCsv(List<String> tables, String layout, int specIndex){
        System.out.println("&&***********layout***************&&" + layout);
        return Util.extractCsvFromPdfExtract(pdfDocument, tables, layout);
    }
    
    public String extractJson(List<String> tables, String layout){
        System.out.println("&&***********layout***************&&" + layout);
        /*# start JSON array
        res.write  "["
        tables.each_with_index do |table, index|
          res.write ", " if index > 0
          res.write table.to_json[0...-1] + ", \"spec_index\": #{table.spec_index}}"
        end

        # end JSON array
        res.write "]"*/
        StringBuffer sb = new StringBuffer();
        
        sb.append("[");
        
        int index = 0;
        for(String table : tables){
        	if(index > 0){
        		sb.append(", ");
        	}
        	
        	sb.append(table + ", \"spec_index\": " + 0 + "}");
        	index++;
        }
        sb.append("]");
        return sb.toString();

        //return Util.extractJsonFromPdfExtract(pdfDocument, tables, layout);
    }

}
