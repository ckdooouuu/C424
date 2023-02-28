package com.example.c424.utils.ad;

public class AdStatusUtil {
    private volatile static AdStatusUtil Instance;
    private AdStatusUtil() {

    }

    public static AdStatusUtil getInstance() {
        if (Instance == null) {
            synchronized (AdStatusUtil.class) {
                if (Instance == null) {
                    Instance = new AdStatusUtil();
                }
            }
        }
        return Instance;
    }

    private int startAdStatus;
    private int homeAdStatus;
    private int finishAdStatus;
    private int reportAdStatus;

    //在启动页是否需要拉取广告 如果不需要就不用等广告返回
    private boolean isNeedLoadStartAd;
    private boolean isNeedLoadHomeAd;
    private boolean isNeedLoadFinishAd;
    private boolean isNeedLoadReportAd;

    private boolean isShareStartToFinish;
    private boolean isShareFinishToStart;
    private boolean isShareHomeReport;

    public void setStartAdStatus(int startAdStatus) {
        this.startAdStatus = startAdStatus;
    }

    public int getStartAdStatus() {
        return startAdStatus;
    }

    public void setHomeAdStatus(int homeAdStatus) {
        this.homeAdStatus = homeAdStatus;
    }

    public int getHomeAdStatus() {
        return homeAdStatus;
    }

    public void setFinishAdStatus(int finishAdStatus) {
        this.finishAdStatus = finishAdStatus;
    }

    public int getFinishAdStatus() {
        return finishAdStatus;
    }

    public void setReportAdStatus(int reportAdStatus) {
        this.reportAdStatus = reportAdStatus;
    }

    public int getReportAdStatus() {
        return reportAdStatus;
    }

    public boolean isNeedLoadStartAd() {
        return isNeedLoadStartAd;
    }

    public void setNeedLoadStartAd(boolean needLoadStartAd) {
        isNeedLoadStartAd = needLoadStartAd;
    }

    public boolean isNeedLoadHomeAd() {
        return isNeedLoadHomeAd;
    }

    public void setNeedLoadHomeAd(boolean needLoadHomeAd) {
        isNeedLoadHomeAd = needLoadHomeAd;
    }

    public boolean isNeedLoadFinishAd() {
        return isNeedLoadFinishAd;
    }

    public void setNeedLoadFinishAd(boolean needLoadFinishAd) {
        isNeedLoadFinishAd = needLoadFinishAd;
    }

    public boolean isNeedLoadReportAd() {
        return isNeedLoadReportAd;
    }

    public void setNeedLoadReportAd(boolean needLoadReportAd) {
        isNeedLoadReportAd = needLoadReportAd;
    }

    public boolean isShareStartToFinish() {
        return isShareStartToFinish;
    }

    public void setShareStartToFinish(boolean shareStartToFinish) {
        isShareStartToFinish = shareStartToFinish;
    }

    public boolean isShareFinishToStart() {
        return isShareFinishToStart;
    }

    public void setShareFinishToStart(boolean shareFinishToStart) {
        isShareFinishToStart = shareFinishToStart;
    }

    public boolean isShareHomeReport() {
        return isShareHomeReport;
    }

    public void setShareHomeReport(boolean shareHomeReport) {
        isShareHomeReport = shareHomeReport;
    }
}
