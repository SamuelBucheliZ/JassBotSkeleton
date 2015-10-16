package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import com.typesafe.config.Config;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
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

    public static Strategy getStrategy(String strategyName, Config config) {
        Strategy strategy;
        try {
            Class[] constructorArguments = new Class[1];
            constructorArguments[0] = Config.class;
            strategy = strategies.get(strategyName).getDeclaredConstructor(constructorArguments).newInstance(config);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("Can not create instance of strategy %s.", strategyName), e);
        }
        return strategy;
    }



    private StrategyRepository() {}
}
