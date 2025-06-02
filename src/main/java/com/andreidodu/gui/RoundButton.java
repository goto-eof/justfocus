package com.andreidodu.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class RoundButton extends JButton {

    public static final Color BACKGROUND_COLOR = new Color(64, 62, 62, 255);
    public static final Color FOREGROUND_COLOR = Color.WHITE;
    private final BufferedImage bufferedImage;
    private boolean isHovered = false;

    public RoundButton(BufferedImage bufferedImage) {
        super();
        this.bufferedImage = bufferedImage;
        setFocusable(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setPreferredSize(new Dimension(20, 20));

        setMargin(new Insets(0, 0, 0, 0));
        setFont(new Font("Arial", Font.BOLD, 14));
        setBackground(BACKGROUND_COLOR);
        setForeground(FOREGROUND_COLOR);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        int cornerRadius = 20;
        if (isHovered) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(BACKGROUND_COLOR);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

            g2.setColor(Color.BLACK);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }

        if (bufferedImage != null) {
            int imgX = (getWidth() - bufferedImage.getWidth()) / 2;
            int imgY = (getHeight() - bufferedImage.getHeight()) / 2;
            g.drawImage(bufferedImage, imgX, imgY, null);
        }

        g2.dispose();
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
