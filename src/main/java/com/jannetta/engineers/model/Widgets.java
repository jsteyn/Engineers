package com.jannetta.engineers.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Widgets {

    @SerializedName("widgets")
    @Expose
    private List<Widget> widgets = null;

    @SerializedName("rows")
    @Expose
    private int rows = 0;

    @SerializedName("columns")
    @Expose
    private int columns = 0;

    @SerializedName("comport")
    @Expose
    private String comport = "";

    public List<Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public String getComport() {
        return comport;
    }

    public void setComport(String comport) {
        this.comport = comport;
    }
}