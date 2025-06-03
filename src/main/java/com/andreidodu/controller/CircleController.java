package com.andreidodu.controller;

import com.andreidodu.gui.WindowGUI;
import com.andreidodu.observer.CircleObserver;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static com.andreidodu.util.TimeUtil.millisToHHMMSS;

public class CircleController implements CircleObserver {

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private AtomicReference<Long> maxTime = new AtomicReference<>((long) 1000 * 30 * 60);
    private AtomicLong currentTime = new AtomicLong(maxTime.get());
    private WindowGUI window;
    private AtomicReference<Double> currentArcValue = new AtomicReference<>(0.0);
    private AtomicReference<Integer> waitTime = new AtomicReference<>(0);
    private AtomicBoolean runningFlag = new AtomicBoolean(true);


    private final static int DEFAULT_INTERVAL = 15 * 60 * 1000;
    private final static int SHORT_INTERVAL = 5 * 60 * 1000;
    private final static int SUPER_SHORT_INTERVAL = 60 * 1000;
    private ScheduledFuture<?> futureHolder;

    public CircleController() {
        window = new WindowGUI(this);
        futureHolder = executorService.scheduleWithFixedDelay(() -> {
            if (!runningFlag.get()) {
                SwingUtilities.invokeLater(() -> {
                    window.getCircle().setBaseColorFlag(!window.getCircle().isBaseColorFlag());
                    window.getCircle().repaint();
                });
                return;
            }

            if (runningFlag.get() && waitTime.get() > 0) {
                waitTime.set(waitTime.get() - 1);
                return;
            }

            long remainingTime = currentTime.get();
            currentTime.set(currentTime.get() - 1000);
            String remainingTimeString = millisToHHMMSS(remainingTime);

            if (remainingTime > 0) {
                currentArcValue.set(currentArcValue.get() - calculateOnePerc());
            }
            SwingUtilities.invokeLater(() -> {
                window.getCircle().setArcAngle(currentArcValue.get().intValue());
                window.getCircle().setTimeString(remainingTimeString);
                window.getCircle().repaint();
            });

            if (remainingTime <= 0) {
                runningFlag.set(false);
            }

        }, 1000L, 1000, TimeUnit.MILLISECONDS);

    }

    @Override
    public void releaseResources() {
        if (futureHolder != null) {
            futureHolder.cancel(true);
        }
        if (executorService != null && !executorService.isShutdown()) {
            try {
                executorService.shutdownNow();
            } catch (Exception e) {
                System.err.println("Unable to shutdown executor service: " + e.getMessage());
            }
        }
    }

    @Override
    public void increaseTimer() {
        long timeCounter = this.currentTime.get();


        if (timeCounter + DEFAULT_INTERVAL >= 1005 * 60 * 1000) {
            updateTimer(990 * 60 * 1000);
            return;
        }


        int k = DEFAULT_INTERVAL;
        if (timeCounter < SHORT_INTERVAL) {
            double divisione = (double) timeCounter / SUPER_SHORT_INTERVAL;
            if (divisione != 0.0) {
                timeCounter = (long) (Math.ceil(divisione) * SUPER_SHORT_INTERVAL);
            }
            k = SUPER_SHORT_INTERVAL;
        } else if (timeCounter < DEFAULT_INTERVAL) {
            double divisione = (double) timeCounter / SHORT_INTERVAL;
            if (divisione != 0.0) {
                timeCounter = (long) (Math.ceil(divisione) * SHORT_INTERVAL);
            }
            k = SHORT_INTERVAL;
        } else {
            double divisione = (double) timeCounter / DEFAULT_INTERVAL;
            if (divisione != 0.0) {
                timeCounter = (long) (Math.ceil(divisione) * DEFAULT_INTERVAL);
            }
            k = DEFAULT_INTERVAL;
        }

        updateTimer(timeCounter + k);
    }

    private void updateTimer(long maxTime) {
        this.maxTime.set(maxTime);
        this.currentTime.set(maxTime);
        this.runningFlag.set(true);
        this.currentArcValue.set(0.0);
        waitTime.set(3);
        String remainingTimeString = millisToHHMMSS(maxTime);
        SwingUtilities.invokeLater(() -> {
            window.getCircle().setArcAngle(0);
            window.getCircle().setBaseColorFlag(true);
            window.getCircle().setTimeString(remainingTimeString);
            window.getCircle().repaint();
        });
    }

    @Override
    public void decreaseTimer() {
        long timeCounter = this.currentTime.get();

        int k = SUPER_SHORT_INTERVAL;


        if (timeCounter - k <= 0) {
            updateTimer(k);
            return;
        }

        if (timeCounter <= SHORT_INTERVAL) {
            double divisione = (double) timeCounter / SUPER_SHORT_INTERVAL;
            timeCounter = (long) (Math.ceil(divisione) * SUPER_SHORT_INTERVAL);
            k = SUPER_SHORT_INTERVAL;
        } else if (timeCounter <= DEFAULT_INTERVAL) {
            double divisione = (double) timeCounter / SHORT_INTERVAL;
            timeCounter = (long) (Math.ceil(divisione) * SHORT_INTERVAL);
            k = SHORT_INTERVAL;
        } else {
            double divisione = (double) timeCounter / DEFAULT_INTERVAL;
            timeCounter = (long) (Math.ceil(divisione) * 15 * 60 * 1000);
            k = DEFAULT_INTERVAL;
        }

        updateTimer(timeCounter - k);
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    public double calculateOnePerc() {
        return (double) 360 / ((double) maxTime.get() / 1000);
    }
}
