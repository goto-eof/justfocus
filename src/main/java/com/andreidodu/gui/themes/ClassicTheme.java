package com.andreidodu.gui.themes;

import java.awt.*;

public class ClassicTheme implements Theme {
    @Override
    public Color getBaseColor() {
        return new Color(101, 255, 137, 255);
    }

    @Override
    public Color getTimeEndedColor() {
        return new Color(255, 101, 101, 255);
    }

    @Override
    public boolean isShowStrokeOnTop() {
        return false;
    }

    @Override
    public float[] getPrimaryStrokeProps() {
        return new float[]{-1f};
    }

    @Override
    public float getPrimaryStrokeWidth() {
        return -1f;
    }

    @Override
    public Color getPrimaryStrokeColor() {
        return new Color(0, 0, 0, 255);
    }
}
