package com.example.c424.bean;

import java.util.List;

public class ConnectStrategy {
    private List<ConnectType> tacticsConfig;
    private int strategy_code;

    public ConnectStrategy() {

    }

    public ConnectStrategy(List<ConnectType> tacticsConfig, int strategy_code) {
        this.tacticsConfig = tacticsConfig;
        this.strategy_code = strategy_code;
    }

    public List<ConnectType> getTacticsConfig() {
        return tacticsConfig;
    }

    public void setTacticsConfig(List<ConnectType> tacticsConfig) {
        this.tacticsConfig = tacticsConfig;
    }

    public int getStrategy_code() {
        return strategy_code;
    }

    public void setStrategy_code(int strategy_code) {
        this.strategy_code = strategy_code;
    }

    @Override
    public String toString() {
        return "ConnectStrategy{" +
                "tacticsConfig=" + tacticsConfig +
                ", strategy_code=" + strategy_code +
                '}';
    }
}
