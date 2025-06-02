package com.andreidodu.gui;

import javax.swing.*;
import java.awt.*;

class RoundButton extends JButton {

    public static final Color BACKGROUND_COLOR = new Color(64, 62, 62, 255);
    public static final Color FOREGROUND_COLOR = Color.WHITE;

    public RoundButton(String label) {
        super(label);
        setFocusable(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        // setPreferredSize(new Dimension(80, 80));

        setMargin(new Insets(0, 0, 0, 0));
        setFont(new Font("Arial", Font.BOLD, 14));
        setBackground(BACKGROUND_COLOR);
        setForeground(FOREGROUND_COLOR);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillOval(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.drawOval(0, 0, getWidth(), getHeight());


        // Draw text
        FontMetrics fm = g.getFontMetrics();
        int stringWidth = fm.stringWidth(getText());
        int stringHeight = fm.getAscent();
        g.setColor(Color.BLACK);
        g.drawString(getText(),
                (getWidth() - stringWidth) / 2,
                (getHeight() + stringHeight) / 2 - 2);
    }

    @Override
    public boolean contains(int x, int y) {
        int radius = getWidth() / 2;
        int centerX = radius;
        int centerY = radius;
        return ((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY)) <= (radius * radius);
    }
}
