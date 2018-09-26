package myframelib.zkzhou.com.common.app;

import android.view.View;
import android.widget.ListView;

/**
 * @auther zhouzhankun
 * @time 18/9/26 16:09
 **/

public class UICompat {
    public static void fixScrollPosition(ListView listView) {
        if (listView != null) {
            View child0 = listView.getChildAt(0);
            final int position = listView.getFirstVisiblePosition();
            final int y = child0 != null ? child0.getTop() : 0;
            listView.setSelectionFromTop(position, y);
        }
    }
}
