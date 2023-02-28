package com.example.c424.bean;

import java.util.List;

public class GlobalConfig {
    private int Native_rocketspeedup_ClickLimit;
    private boolean rocketspeedup_Guide;
    private String Priority_rocketspeedup_Connect;
    private boolean List_rocketspeedup_BackAd;
    private int Max_rocketspeedup_Connect;
    private List<AdConfig> configAd;
    private List<ConnectStrategy> strategyList;
    private int Config_rocketspeedup_Interval;
    private UpdateConfig forceConfig;
    private boolean StStart_rocketspeedup_Ad;
    private int StartAnimation_rocketspeedup_Upper;
    private boolean Report_rocketspeedup_BackAd;
    private String rocketspeedup_BlackLists;

    public GlobalConfig() {

    }

    public GlobalConfig(int nativeRocketspeedupClickLimit, boolean rocketspeedupGuide, String priorityRocketspeedupConnect,
                        boolean listRocketspeedupBackAd, int maxRocketspeedupConnect, List<AdConfig> configAd,
                        List<ConnectStrategy> strategyList, int configRocketspeedupInterval, UpdateConfig forceConfig,
                        boolean stStartRocketspeedupAd, int startAnimationRocketspeedupUpper, boolean reportRocketspeedupBackAd, String rocketspeedupBlackLists) {
        Native_rocketspeedup_ClickLimit = nativeRocketspeedupClickLimit;
        rocketspeedup_Guide = rocketspeedupGuide;
        Priority_rocketspeedup_Connect = priorityRocketspeedupConnect;
        List_rocketspeedup_BackAd = listRocketspeedupBackAd;
        Max_rocketspeedup_Connect = maxRocketspeedupConnect;
        this.configAd = configAd;
        this.strategyList = strategyList;
        Config_rocketspeedup_Interval = configRocketspeedupInterval;
        this.forceConfig = forceConfig;
        StStart_rocketspeedup_Ad = stStartRocketspeedupAd;
        StartAnimation_rocketspeedup_Upper = startAnimationRocketspeedupUpper;
        Report_rocketspeedup_BackAd = reportRocketspeedupBackAd;
        rocketspeedup_BlackLists = rocketspeedupBlackLists;
    }

    public int getNative_rocketspeedup_ClickLimit() {
        return Native_rocketspeedup_ClickLimit;
    }

    public void setNative_rocketspeedup_ClickLimit(int native_rocketspeedup_ClickLimit) {
        Native_rocketspeedup_ClickLimit = native_rocketspeedup_ClickLimit;
    }

    public boolean isRocketspeedup_Guide() {
        return rocketspeedup_Guide;
    }

    public void setRocketspeedup_Guide(boolean rocketspeedup_Guide) {
        this.rocketspeedup_Guide = rocketspeedup_Guide;
    }

    public String getPriority_rocketspeedup_Connect() {
        return Priority_rocketspeedup_Connect;
    }

    public void setPriority_rocketspeedup_Connect(String priority_rocketspeedup_Connect) {
        Priority_rocketspeedup_Connect = priority_rocketspeedup_Connect;
    }

    public boolean isList_rocketspeedup_BackAd() {
        return List_rocketspeedup_BackAd;
    }

    public void setList_rocketspeedup_BackAd(boolean list_rocketspeedup_BackAd) {
        List_rocketspeedup_BackAd = list_rocketspeedup_BackAd;
    }

    public int getMax_rocketspeedup_Connect() {
        return Max_rocketspeedup_Connect;
    }

    public void setMax_rocketspeedup_Connect(int max_rocketspeedup_Connect) {
        Max_rocketspeedup_Connect = max_rocketspeedup_Connect;
    }

    public List<AdConfig> getConfigAd() {
        return configAd;
    }

    public void setConfigAd(List<AdConfig> configAd) {
        this.configAd = configAd;
    }

    public List<ConnectStrategy> getStrategyList() {
        return strategyList;
    }

    public void setStrategyList(List<ConnectStrategy> strategyList) {
        this.strategyList = strategyList;
    }

    public int getConfig_rocketspeedup_Interval() {
        return Config_rocketspeedup_Interval;
    }

    public void setConfig_rocketspeedup_Interval(int config_rocketspeedup_Interval) {
        Config_rocketspeedup_Interval = config_rocketspeedup_Interval;
    }

    public UpdateConfig getForceConfig() {
        return forceConfig;
    }

    public void setForceConfig(UpdateConfig forceConfig) {
        this.forceConfig = forceConfig;
    }

    public boolean isStStart_rocketspeedup_Ad() {
        return StStart_rocketspeedup_Ad;
    }

    public void setStStart_rocketspeedup_Ad(boolean stStart_rocketspeedup_Ad) {
        StStart_rocketspeedup_Ad = stStart_rocketspeedup_Ad;
    }

    public int getStartAnimation_rocketspeedup_Upper() {
        return StartAnimation_rocketspeedup_Upper;
    }

    public void setStartAnimation_rocketspeedup_Upper(int startAnimation_rocketspeedup_Upper) {
        StartAnimation_rocketspeedup_Upper = startAnimation_rocketspeedup_Upper;
    }

    public boolean isReport_rocketspeedup_BackAd() {
        return Report_rocketspeedup_BackAd;
    }

    public void setReport_rocketspeedup_BackAd(boolean report_rocketspeedup_BackAd) {
        Report_rocketspeedup_BackAd = report_rocketspeedup_BackAd;
    }

    public String getRocketspeedup_BlackLists() {
        return rocketspeedup_BlackLists;
    }

    public void setRocketspeedup_BlackLists(String rocketspeedup_BlackLists) {
        this.rocketspeedup_BlackLists = rocketspeedup_BlackLists;
    }

    @Override
    public String toString() {
        return "GlobalConfig{" +
                "Native_rocketspeedup_ClickLimit=" + Native_rocketspeedup_ClickLimit +
                ", rocketspeedup_Guide=" + rocketspeedup_Guide +
                ", Priority_rocketspeedup_Connect='" + Priority_rocketspeedup_Connect + '\'' +
                ", List_rocketspeedup_BackAd=" + List_rocketspeedup_BackAd +
                ", Max_rocketspeedup_Connect=" + Max_rocketspeedup_Connect +
                ", configAd=" + configAd +
                ", strategyList=" + strategyList +
                ", Config_rocketspeedup_Interval=" + Config_rocketspeedup_Interval +
                ", forceConfig=" + forceConfig +
                ", StStart_rocketspeedup_Ad=" + StStart_rocketspeedup_Ad +
                ", StartAnimation_rocketspeedup_Upper=" + StartAnimation_rocketspeedup_Upper +
                ", Report_rocketspeedup_BackAd=" + Report_rocketspeedup_BackAd +
                ", rocketspeedup_BlackLists='" + rocketspeedup_BlackLists + '\'' +
                '}';
    }
}
