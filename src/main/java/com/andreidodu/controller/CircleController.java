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

public class CircleController implements CircleObserver {

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private AtomicReference<Long> maxTime = new AtomicReference<>((long) 1000 * 30 * 60);
    private AtomicLong currentTime = new AtomicLong(maxTime.get());
    private WindowGUI window;
    private AtomicReference<Double> currentArcValue = new AtomicReference<>(0.0);
    private AtomicReference<Integer> waitTime = new AtomicReference<>(0);
    private AtomicBoolean runningFlag = new AtomicBoolean(true);

    public CircleController() {
        window = new WindowGUI(this);

        final ScheduledFuture<?>[] futureHolder = new ScheduledFuture<?>[1];


        ScheduledFuture<?> future = executorService.scheduleWithFixedDelay(() -> {
            if (!runningFlag.get()) {
                return;
            }

            if (runningFlag.get() && waitTime.get() > 0) {
                waitTime.set(waitTime.get() - 1);
                return;
            }

            long remainingTime = currentTime.get();
            currentTime.set(currentTime.get() - 1000);
            String remainingTimeString = toMinuteSeconds(remainingTime);

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
        futureHolder[0] = future;

    }


    private String toMinuteSeconds(long time) {

        long totalSeconds = time / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void releaseResources() {
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
        double divisione = (double) timeCounter / (15 * 60 * 1000);
        if (divisione != 0.0) {
            timeCounter = (long) (Math.ceil(divisione) * 15 * 60 * 1000);
        }

        int k = 15000 * 60;

        if (timeCounter + k >= 1005 * 60 * 1000) {
            updateTimer(990 * 60 * 1000);
            return;
        }
        updateTimer(timeCounter + k);
    }

    private void updateTimer(long maxTime) {
        this.maxTime.set(maxTime);
        this.currentTime.set(maxTime);
        this.runningFlag.set(true);
        this.currentArcValue.set(0.0);
        waitTime.set(3);
        String remainingTimeString = toMinuteSeconds(maxTime);
        SwingUtilities.invokeLater(() -> {
            window.getCircle().setArcAngle(0);
            window.getCircle().setTimeString(remainingTimeString);
            window.getCircle().repaint();
        });
    }

    @Override
    public void decreaseTimer() {
        long timeCounter = this.currentTime.get();
        double divisione = (double) timeCounter / (15 * 60 * 1000);
        if (divisione > 0) {
            timeCounter = (long) (Math.ceil(divisione) * 15 * 60 * 1000);
        }


        int k = 15000 * 60;

        if (timeCounter - k <= 0) {
            updateTimer(k);
            return;
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
