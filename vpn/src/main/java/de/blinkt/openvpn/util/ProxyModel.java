/*
 * Copyright (c) 2012-2022 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package de.blinkt.openvpn.util;

public class ProxyModel {
    private String ip;
    private String port;
    private String userName;
    private String password;
    private boolean useUDP;

    public ProxyModel() {

    }

    public ProxyModel(String ip, String port, String userName, String password ,boolean useUDP) {
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.useUDP = useUDP;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUseUDP(boolean useUDP) {
        this.useUDP = useUDP;
    }

    public boolean isUseUDP() {
        return useUDP;
    }

    @Override
    public String toString() {
        return "ProxyModel{" +
                "ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", useUDP=" + useUDP +
                '}';
    }
}
