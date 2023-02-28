package com.example.c424.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.c424.bean.AdConfig;
import com.example.c424.bean.AdInfo;
import com.example.c424.bean.AdvertisementInfo;
import com.example.c424.bean.ConnectType;
import com.example.c424.bean.GlobalConfig;
import com.example.c424.bean.UpdateConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.network.crypt.NativeLib;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class SpUtil {
    private static SharedPreferences getSP(Context context, String spName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static boolean isFirstLaunch(Context context) {
        try {
            SharedPreferences sharedPreferences = getSP(context, "FirstLaunch");
            return sharedPreferences.getBoolean("FirstLaunch", true);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("isFirstLaunch Exception:" + e.getMessage());
        }
        return true;
    }

    public static void setNotFirstLaunch(Context context) {
        try {
            SharedPreferences sharedPreferences = getSP(context, "FirstLaunch");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("FirstLaunch", false);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("setNotFirstLaunch Exception:" + e.getMessage());
        }
    }

    public static void saveConfig(Context context, GlobalConfig config) {
        try {
            SharedPreferences sharedPreferences = getSP(context, "Config");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String jsonStr = new Gson().toJson(config);
            editor.putString("Config", jsonStr);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("saveConfig Exception:" + e.getMessage());
        }
    }

    public static GlobalConfig getConfig(Context context) {
        try {
            SharedPreferences sharedPreferences = getSP(context, "Config");
            String jsonStr = sharedPreferences.getString("Config", "");
            if (jsonStr == null || jsonStr.isEmpty()) {
                jsonStr = NativeLib.getLocalConfig();
            }
            if (jsonStr != null && !jsonStr.isEmpty()) {
                GlobalConfig config = new Gson().fromJson(jsonStr, new TypeToken<GlobalConfig>() {
                }.getType());
//                LogUtil.d("config ::::" + config.toString());
                if (config != null) {
                    return config;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getConfig Exception:" + e.getMessage());
        }
        return null;
    }

    public static List<String> getPriorityCountry(Context context) {
        List<String> priorityCountryList = new ArrayList<>();
        try {
            GlobalConfig globalConfig = getConfig(context);
            String[] countryArray = globalConfig.getPriority_rocketspeedup_Connect().split(",");
            if (countryArray != null && countryArray.length > 0) {
                for (int i = 0; i < countryArray.length; i++) {
                    priorityCountryList.add(countryArray[i].toUpperCase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getPriorityCountry Exception:" + e.getMessage());
        }
        return priorityCountryList;
    }

    public static List<ConnectType> getConnectType(Context context) {
        List<ConnectType> connectTypeList = new ArrayList<>();
        try {
            GlobalConfig globalConfig = getConfig(context);
            for (int i = 0; i < globalConfig.getStrategyList().size(); i++) {
                connectTypeList.addAll(globalConfig.getStrategyList().get(i).getTacticsConfig());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getConnectType Exception:" + e.getMessage());
        }
        return connectTypeList;
    }

    public static HashSet<String> getBlackList(Context context) {
        HashSet<String> pkgSet = new HashSet<>();
        try {
            GlobalConfig config = getConfig(context);
            String pkgStr = config.getRocketspeedup_BlackLists();

            if (pkgStr != null && !pkgStr.isEmpty()) {
                String[] pkgArray = pkgStr.split(",");
                if (pkgArray != null && pkgArray.length > 0) {
                    for (int i = 0; i < pkgArray.length; i++) {
                        pkgSet.add(pkgArray[i]);
                    }
                    return pkgSet;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getBlackList Exception:" + e.getMessage());
        }
        return pkgSet;
    }

    public static int getMaxConnectTime(Context context) {
        try {
            GlobalConfig globalConfig = getConfig(context);
            return globalConfig.getMax_rocketspeedup_Connect();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getMaxConnectTime:" + e.getMessage());
        }
        return 60;
    }

    public static int getConfigInterval(Context context) {
        try {
            GlobalConfig globalConfig = getConfig(context);
            return globalConfig.getConfig_rocketspeedup_Interval();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getConfigInterval Exception:" + e.getMessage());
        }
        return 60;
    }

    public static AdInfo getAdInfoByLocation(Context context, String location) {
        try {
            GlobalConfig globalConfig = getConfig(context);
            List<AdConfig> adConfigList = globalConfig.getConfigAd();
            if (adConfigList != null) {
                for (int i = 0; i < adConfigList.size(); i++) {
                    String l = adConfigList.get(i).getAdLocation();
                    if (location.equals(l)) {
                        boolean adSwitch = adConfigList.get(i).isCloseAdv();
                        List<AdvertisementInfo> advertisementInfoList = adConfigList.get(i).getInnerAd();
                        for (int j = 0; j < advertisementInfoList.size(); j++) {
                            AdInfo adInfo = new AdInfo(advertisementInfoList.get(j).getAdId(), l, advertisementInfoList.get(j).getAdvFormat(), adSwitch);
                            return adInfo;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getAdInfoByLocation Exception:" + e.getMessage());
        }
        return null;
    }

    public static int getMaxSplashTime(Context context) {
        try {
            GlobalConfig globalConfig = getConfig(context);
            return globalConfig.getStartAnimation_rocketspeedup_Upper();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getMaxSplashTime");
        }
        return 10;
    }

    public static int getMaxNativeAdClickTime(Context context) {
        try {
            GlobalConfig globalConfig = getConfig(context);
            return globalConfig.getNative_rocketspeedup_ClickLimit();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getMaxNativeClickTime Exception:" + e.getMessage());
        }
        return 3;
    }

    public static void setTodayNativeAdClickCount(Context context) {
        try {
            SharedPreferences sharedPreferences = getSP(context, "TodayNativeAdClickCount");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int todayClickCount = getTodayNativeAdClickCount(context);
            todayClickCount = todayClickCount + 1;
            editor.putInt("ClickCount", todayClickCount);
            editor.putLong("ClickTime", System.currentTimeMillis());
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("setTodayNativeAdClickTime Exception:" + e.getMessage());
        }
    }

    public static int getTodayNativeAdClickCount(Context context) {
        try {
            SharedPreferences sharedPreferences = getSP(context, "TodayNativeAdClickCount");
            int clickCount = sharedPreferences.getInt("ClickCount", 0);
            long clickTime = sharedPreferences.getLong("ClickTime", System.currentTimeMillis());
            if (isSameDay(clickTime, System.currentTimeMillis())) {
                return clickCount;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getTodayNativeAdClickTime Exception:" + e.getMessage());
        }
        return 0;
    }

    private static boolean isSameDay(long lastTimestamp, long currentTimestamp) {
        try {
            Calendar lastCalendar = Calendar.getInstance();
            Calendar currentCalendar = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String lastDateStr = df.format(lastTimestamp);
            String currentDateStr = df.format(currentTimestamp);
            Date lastDate = df.parse(lastDateStr);
            Date currentDate = df.parse(currentDateStr);
            lastCalendar.setTime(lastDate);
            currentCalendar.setTime(currentDate);
            if (lastCalendar != null && currentCalendar != null) {
                return lastCalendar.get(Calendar.ERA) == currentCalendar.get(Calendar.ERA)
                        && lastCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
                        && lastCalendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR);
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean getFistLaunchStartAdSwitch(Context context) {
        try {
            GlobalConfig globalConfig = getConfig(context);
            return globalConfig.isStStart_rocketspeedup_Ad();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getFirstLaunchStartAdSwitch Exception:" + e.getMessage());
        }
        return false;
    }

    public static boolean getBackFromReportAdSwitch(Context context) {
        try {
            GlobalConfig globalConfig = getConfig(context);
            return globalConfig.isReport_rocketspeedup_BackAd();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getBackFromReportAdSwitch Exception:" + e.getMessage());
        }
        return false;
    }

    public static boolean getBackFromProxyListAdSwitch(Context context) {
        try {
            GlobalConfig globalConfig = getConfig(context);
            return globalConfig.isList_rocketspeedup_BackAd();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("getBackFromProxyListSwtch Exception:" + e.getMessage());
        }
        return false;
    }

    public static UpdateConfig getUpdateConfig(Context context) {
         try {
             GlobalConfig globalConfig = getConfig(context);
             return globalConfig.getForceConfig();
         } catch (Exception e) {
             e.printStackTrace();
             LogUtil.d("getUpdateConfig Exception:" + e.getMessage());
         }
         return null;
    }

    public static boolean isShowConnectGuide(Context context) {
        try {
            GlobalConfig globalConfig = getConfig(context);
            return globalConfig.isRocketspeedup_Guide();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("isShowConnectGuide Exception:" + e.getMessage());
        }
        return true;
    }
}
