package com.andreidodu.controller;

import com.andreidodu.observer.CircleObserver;
import com.andreidodu.gui.WindowGUI;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class CircleController implements CircleObserver {

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private long maxTime = 1000 * 30 * 60;
    private AtomicLong timeCounter = new AtomicLong(maxTime);
    private AtomicReference<Double> arcCounter = new AtomicReference<>(0.0);


    public CircleController() {
        WindowGUI window = new WindowGUI(this);

        final ScheduledFuture<?>[] futureHolder = new ScheduledFuture<?>[1];
        double circleOnePercent = (double) 360 / ((double) maxTime / 1000);
        Double[] calculated = new Double[1];
        calculated[0] = arcCounter.get();

        ScheduledFuture<?> future = executorService.scheduleWithFixedDelay(() -> {
            long remainingTime = timeCounter.getAndSet(timeCounter.get() - 1000);
            String remainingTimeString = toMinuteSeconds(remainingTime);

            SwingUtilities.invokeLater(() -> {
                arcCounter.set(calculated[0]);
                window.getCircle().setArcAngle(calculated[0].intValue());
                window.getCircle().setTimeString(remainingTimeString);
                window.getCircle().repaint();
            });

            if (remainingTime <= 0) {
                futureHolder[0].cancel(true);
            } else {
                calculated[0] = arcCounter.get() - circleOnePercent;
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
}
