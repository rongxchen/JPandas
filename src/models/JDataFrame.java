package models;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class JDataFrame {
    private DataCell<?>[][] cells;

    public JDataFrame(DataCell<?>[][] cells) {
        if (cells == null) {
            throw new NullPointerException("data cells cannot be null");
        }
        this.cells = cells;
    }

    private int[] findColumnIndex(String[] columns) {
        int[] indexList = new int[columns.length];
        for (int i = 0; i < indexList.length; i++) {
            boolean found = false;
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[0][j].toString().equals(columns[i])) {
                    indexList[i] = j;
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new IllegalArgumentException("column not found: " + columns[i]);
            }
        }
        return indexList;
    }

    /** select columns from data frame
     * @param columns: an array of column names
     * @return
     */
    public JDataFrame selectColumns(String[] columns) {
        DataCell<?>[][] cells = new DataCell[this.cells.length][columns.length];
        int[] columnIndex = findColumnIndex(columns);
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < columns.length; j++) {
                cells[i][j] = new DataCell<>(this.cells[i][columnIndex[j]].getData());
            }
        }
        return new JDataFrame(cells);
    }


    /** select rows from data frame
     * @param rowBegin: beginning of rows (inclusive)
     * @param rowEnd: end of rows (exclusive)
     * @return
     */
    public JDataFrame selectRows(int rowBegin, int rowEnd) {
        if (rowBegin < 0 || rowBegin >= this.cells.length-1 || rowEnd < 0 || rowEnd >= this.cells.length-1) {
            throw new IllegalArgumentException(String.format("index out of range: [%d:%d], " + 
                "boundaries: [%d:%d]", rowBegin, rowEnd, 0, this.cells.length-1));
        }
        int length = rowEnd - rowBegin;
        DataCell<?>[][] cells = new DataCell[length+1][this.cells[0].length];
        for (int i = 0; i < this.cells[0].length; i++) {
            cells[0][i] = new DataCell<>(this.cells[0][i].getData());
        }
        for (int i = rowBegin; i < rowEnd; i++) {
            int currIndex = i-rowBegin+1;
            for (int j = 0; j < this.cells[i].length; j++) {
                cells[currIndex][j] = new DataCell<>(this.cells[i+1][j].getData());
            }
        }
        return new JDataFrame(cells);
    }


    /** using where to filter data
     * @param column
     * @return
     */
    public JDataFrame selectIf(JdfSelector selector) {
        if (selector == null) {
            throw new NullPointerException("selector cannot be null");
        }
        List<DataCell<?>[]> cells = new ArrayList<>();
        HashMap<String, Integer> columnIndex = new HashMap<>();
        DataCell<?>[] columns = new DataCell[this.cells[0].length];
        for (int i = 0; i < this.cells[0].length; i++) {
            columnIndex.put(this.cells[0][i].toString(), i);
            columns[i] = new DataCell<>(this.cells[0][i].getData());
        }
        cells.add(columns);
        for (int i = 1; i < this.cells.length; i++) {
            boolean found = true;
            for (SelectorModel<?> selectorModel : selector.getSelectorList()) {
                Object value = this.cells[i][columnIndex.get(selectorModel.getColumn())].getData();
                boolean fulfilled = JdfSelector.eval(selectorModel, value);
                if (!fulfilled) {
                    found = false;
                    break;
                }
            }
            if (found) {
                DataCell<?>[] row = new DataCell[this.cells[i].length];
                for (int j = 0; j < this.cells[i].length; j++) {
                    row[j] = new DataCell<>(this.cells[i][j].getData());
                }
                cells.add(row);
            }
        }
        return new JDataFrame(cells.toArray(new DataCell[cells.size()][]));
    }


    private int findMaxLength(int column) {
        int maxLength = 0;
        for (int i = 0; i < cells.length; i++) {
            maxLength = Math.max(maxLength, cells[i][column].toString().length());
        }
        return maxLength;
    }

    /** toString method to display data frame
     */
    public String toString() {
        int[] maxLengthList = new int[cells[0].length];
        for (int i = 0; i < maxLengthList.length; i++) {
            int maxLength = findMaxLength(i);
            maxLengthList[i] = maxLength;
        }
        StringBuilder builder = new StringBuilder();
        int indexMax = String.valueOf(cells.length).length();
        for (int i = 0; i < cells.length; i++) {
            builder.append(String.format("%-" + indexMax + "s  ", i));
            for (int j = 0; j < cells[i].length; j++) {
                String cellString = String.format("%-" + maxLengthList[j] + "s  ", cells[i][j]);
                builder.append(cellString);
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
