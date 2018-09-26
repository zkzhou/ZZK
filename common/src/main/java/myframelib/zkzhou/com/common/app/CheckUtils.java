
package myframelib.zkzhou.com.common.app;

import android.text.TextUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CheckUtils {

    public static boolean isAvailable(List list) {
        return list != null && !list.isEmpty();
    }

    public static <T> boolean isAvailable(T[] menus) {
        return menus != null && menus.length != 0;
    }

    public static boolean isAvailable(Map map) {
        return map != null && !map.isEmpty();
    }

    public static boolean isAvailable(String str) {
        return !TextUtils.isEmpty(str);
    }

    public static boolean isAvailable(CharSequence str) {
        return !TextUtils.isEmpty(str);
    }

    public static boolean isAvailable(List list, int i) {
        return isAvailable(list) && list.size() > i;
    }

    /**
     * 判断index是否是集合的有效序列，防止越界
     *
     * @param collection
     * @param index
     * @return true 有效
     */
    public static boolean isIndexAvailable(Collection collection, int index) {
        if (collection == null) {
            return false;
        }
        return (index >= 0 && index < collection.size());
    }
}
