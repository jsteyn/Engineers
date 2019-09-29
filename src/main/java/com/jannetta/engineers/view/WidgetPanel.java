package com.jannetta.engineers.view;

import com.jannetta.engineers.controller.ArduinoController;
import com.jannetta.engineers.controller.Helpers;
import com.jannetta.engineers.model.Widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class WidgetPanel extends JPanel implements MouseListener {
    Widget widget;
    ArduinoController arduinoController;
    int cell = -1;
    private JLabel outputLabel;
    int bytestoread = 1;

    public WidgetPanel(ArduinoController arduinoController) {
        super();
        this.arduinoController = arduinoController;
        addMouseListener(this);
        outputLabel = new JLabel();
        outputLabel.setFont(new Font("Arial", 1, 20));
        outputLabel.setText(">><<");
        add(outputLabel);
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
        setBackground(Helpers.hex2Rgb(widget.getBackgroundColor()));
        outputLabel.setForeground(Helpers.hex2Rgb(widget.getFontColor()));
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public JLabel getOutputLabel() {
        return outputLabel;
    }

    public void setOutputLabel(JLabel outputLabel) {
        this.outputLabel = outputLabel;
    }

    public int getBytestoread() {
        return bytestoread;
    }

    public void setBytestoread(int bytestoread) {
        this.bytestoread = bytestoread;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            String s = ArduinoController.writePort(Integer.valueOf(widget.getAction()), getBytestoread(),100);
            outputLabel.setText(s);
            System.out.println(outputLabel.getText());
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
