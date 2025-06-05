package com.andreidodu.gui.themes;

import java.awt.*;

public class StrokeTheme implements Theme {
    private Color baseColor;


    public StrokeTheme(Color baseColor) {
        this.baseColor = baseColor;
    }

    @Override
    public Color getBaseColor() {
        return baseColor;
    }

    @Override
    public Color getTimeEndedColor() {
        return new Color(255, 101, 101, 255);
    }

    @Override
    public boolean isShowStrokeOnTop() {
        return true;
    }

    @Override
    public float[] getPrimaryStrokeProps() {
        return new float[]{6f, 6f};
    }

    @Override
    public float getPrimaryStrokeWidth() {
        return 24f;
    }

    @Override
    public Color getPrimaryStrokeColor() {
        return new Color(0, 0, 0, 255);
    }
}
