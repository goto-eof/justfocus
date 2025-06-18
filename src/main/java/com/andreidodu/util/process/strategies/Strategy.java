package com.andreidodu.util.process.strategies;

public interface Strategy {
    boolean accept();

    void execute(boolean isEnabled);
}
