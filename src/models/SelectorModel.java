package models;

import models.JdfSelector.Operator;

public class SelectorModel<T> {
    private String column;
    private Operator operator;
    private T value;

    public SelectorModel(String column, Operator operator, T value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SelectorModel [column=" + column + ", operator=" + operator + ", value=" + value + "]";
    }
}
