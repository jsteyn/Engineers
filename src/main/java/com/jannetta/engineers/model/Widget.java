
package com.jannetta.engineers.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Widget {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("action")
    @Expose
    private String action;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}