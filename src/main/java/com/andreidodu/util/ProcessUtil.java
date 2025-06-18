package com.andreidodu.util;

import com.andreidodu.util.process.strategies.LinuxStrategy;
import com.andreidodu.util.process.strategies.Strategy;

import java.util.ArrayList;
import java.util.List;


public class ProcessUtil {

    private static final List<Strategy> strategies = getStrategies();

    private static List<Strategy> getStrategies() {
        List<Strategy> strategies = new ArrayList<>();
        strategies.add(new LinuxStrategy());
        return strategies;
    }

    public static void enableFocusMode(boolean isEnabled) {
        strategies.stream()
                .filter(Strategy::accept)
                .findFirst()
                .map(strategy -> {
                    strategy.execute(isEnabled);
                    return true;
                })
                .orElseGet(() -> false);
    }


}
