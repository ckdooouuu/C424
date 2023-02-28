package com.example.c424.bean;

public class ConnectionResult {
    private int point;//端口
    private int linkStatus;//连接状态 0成功 1失败
    private long linkTimes;//连接耗时
    private int joinType;//连接协议 0 1 udp    2 tcp    3 ikev2

    public ConnectionResult() {

    }

    public ConnectionResult(int point, int linkStatus, long linkTimes, int joinType) {
        this.point = point;
        this.linkStatus = linkStatus;
        this.linkTimes = linkTimes;
        this.joinType = joinType;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(int linkStatus) {
        this.linkStatus = linkStatus;
    }

    public long getLinkTimes() {
        return linkTimes;
    }

    public void setLinkTimes(long linkTimes) {
        this.linkTimes = linkTimes;
    }

    public int getJoinType() {
        return joinType;
    }

    public void setJoinType(int joinType) {
        this.joinType = joinType;
    }

    @Override
    public String toString() {
        return "ConnectionResult{" +
                "point=" + point +
                ", linkStatus=" + linkStatus +
                ", linkTimes=" + linkTimes +
                ", joinType=" + joinType +
                '}';
    }
}
