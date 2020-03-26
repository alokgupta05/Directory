package com.ajit.bjp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gs$cell {

    @SerializedName("row")
    @Expose
    private int row;

    @SerializedName("col")
    @Expose
    private int col;

    @SerializedName("inputValue")
    @Expose
    private String inputValue;

    @SerializedName("numericValue")
    @Expose
    private String numericValue;

    @SerializedName("$t")
    @Expose
    private String $t;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(String numericValue) {
        this.numericValue = numericValue;
    }

    public String get$t() {
        return $t;
    }

    public void set$t(String $t) {
        this.$t = $t;
    }
}
