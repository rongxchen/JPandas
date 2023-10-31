package models;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataCell<T> {
    private T data;

    public DataCell(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
        // return "DataCell [data=" + data + ", class=" + data.getClass().getName() + "]";
    }

    public static DataCell<?> autoConvert(String data, boolean doubleFirst) {
        if ("".equals(data.trim())) {
            return new DataCell<String>("null");
        }
        String[] convertMethods = new String[]{"convertInteger", "convertDouble", "convertLong", "convertBoolean", "convertDate"};
        DataCell<?> dataCell = new DataCell<>(null);
        for (String convertMethod : convertMethods) {
            if (doubleFirst && "convertInteger".equals(convertMethod)) continue;
            try {
                Method method = DataCell.class.getMethod(convertMethod, String.class);
                Object result = method.invoke(dataCell, data);
                if (result != null) {
                    return (DataCell<?>) (result);
                }
            } catch (Exception e) {}
        }
        return new DataCell<String>(data);
    }

    public static DataCell<?> autoConvert(String data) {
        return autoConvert(data, false);
    }

    public static DataCell<Double> convertDouble(String data) {
        try {
            double doubleData = Double.parseDouble(data);
            return new DataCell<Double>(doubleData);
        } catch (Exception e) {}
        return null;
    }

    public static DataCell<Integer> convertInteger(String data) {
        try {
            int intData = Integer.parseInt(data);
            return new DataCell<Integer>(intData);
        } catch (Exception e) {}
        return null;
    }

    public static DataCell<Long> convertLong(String data) {
        try {
            long longData = Long.parseLong(data);
            return new DataCell<Long>(longData);
        } catch (Exception e) {}
        return null;
    }

    public static DataCell<Boolean> convertBoolean(String data) {
        Boolean booleanData = null;
        if ("true".equals(data) || "True".equals(data)) {
            booleanData = true;
        } else if ("false".equals(data) || "False".equals(data)) {
            booleanData = false;
        }
        if (booleanData != null) {
            return new DataCell<Boolean>(booleanData);
        }
        return null;
    }

    public static DataCell<LocalDate> convertDate(String data) {
        String[] patterns = {"yyyy-MM-dd", "yyyy/MM/dd"};
        for (String pattern : patterns) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            try {
                LocalDate parsedDate = LocalDate.parse(data, dateTimeFormatter);
                return new DataCell<LocalDate>(parsedDate);
            } catch (Exception e) {}
        }
        return null;
    }
}
