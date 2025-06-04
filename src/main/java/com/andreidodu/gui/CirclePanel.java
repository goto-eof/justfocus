package com.andreidodu.gui;

import com.andreidodu.gui.themes.Theme;

import javax.swing.*;
import java.awt.*;

public class CirclePanel extends JPanel implements ThemeChangerListener {

    public Color primaryStrokeColor = new Color(0, 0, 0, 255);
    public float[] primaryStrokeProps = {6f, 6f};
    public float primaryStrokeWidth = 24f;
    private Color baseColor = new Color(101, 255, 137, 255);
    private Color timeEndedColor = new Color(255, 101, 101, 255);
    private boolean isShowStrokeOnTop = false;

    private int arcAngle;
    private String timeString = "00:00:00";
    private final int diameter = 160;
    private final float thickness = 24f;
    private final int FONT_SIZE = 16;
    private boolean baseColorFlag = true;


    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public CirclePanel() {
        setOpaque(false);
    }

    public boolean isBaseColorFlag() {
        return baseColorFlag;
    }

    public void setBaseColorFlag(boolean baseColorFlag) {
        this.baseColorFlag = baseColorFlag;
    }

    public void setArcAngle(int arcAngle) {
        this.arcAngle = arcAngle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;

            if (baseColorFlag) {
                g2d.setColor(new Color(58, 58, 58, 255));
            } else {
                g2d.setColor(new Color(103, 3, 3, 255));

            }
            g2d.fillOval(x, y, diameter, diameter);

            g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, null, 0));


            g2d.setColor(new Color(69, 68, 68, 255));
            g2d.drawOval(x, y, diameter, diameter);


            if (baseColorFlag) {
                g2d.setColor(baseColor);
            } else {
                g2d.setColor(timeEndedColor);
            }
            int startAngle = 90;
            g2d.drawArc(x, y, diameter, diameter, startAngle, arcAngle);

            if (isShowStrokeOnTop) {
                g2d.setStroke(new BasicStroke(primaryStrokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f, primaryStrokeProps, 1));
                g2d.setColor(primaryStrokeColor);
                g2d.drawOval(x, y, diameter, diameter);
            }

            int innerDiameter = 80;
            int innerX = (getWidth() - innerDiameter) / 2;
            int innerY = (getHeight() - innerDiameter) / 2;

            g2d.setColor(new Color(69, 68, 68, 255));
            g2d.fillOval(innerX, innerY, innerDiameter, innerDiameter);

            g2d.setStroke(new BasicStroke(2f));
            g2d.setColor(baseColor);
            g2d.drawOval(innerX, innerY, innerDiameter, innerDiameter);


            g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f, new float[]{5f, 45f}, 1));
            g2d.setColor(primaryStrokeColor);
            g2d.drawOval(innerX, innerY, innerDiameter, innerDiameter);


            g2d.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(getTimeString());
            int textHeight = fm.getAscent();

            int centerX = getWidth() / 2 - textWidth / 2;
            int centerY = getHeight() / 2 + textHeight / 4;

            g2d.setColor(baseColor);
            g2d.drawString(getTimeString(), centerX, centerY);
        } finally {
            g2d.dispose();
        }
    }

    @Override
    public void onThemeChange(Theme theme) {
        this.baseColor = theme.getBaseColor();
        this.timeEndedColor = theme.getTimeEndedColor();
        this.isShowStrokeOnTop = theme.isShowStrokeOnTop();
        if (theme.isShowStrokeOnTop()) {
            this.primaryStrokeProps = theme.getPrimaryStrokeProps();
            this.primaryStrokeWidth = theme.getPrimaryStrokeWidth();
            this.primaryStrokeColor = theme.getPrimaryStrokeColor();
        }
        repaint();
    }

    public boolean isOnPanel(Point p) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        double dx = p.x - centerX;
        double dy = p.y - centerY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        double outerRadius = diameter / 2.0 + thickness / 2.0;

        return distance <= outerRadius;
    }

}
