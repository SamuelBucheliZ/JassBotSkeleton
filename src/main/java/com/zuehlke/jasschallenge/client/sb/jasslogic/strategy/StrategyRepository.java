package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StrategyRepository {
    private static final Map<String, Class<? extends Strategy>> strategies = new HashMap<>();
    static {
        String packageName = Strategy.class.getPackage().getName();

        Reflections reflections = new Reflections(packageName);

        Set<Class<? extends Strategy>> strategyClasses =
                reflections.getSubTypesOf(Strategy.class);

        for(Class<? extends Strategy> strategyClass : strategyClasses) {
            strategies.put(strategyClass.getSimpleName(), strategyClass);
        }
    }

    public static Strategy getStrategy(String strategyName) {
        Strategy strategy;
        try {
            strategy = strategies.get(strategyName).newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new IllegalArgumentException(String.format("Can not create instance of strategy %s.", strategyName), e);
        }
        return strategy;
    }

    private StrategyRepository() {}
}
