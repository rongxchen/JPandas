import models.DataCell;
import models.JDataFrame;
import readers.CsvReader;

public class JPandas {
    private JPandas() {}

    public static JDataFrame readCsv(String path) {
        DataCell<?>[][] cells = CsvReader.read(path);
        return new JDataFrame(cells);
    }
}
