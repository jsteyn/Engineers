package com.jannetta.engineers.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jannetta.engineers.model.Widget;
import com.jannetta.engineers.model.Widgets;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TouchScreen extends JFrame {

    WidgetPanel[] widgetPanels = new WidgetPanel[100];
    Color[] colour = {Color.RED, Color.BLUE, Color.YELLOW};
    int screenwidth = 800;
    int screenheight = 480;
    int numcols = 8;
    int numrows = 8;
    int widgetheight = screenheight / numrows - 8;
    int widgetwidth = screenwidth / numcols - 5;

    TouchScreen() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("config.json"));
            Widgets widgets = gson.fromJson(br, Widgets.class);
            setLayout(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setMinimumSize(new Dimension(screenwidth, screenheight));
            setMaximumSize(new Dimension(screenwidth, screenheight));
            setSize(screenwidth, screenheight);

            for (int column = 0; column < numcols; column++) {
                for (int row = 0; row < numrows; row++) {
                    int dim = (row * numrows) + column;
                    String title = "";
                    String action = "";
                    widgetPanels[dim] = new WidgetPanel();
                    if (widgets.getWidgets().size() > dim) {
                        System.out.println(dim);
                        title = widgets.getWidgets().get(dim).getTitle();
                        action = widgets.getWidgets().get(dim).getAction();
                        widgetPanels[dim].setWidget(widgets.getWidgets().get(dim));
                    } else {
                        Widget widget = new Widget();
                        widgetPanels[dim].setWidget(widget);

                    }
                    widgetPanels[dim].setBounds(column * widgetwidth, row * widgetheight, widgetwidth, widgetheight);
                    widgetPanels[dim].setBackground(colour[dim % 3]);
                    widgetPanels[dim].setBorder(
                            BorderFactory.createCompoundBorder(
                                    BorderFactory.createCompoundBorder(
                                            BorderFactory.createTitledBorder(title),
                                            BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                                    widgetPanels[dim].getBorder()));
                    this.add(widgetPanels[dim]);
                    System.out.println(action);
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
