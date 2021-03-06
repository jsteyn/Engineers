package com.jannetta.engineers.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jannetta.engineers.controller.ArduinoController;
import com.jannetta.engineers.controller.Helpers;
import com.jannetta.engineers.model.Widget;
import com.jannetta.engineers.model.Widgets;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TouchScreen extends JFrame {

    WidgetPanel[] widgetPanels = new WidgetPanel[100];
    Color[] colour = {Color.RED, Color.BLUE, Color.YELLOW};
    int screenwidth = 800;
    int screenheight = 480;
    int numcols = 0;
    int numrows = 0;
    int widgetheight = 0;
    int widgetwidth = 0;
    String serialPortDescriptor = "";
    ArduinoController arduinoController;

    TouchScreen() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("config.json"));
            Widgets widgets = gson.fromJson(br, Widgets.class);
            serialPortDescriptor = widgets.getComport();
            arduinoController = ArduinoController.getInstance(serialPortDescriptor);
            numcols = widgets.getColumns();
            numrows = widgets.getRows();
            widgetheight = ((screenheight - 50) / numrows);
            widgetwidth = (screenwidth - 20) / numcols;
            setLayout(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setMinimumSize(new Dimension(screenwidth, screenheight));
            setMaximumSize(new Dimension(screenwidth, screenheight));
            setSize(screenwidth, screenheight);

            // Set up widgets with their text and actions
            for (int row = 0; row < numrows; row++) {
                for (int column = 0; column < numcols; column++) {
                    int dim = (row * numrows) + column;
                    String title = "";
                    String action = "";
                    widgetPanels[dim] = new WidgetPanel(arduinoController);
                    widgetPanels[dim].setCell(dim);
                    if (widgets.getWidgets().size() > dim) {
                        title = widgets.getWidgets().get(dim).getTitle();
                        action = widgets.getWidgets().get(dim).getAction();
                        widgetPanels[dim].setWidget(widgets.getWidgets().get(dim));
                        widgetPanels[dim].setForeground(Helpers.hex2Rgb(widgets.getWidgets().get(dim).getFontColor()));
                        widgetPanels[dim].setBytestoread(widgets.getWidgets().get(dim).getBytestoread());
                    } else {
                        Widget widget = new Widget();
                        widgetPanels[dim].setWidget(widget);
                    }
                    widgetPanels[dim].setBounds(column * widgetwidth, row * widgetheight, widgetwidth, widgetheight);
                    widgetPanels[dim].setBorder(
                            BorderFactory.createTitledBorder(null,
                                    title,
                                    TitledBorder.CENTER,
                                    TitledBorder.TOP,
                                    new Font("Verdana", Font.BOLD, 12),
                                    widgetPanels[dim].getForeground())); //BorderFactory.createEmptyBorder(5, 5, 5, 5))
                    if (dim == numrows * numcols - 1)
                        widgetPanels[dim].getOutputLabel().setText(widgets.getComport());
                    this.add(widgetPanels[dim]);
                }
            }
            setVisible(true);
            pack();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    static public void main(String[] args) {
        TouchScreen touchScreen = new TouchScreen();
    }
}
