package com.example.c424.utils.ad;

import com.example.c424.Constant;

public class LaunchAppAdUtil {
    public static int getLaunchPageResult() {
        if (AdStatusUtil.getInstance().getStartAdStatus() == Constant.Has_Cache) {//start有缓存
            if (AdStatusUtil.getInstance().getHomeAdStatus() == Constant.Has_Cache) {//home有缓存
                return Constant.Launch_Page_Show_Start;
            } else {//home没有缓存
                if (AdStatusUtil.getInstance().isNeedLoadHomeAd()) {//home 需要拉取，但是没有缓存
                    if (AdStatusUtil.getInstance().getHomeAdStatus() != Constant.Loading) {//home拉取失败，所以没有缓存
                        if (AdStatusUtil.getInstance().isShareHomeReport()) {//home report公用
                            if (AdStatusUtil.getInstance().getReportAdStatus() != Constant.Loading) {//home没有缓存，home、report公用，report没有正在拉取(说明已经返回了结果，或者本来就有缓存)
                                return Constant.Launch_Page_Show_Start;
                            }
                        }
                    }
                } else {//home没有缓存，不需要拉取
                    return Constant.Launch_Page_Show_Start;
                }
            }
        } else {//start没有缓存
                if (AdStatusUtil.getInstance().getStartAdStatus() != Constant.Loading) {//start拉取失败，所以没有缓存
                    if (AdStatusUtil.getInstance().isShareFinishToStart()) {//start finish共用
                        if (AdStatusUtil.getInstance().getFinishAdStatus() == Constant.Has_Cache) {//finish有缓存
                            if (AdStatusUtil.getInstance().getHomeAdStatus() == Constant.Has_Cache) {//home有缓存
                                return Constant.Launch_Page_Show_Finish;
                            } else {//home没有缓存
                                if (AdStatusUtil.getInstance().isNeedLoadHomeAd()) {//home 需要拉取，但是没有缓存
                                    if (AdStatusUtil.getInstance().getHomeAdStatus() != Constant.Loading) {//home拉取失败，所以没有缓存
                                        if (AdStatusUtil.getInstance().isShareHomeReport()) {//home report公用
                                            if (AdStatusUtil.getInstance().getReportAdStatus() != Constant.Loading) {//home没有缓存，home、report公用，report没有正在拉取(说明已经返回了结果，或者本来就有缓存)
                                                return Constant.Launch_Page_Show_Finish;
                                            }
                                        }
                                    }
                                } else {//home没有缓存，不需要拉取
                                    return Constant.Launch_Page_Show_Finish;
                                }
                            }
                        } else {//finish没有缓存
                            if (AdStatusUtil.getInstance().getFinishAdStatus() != Constant.Loading) {//finish拉取失败，所以没有缓存
                                if (AdStatusUtil.getInstance().getHomeAdStatus() == Constant.Has_Cache) {//home有缓存
                                    return Constant.Launch_Page_Into_Next;
                                } else {//home没有缓存
                                    if (AdStatusUtil.getInstance().isNeedLoadHomeAd()) {//home 需要拉取，但是没有缓存
                                        if (AdStatusUtil.getInstance().getHomeAdStatus() != Constant.Loading) {//home拉取失败，所以没有缓存
                                            if (AdStatusUtil.getInstance().isShareHomeReport()) {//home report公用
                                                if (AdStatusUtil.getInstance().getReportAdStatus() != Constant.Loading) {//home没有缓存，home、report公用，report没有正在拉取(说明已经返回了结果，或者本来就有缓存)
                                                    return Constant.Launch_Page_Into_Next;
                                                }
                                            }
                                        }
                                    } else {//home没有缓存，不需要拉取
                                        return Constant.Launch_Page_Into_Next;
                                    }
                                }
                            }
                        }
                    } else {//start finish不共用
                        if (AdStatusUtil.getInstance().getHomeAdStatus() == Constant.Has_Cache) {//home有缓存
                            return Constant.Launch_Page_Into_Next;
                        } else {//home没有缓存
                            if (AdStatusUtil.getInstance().isNeedLoadHomeAd()) {//home 需要拉取，但是没有缓存
                                if (AdStatusUtil.getInstance().getHomeAdStatus() != Constant.Loading) {//home拉取失败，所以没有缓存
                                    if (AdStatusUtil.getInstance().isShareHomeReport()) {//home report公用
                                        if (AdStatusUtil.getInstance().getReportAdStatus() != Constant.Loading) {//home没有缓存，home、report公用，report没有正在拉取(说明已经返回了结果，或者本来就有缓存)
                                            return Constant.Launch_Page_Into_Next;
                                        }
                                    }
                                }
                            } else {//home没有缓存，不需要拉取
                                return Constant.Launch_Page_Into_Next;
                            }
                        }
                    }
                }
        }
        return -1;
    }
}
