package org.nerdpower.tabula.writers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import org.nerdpower.tabula.RectangularTextContainer;
import org.nerdpower.tabula.Table;

public class CSVWriter implements Writer {
    
    CSVPrinter printer;
    
    void createWriter(Appendable out) {
        this.printer = new CSVPrinter(out, CSVFormat.EXCEL);
    }
    
    @Override
    public void write(Appendable out, Table table) throws IOException {
        this.createWriter(out);
        for (List<RectangularTextContainer> row: table.getRows()) {
            List<String> cells = new ArrayList<String>(row.size());
            for (RectangularTextContainer tc: row) {
                cells.add(tc.getText());
            }
            this.printer.printRecord(cells);
        }
        printer.flush();
    }

}