package readers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import models.DataCell;

public class CsvReader {
    public static DataCell<?>[][] read(String filepath) {
        try {
            List<DataCell<?>[]> dataCellList = new ArrayList<>();
            Scanner sc = new Scanner(new File(filepath));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] items = line.split(",", -1);
                DataCell<?>[] row = new DataCell[items.length];
                for (int i = 0; i < items.length; i++) {
                    row[i] = DataCell.autoConvert(items[i]);
                }
                dataCellList.add(row);
            }
            return dataCellList.toArray(new DataCell<?>[dataCellList.size()][]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}