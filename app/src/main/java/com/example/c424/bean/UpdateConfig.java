package com.example.c424.bean;

public class UpdateConfig {
    private int finishApp;
    private String dialogMsg;
    private int minApp;
    private String upgradett;
    private boolean mustForce;
    private String upgradeName;

    public UpdateConfig() {

    }

    public UpdateConfig(int finishApp, String dialogMsg, int minApp, String upgradett, boolean mustForce, String upgradeName) {
        this.finishApp = finishApp;
        this.dialogMsg = dialogMsg;
        this.minApp = minApp;
        this.upgradett = upgradett;
        this.mustForce = mustForce;
        this.upgradeName = upgradeName;
    }

    public int getFinishApp() {
        return finishApp;
    }

    public void setFinishApp(int finishApp) {
        this.finishApp = finishApp;
    }

    public String getDialogMsg() {
        return dialogMsg;
    }

    public void setDialogMsg(String dialogMsg) {
        this.dialogMsg = dialogMsg;
    }

    public int getMinApp() {
        return minApp;
    }

    public void setMinApp(int minApp) {
        this.minApp = minApp;
    }

    public String getUpgradett() {
        return upgradett;
    }

    public void setUpgradett(String upgradett) {
        this.upgradett = upgradett;
    }

    public boolean isMustForce() {
        return mustForce;
    }

    public void setMustForce(boolean mustForce) {
        this.mustForce = mustForce;
    }

    public String getUpgradeName() {
        return upgradeName;
    }

    public void setUpgradeName(String upgradeName) {
        this.upgradeName = upgradeName;
    }

    @Override
    public String toString() {
        return "UpdateConfig{" +
                "finishApp=" + finishApp +
                ", dialogMsg='" + dialogMsg + '\'' +
                ", minApp=" + minApp +
                ", upgradett='" + upgradett + '\'' +
                ", mustForce=" + mustForce +
                ", upgradeName='" + upgradeName + '\'' +
                '}';
    }
}
