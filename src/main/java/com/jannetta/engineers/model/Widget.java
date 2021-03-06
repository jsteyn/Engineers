
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

    @SerializedName("backgroundColor")
    @Expose
    private String backgroundColor = "#ffffff";

    @SerializedName("fontColor")
    @Expose
    private String fontColor = "#000000";

    @SerializedName("bytestoread")
    @Expose
    private int bytestoread = 1;

    private String outputStream;

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

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(String outputStream) {
        this.outputStream = outputStream;
    }

    public int getBytestoread() {
        return bytestoread;
    }

    public void setBytestoread(int bytestoread) {
        this.bytestoread = bytestoread;
    }
}