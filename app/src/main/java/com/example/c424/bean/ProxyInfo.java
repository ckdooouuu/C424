package com.example.c424.bean;

public class ProxyInfo {
    private String webAlias;
    private String hostServer;
    private String serverCouAbbr;
    private String serCity;
    private int serverBalance;
    private String serverGN;
    private int osType;
    private String web_cou;
    private String ovports;
    private String kkports;
    private String userName;
    private String ownerPwd;
    private String cryptport;
    private long delay;

    public ProxyInfo() {
    }

    public ProxyInfo(String webAlias, String hostServer, String serverCouAbbr, String serCity,
                     int serverBalance, String serverGN, int osType, String web_cou, String ovports,
                     String kkports, String userName, String ownerPwd, String cryptport) {
        this.webAlias = webAlias;
        this.hostServer = hostServer;
        this.serverCouAbbr = serverCouAbbr;
        this.serCity = serCity;
        this.serverBalance = serverBalance;
        this.serverGN = serverGN;
        this.osType = osType;
        this.web_cou = web_cou;
        this.ovports = ovports;
        this.kkports = kkports;
        this.userName = userName;
        this.ownerPwd = ownerPwd;
        this.cryptport = cryptport;
    }

    public String getWebAlias() {
        return webAlias;
    }

    public void setWebAlias(String webAlias) {
        this.webAlias = webAlias;
    }

    public String getHostServer() {
        return hostServer;
    }

    public void setHostServer(String hostServer) {
        this.hostServer = hostServer;
    }

    public String getServerCouAbbr() {
        return serverCouAbbr;
    }

    public void setServerCouAbbr(String serverCouAbbr) {
        this.serverCouAbbr = serverCouAbbr;
    }

    public String getSerCity() {
        return serCity;
    }

    public void setSerCity(String serCity) {
        this.serCity = serCity;
    }

    public int getServerBalance() {
        return serverBalance;
    }

    public void setServerBalance(int serverBalance) {
        this.serverBalance = serverBalance;
    }

    public String getServerGN() {
        return serverGN;
    }

    public void setServerGN(String serverGN) {
        this.serverGN = serverGN;
    }

    public int getOsType() {
        return osType;
    }

    public void setOsType(int osType) {
        this.osType = osType;
    }

    public String getWeb_cou() {
        return web_cou;
    }

    public void setWeb_cou(String web_cou) {
        this.web_cou = web_cou;
    }

    public String getOvports() {
        return ovports;
    }

    public void setOvports(String ovports) {
        this.ovports = ovports;
    }

    public String getKkports() {
        return kkports;
    }

    public void setKkports(String kkports) {
        this.kkports = kkports;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOwnerPwd() {
        return ownerPwd;
    }

    public void setOwnerPwd(String ownerPwd) {
        this.ownerPwd = ownerPwd;
    }

    public String getCryptport() {
        return cryptport;
    }

    public void setCryptport(String cryptport) {
        this.cryptport = cryptport;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "ProxyInfo{" +
                "webAlias='" + webAlias + '\'' +
                ", hostServer='" + hostServer + '\'' +
                ", serverCouAbbr='" + serverCouAbbr + '\'' +
                ", serCity='" + serCity + '\'' +
                ", serverBalance=" + serverBalance +
                ", serverGN='" + serverGN + '\'' +
                ", osType=" + osType +
                ", web_cou='" + web_cou + '\'' +
                ", ovports='" + ovports + '\'' +
                ", kkports='" + kkports + '\'' +
                ", userName='" + userName + '\'' +
                ", ownerPwd='" + ownerPwd + '\'' +
                ", cryptport='" + cryptport + '\'' +
                ", delay='" + delay + '\'' +
                '}';
    }
}
