package com.example.c424.bean;

import java.util.List;

public class ProxyCollect {
    private List<ProxyInfo> vsArrays;
    private List<ProxyInfo> serverArrayPro;
    private String cert_url;

    public ProxyCollect() {

    }

    public ProxyCollect(List<ProxyInfo> vsArrays, List<ProxyInfo> serverArrayPro) {
        this.vsArrays = vsArrays;
        this.serverArrayPro = serverArrayPro;
    }

    public void setVsArrays(List<ProxyInfo> vsArrays) {
        this.vsArrays = vsArrays;
    }

    public List<ProxyInfo> getVsArrays() {
        return vsArrays;
    }

    public void setServerArrayPro(List<ProxyInfo> serverArrayPro) {
        this.serverArrayPro = serverArrayPro;
    }

    public List<ProxyInfo> getServerArrayPro() {
        return serverArrayPro;
    }

    public void setCert_url(String cert_url) {
        this.cert_url = cert_url;
    }

    public String getCert_url() {
        return cert_url;
    }

    @Override
    public String toString() {
        return "ProxyCollect{" +
                "vsArrays=" + vsArrays +
                ", serverArrayPro=" + serverArrayPro +
                ", cert_url='" + cert_url + '\'' +
                '}';
    }
}
