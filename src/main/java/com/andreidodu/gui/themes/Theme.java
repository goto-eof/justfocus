package com.andreidodu.gui.themes;

import java.awt.*;

public interface Theme {

    Color getBaseColor();

    Color getTimeEndedColor();

    boolean isShowStrokeOnTop();

    float[] getPrimaryStrokeProps();

    float getPrimaryStrokeWidth();

    Color getPrimaryStrokeColor();
}
