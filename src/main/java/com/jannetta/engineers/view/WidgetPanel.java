package com.jannetta.engineers.view;

import com.jannetta.engineers.controller.ArduinoController;
import com.jannetta.engineers.model.Widget;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class WidgetPanel extends JPanel implements MouseListener {
    Widget widget;
    ArduinoController arduinoController;
    int cell = -1;

    public WidgetPanel(ArduinoController arduinoController) {
        super();
        this.arduinoController = arduinoController;
        addMouseListener(this);
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public int getCell() { return cell;}

    public void setCell(int cell) {
        this.cell = cell;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Cell: " + getCell());
        try {
            ArduinoController.writePort(Integer.valueOf(widget.getAction()));
        } catch (NumberFormatException err) {
            System.out.println("Action not specified");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
