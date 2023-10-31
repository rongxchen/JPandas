package models;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class JdfSelector {
    private List<SelectorModel<?>> selectorList = new ArrayList<>();

    public static enum Operator {
        EQUAL, LESS, LESS_EQUAL, GREATER, GREATER_EQUAL
    }

    public JdfSelector() {}

    public JdfSelector equal(String column, Object value) {
        selectorList.add(new SelectorModel<>(column, Operator.EQUAL, value));
        return this;
    }

    public JdfSelector lessThan(String column, Object value) {
        selectorList.add(new SelectorModel<>(column, Operator.LESS, value));
        return this;
    }

    public JdfSelector lessThanOrEqual(String column, Object value) {
        selectorList.add(new SelectorModel<>(column, Operator.LESS_EQUAL, value));
        return this;
    }

    public JdfSelector greaterThan(String column, Object value) {
        selectorList.add(new SelectorModel<>(column, Operator.GREATER, value));
        return this;
    }

    public JdfSelector greaterThanOrEqual(String column, Object value) {
        selectorList.add(new SelectorModel<>(column, Operator.GREATER_EQUAL, value));
        return this;
    }

    public static boolean eval(SelectorModel<?> selectorModel, Object rowValue) {
        if (selectorModel == null) {
            throw new NullPointerException("selector model must not be null");
        }
        Operator operator = selectorModel.getOperator();
        Object selectorValue = selectorModel.getValue();
        int compareTo = -2;
        // check if is string
        if (rowValue instanceof String) {
            compareTo = rowValue.toString().compareTo(selectorValue.toString());
        }
        // check if is number
        if (rowValue instanceof Integer || rowValue instanceof Double || rowValue instanceof Long) {
            double valueDouble = Double.valueOf(rowValue.toString());
            double compareValueDouble = Double.valueOf(selectorValue.toString());
            compareTo = Double.compare(valueDouble, compareValueDouble);
        }
        // check if is date
        if (rowValue instanceof LocalDate) {
            LocalDate date = (LocalDate) (rowValue);
            LocalDate compareDate = (LocalDate) (selectorValue);
            compareTo = date.compareTo(compareDate);
        }
        // start comparing
        if (compareTo == -2) {
            throw new IllegalArgumentException(String.format("cannot compare the values %s and %s", selectorValue, rowValue));
        }
        switch (operator) {
           case EQUAL:
                return compareTo == 0;
            case LESS:
                return compareTo == -1;
            case LESS_EQUAL:
                return compareTo == 0 || compareTo == -1;
            case GREATER:
                return compareTo == 1;
            case GREATER_EQUAL:
                return compareTo == 0 || compareTo == 1;
            default:
                return false;
        }
    }

    public List<SelectorModel<?>> getSelectorList() {
        return selectorList;
    }

    public void setSelectorList(List<SelectorModel<?>> selectorList) {
        this.selectorList = selectorList;
    }

    @Override
    public String toString() {
        return "JdfSelector [selectorList=" + selectorList + "]";
    }
}
