package com.zkzhou.myframelib.uiframe.widget.pullrefreshrecyclerview;

/**
 * Created by aspsine on 16/3/7.
 */
public interface RefreshTrigger {

    void onStart(boolean automatic, int headerHeight, int finalHeight);

    void onMove(boolean finished, boolean automatic, int moved);

    void onRefresh();

    void onRelease();

    void onComplete(boolean success);

    void onReset();
}
