package com.example.c424.bean;

public class ConnectType {
    private int strategyType;
    private int port;
    private int timeouts;

    public ConnectType() {

    }

    public ConnectType(int strategyType, int port, int timeouts) {
        this.strategyType = strategyType;
        this.port = port;
        this.timeouts = timeouts;
    }

    public int getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(int strategyType) {
        this.strategyType = strategyType;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeouts() {
        return timeouts;
    }

    public void setTimeouts(int timeouts) {
        this.timeouts = timeouts;
    }

    @Override
    public String toString() {
        return "ConnectType{" +
                "strategyType=" + strategyType +
                ", port=" + port +
                ", timeouts=" + timeouts +
                '}';
    }
}
