package com.example.c424.bean;

import java.util.List;

public class ConnectionProxyInfo {
    private int auto;
    private int p_link;
    private String proxIp;//节点IP
    private String couServers;//节点国家
    private String webAlias;//节点别名
    private List<ConnectionResult> knotInfo;

    public ConnectionProxyInfo() {

    }

    public ConnectionProxyInfo(int auto, int p_link, String proxIp, String couServers, String webAlias, List<ConnectionResult> knotInfo) {
        this.auto = auto;
        this.p_link = p_link;
        this.proxIp = proxIp;
        this.couServers = couServers;
        this.webAlias = webAlias;
        this.knotInfo = knotInfo;
    }

    public int getAuto() {
        return auto;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    public int getP_link() {
        return p_link;
    }

    public void setP_link(int p_link) {
        this.p_link = p_link;
    }

    public String getProxIp() {
        return proxIp;
    }

    public void setProxIp(String proxIp) {
        this.proxIp = proxIp;
    }

    public String getCouServers() {
        return couServers;
    }

    public void setCouServers(String couServers) {
        this.couServers = couServers;
    }

    public String getWebAlias() {
        return webAlias;
    }

    public void setWebAlias(String webAlias) {
        this.webAlias = webAlias;
    }

    public List<ConnectionResult> getKnotInfo() {
        return knotInfo;
    }

    public void setKnotInfo(List<ConnectionResult> knotInfo) {
        this.knotInfo = knotInfo;
    }

    @Override
    public String toString() {
        return "ConnectionProxyInfo{" +
                "auto=" + auto +
                ", p_link=" + p_link +
                ", proxyIp='" + proxIp + '\'' +
                ", couServers='" + couServers + '\'' +
                ", webAlias='" + webAlias + '\'' +
                ", knotInfo=" + knotInfo +
                '}';
    }
}
