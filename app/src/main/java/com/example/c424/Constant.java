package com.example.c424;

public class Constant {
    //连接状态
    public static final int UN_CONNECT = 0;
    public static final int CONNECTING = 1;
    public static final int CONNECTED = 2;

    //广告状态
    public static final int Loading = 1;
    public static final int Has_Cache = 2;
    public static final int No_Cache = 3;

    public static final String START_AD_LOCATION = "start";
    public static final String HOME_AD_LOCATION = "home";
    public static final String FINISH_AD_LOCATION = "finish";
    public static final String REPORT_AD_LOCATION = "report";

    public static final String OPEN_AD_TYPE = "admob_start";
    public static final String INT_AD_TYPE = "admob_int";
    public static final String NAV_AD_TYPE = "admob_nav";
    public static final String BAN_AD_TYPE = "admob_ban";

    public static final int CLOSE_AD_ACTION_FINISH = 0;
    public static final int CLOSE_AD_ACTION_JUMP = 1;
    public static final int CLOSE_AD_ACTION_JUMP_FINISH = 2;

    public static final int Launch_Page_Show_Start = 0;
    public static final int Launch_Page_Show_Finish = 1;
    public static final int Launch_Page_Into_Next = 2;
}
