package com.andreidodu.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class RoundButton extends JButton {

    public static final Color BACKGROUND_COLOR = new Color(64, 62, 62, 255);
    public static final Color FOREGROUND_COLOR = Color.WHITE;
    private final BufferedImage bufferedImage;
    private boolean isHovered = false;

    public RoundButton(BufferedImage bufferedImage, String label) {
        super(label);
        this.bufferedImage = bufferedImage;
        setFocusable(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setPreferredSize(new Dimension(16, 16));

        setMargin(new Insets(0, 0, 0, 0));
        setFont(new Font("Arial", Font.BOLD, 14));
        setBackground(BACKGROUND_COLOR);
        setForeground(FOREGROUND_COLOR);
    }

    @Override
    protected void paintComponent(Graphics g) {

        if (isHovered) {
            g.setColor(BACKGROUND_COLOR);
            g.fillOval(0, 0, getWidth(), getHeight());

            g.setColor(Color.BLACK);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
        if (bufferedImage != null) {
            int imgX = (getWidth() - bufferedImage.getWidth()) / 2;
            int imgY = (getHeight() - bufferedImage.getHeight()) / 2;
            g.drawImage(bufferedImage, imgX, imgY, null);
        }
    }

    @Override
    public boolean contains(int x, int y) {
        int radius = getWidth() / 2;
        int centerX = radius;
        int centerY = radius;
        return ((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY)) <= (radius * radius);
    }

    public void setIsHovered(boolean boolValue) {
        this.isHovered = boolValue;
    }
}
