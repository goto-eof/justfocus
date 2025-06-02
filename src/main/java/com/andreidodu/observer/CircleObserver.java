package com.andreidodu.observer;

public interface CircleObserver {
    void releaseResources();

    void increaseTimer();

    void decreaseTimer();

    void exit();
}
