package com.example.c424.utils;

import android.content.Context;
import android.net.TrafficStats;
import android.text.format.Formatter;

import java.math.BigDecimal;

public class SpeedUtil {
    private static long mCurrentTxTotal = 0;    //当前手机发送总的流量
    private static long mCurrentRxTotal = 0;    //当前手机接收总的流量
    private static long mLastTxTotal = 0;        //上次手机发送总的流量
    private static long mLastRxTotal = 0;        //上次手机接收总的流量
    private static long mCurrentTotalUp = 0;    //当前手机总的上行流量
    private static long mCurrentTotalDown = 0;   //当前手机总的下行流量
    private static long mLastTotalUp = 0;        //上次手机总的上行流量
    private static long mLastTotalDown = 0;      //上次手机总的下行流量
    private static long lastTimeStampTotalUp = 0;
    private static long lastTimeStampTotalDown = 0;
    private static long mCurrentUp = 0;         //当前手机指定进程的上行流量
    private static long mCurrentDown = 0;       //当前手机指定进程的下行流量
    private static long mLastUp = 0;           //上次手机指定进程的上行流量
    private static long mLastDown = 0;          //上次手机指定进程的下行流量
    private static long lastTimeStampUp = 0;  //上行时间戳
    private static long lastTimeStampDown = 0;   //下行时间戳

    private static float upSpeed = 0.00F;
    private static float downSpeed = 0.00F;
    private static float defaultPointAmount = 4;         //保留小数的位数，默认为2位


    /**
     * 获取总流量
     *
     * @param resetLast 是否重置上次手机总的流量
     * @return
     */
    public static long getTotalFlow(boolean resetLast) {
        long currentTotalTxBytes = TrafficStats.getTotalTxBytes();
        long currentTotalRxBytes = TrafficStats.getTotalRxBytes();

        if (resetLast) {
            mLastTxTotal = 0;
            mLastRxTotal = 0;
        }
        if (mLastTxTotal == 0) {
            mCurrentTxTotal = 0;
        } else {
            mCurrentTxTotal = currentTotalTxBytes - mLastTxTotal;
        }
        //保存当前的流量总和和上次的时间戳
        mLastTxTotal = currentTotalTxBytes;

        if (mLastRxTotal == 0) {
            mCurrentRxTotal = 0;
        } else {
            mCurrentRxTotal = currentTotalRxBytes - mLastRxTotal;
        }
        //保存当前的流量总和和上次的时间戳
        mLastRxTotal = currentTotalRxBytes;

        return mCurrentTxTotal + mCurrentRxTotal;
    }

    //获取总的上行速度
    public static String getTotalUpSpeed(Context context) {                    //refreshTime : 刷新时间
        long currentTotalTxBytes = TrafficStats.getTotalTxBytes();

        long nowTimeStampTotalUp = System.currentTimeMillis();
        mCurrentTotalUp = currentTotalTxBytes - mLastTotalUp;

        if ((nowTimeStampTotalUp - lastTimeStampTotalUp) != 0) {
            String byte2FitSize = Formatter.formatFileSize(context, (mCurrentTotalUp * 1000 / (nowTimeStampTotalUp - lastTimeStampTotalUp))) + "/s";//获取每秒上传的数据量


            //计算上传速度
//        totalUpSpeed = new BigDecimal((mCurrentTotalUp/1024)*1000  /  ((nowTimeStampTotalUp-lastTimeStampTotalUp)*1.0)).setScale((int) defaultPointAmount, BigDecimal.ROUND_HALF_UP).floatValue(); //单位： KB/s


            //保存当前的流量总和和上次的时间戳
            mLastTotalUp = currentTotalTxBytes;
            lastTimeStampTotalUp = nowTimeStampTotalUp;

            return byte2FitSize;
        } else {
            return "0 m/s";
        }
    }


    //获取总的下行速度
    public static String getTotalDownloadSpeed(Context context) {
        long currentTotalRxBytes = TrafficStats.getTotalRxBytes();
        long nowTimeStampTotalDown = System.currentTimeMillis();
        mCurrentTotalDown = currentTotalRxBytes - mLastTotalDown;

        if ((nowTimeStampTotalDown - lastTimeStampTotalDown) != 0) {
            String byte2FitSize = Formatter.formatFileSize(context, (mCurrentTotalDown * 1000 / (nowTimeStampTotalDown - lastTimeStampTotalDown))) + "/s";//获取每秒上传的数据量
            //计算下行速度
//        totalDownSpeed = new BigDecimal((mCurrentTotalDown/1024)*1000  /  ((nowTimeStampTotalDown-lastTimeStampTotalDown)*1.0)).setScale((int) defaultPointAmount,BigDecimal.ROUND_HALF_UP).floatValue(); //单位： KB/s

            //保存当前的流量总和和上次的时间戳
            mLastTotalDown = currentTotalRxBytes;
            lastTimeStampTotalDown = nowTimeStampTotalDown;

            return byte2FitSize;
        } else {
            return "0 m/s";
        }


    }

    //获取指定进程的上行速度
    public static float getUpSpeedByUid(int uid) {
        long currentTxBytes = TrafficStats.getUidTxBytes(uid);
        long nowTimeStampUp = System.currentTimeMillis();
        mCurrentUp = currentTxBytes - mLastUp;

        //计算上传速度
        upSpeed = new BigDecimal((mCurrentUp / 1024) * 1000 / ((nowTimeStampUp - lastTimeStampUp) * 1.0)).setScale((int) defaultPointAmount, BigDecimal.ROUND_HALF_UP).floatValue(); //单位： KB/s
        //保存当前的流量总和和上次的时间戳
        mLastUp = currentTxBytes;
        lastTimeStampUp = nowTimeStampUp;

        return upSpeed;
    }

    //获取总的下行速度
    public static float getDownloadSpeedByUid(int uid) {
        long currentRxBytes = TrafficStats.getUidRxBytes(uid);
        long nowTimeStampDown = System.currentTimeMillis();
        mCurrentDown = currentRxBytes - mLastDown;

        //计算下行速度
        //downSpeed = numberFormat.format((mCurrentDown/1024)*1000  /  ((nowTimeStampDown-lastTimeStampDown).toFloat()) )   //单元为 KB/s
        downSpeed = new BigDecimal((mCurrentDown / 1024) * 1000 / ((nowTimeStampDown - lastTimeStampDown) * 1.0)).setScale((int) defaultPointAmount, BigDecimal.ROUND_HALF_UP).floatValue(); //单位： KB/s

        //保存当前的流量总和和上次的时间戳
        mLastDown = currentRxBytes;
        lastTimeStampDown = nowTimeStampDown;

        return downSpeed;
    }


//    /**
//     * 字节数转合适大小
//     * <p>保留2位小数</p>
//     *
//     * @param byteNum 字节数
//     * @return 1...1024 unit
//     */
//    public static String byte2FitSize(long byteNum) {
//        if (byteNum < 0) {
//            return "shouldn't be less than zero!";
//        } else if (byteNum < DataUtils.KB) {
//            return String.format(Locale.getDefault(), "%.2fB", (double) byteNum);
//        } else if (byteNum < DataUtils.MB) {
//            return String.format(Locale.getDefault(), "%.2fKB", (double) byteNum / DataUtils.KB);
//        } else if (byteNum < DataUtils.GB) {
//            return String.format(Locale.getDefault(), "%.2fMB", (double) byteNum / DataUtils.MB);
//        } else {
//            return String.format(Locale.getDefault(), "%.2fGB", (double) byteNum / DataUtils.GB);
//        }
//    }
}