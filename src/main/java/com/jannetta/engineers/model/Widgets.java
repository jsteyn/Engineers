package com.jannetta.engineers.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Widgets {

    @SerializedName("widgets")
    @Expose
    private List<Widget> widgets = null;

    public List<Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
    }

}