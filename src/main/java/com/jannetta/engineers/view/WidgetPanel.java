package com.jannetta.engineers.view;

import com.jannetta.engineers.model.Widget;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class WidgetPanel extends JPanel implements MouseListener {
    Widget widget;

    public WidgetPanel() {
        super();
        addMouseListener(this);
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        widget.getAction();
        System.out.println("Action: " + widget.getAction());
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
