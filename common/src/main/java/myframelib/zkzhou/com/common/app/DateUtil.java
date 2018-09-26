
package myframelib.zkzhou.com.common.app;

import android.content.Context;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String[] zodiacArr = {
            "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"
    };

    public static final String[] constellationArr = {
            "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"
    };

    public static final int[] constellationEdgeDay = {
            20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22
    };

    /**
     * 根据日期获取星座
     * 
     * @param time
     * @return
     */
    public static String date2Constellation(Calendar time) {
        int month = time.get(Calendar.MONTH);
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArr[month];
        }
        // default to return 魔羯
        return constellationArr[11];
    }

    /**
     * 根据日期获取生肖
     * 
     * @return
     */
    public static String date2Zodica(Calendar time) {
        return zodiacArr[time.get(Calendar.YEAR) % 12];
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        return formater.format(date);
    }

    /**
     * 格式化日期
     * 
     * @param datetime
     * @param format
     * @return
     */
    public static String formatDate(String datetime, String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            Date date = formater.parse(datetime);
            return formater.format(date);
        } catch (ParseException e) {
            return datetime;
        }
    }

    public static String getCurDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
    
    public static String getCurDate(){
        return formatDate(new Date(), "yyyy-MM-dd");
    }

    /**
     * 返回指定格式的日期
     * 
     * @param format
     * @param milliseconds 毫秒数
     * @return
     */
    public static String getDate(String format, long milliseconds) {
        try {
            SimpleDateFormat formater = new SimpleDateFormat(format);
            return formater.format(new Date(milliseconds));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 生成一个日期
     * 
     * @param datetime　基准日期
     * @param format　日期格式
     * @param days　与基准日期相差的天数
     * @return
     */
    public static String getDate(String datetime, String format, int days) {
        String newDate = "";
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formater.parse(datetime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, days);
            newDate = formater.format(calendar.getTime());
        } catch (ParseException e) {
        }
        return newDate;
    }


    /**
     * 根据一个日期，取得时分秒等信息
     * 
     * @param time　
     * @param field　Calendar字段
     * @return
     */
    public static int getDateField(long time, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(field);
    }

    /**
     * 检测第一个时间是否在第二个时间之前
     * 
     * @param firstDate
     * @param secondDate
     * @return 第一个时间在第二个时间之前则返回true
     */
    public static boolean isFirstDateBeforeSecondDate(long firstDate, long secondDate) {
        Calendar firstCalendar = Calendar.getInstance();
        firstCalendar.setTimeInMillis(firstDate);
        Calendar secondCalendar = Calendar.getInstance();
        secondCalendar.setTimeInMillis(secondDate);
        int result = firstCalendar.compareTo(secondCalendar);
        if (result <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测是否在兩個時間段內
     *
     * @param start
     * @param end
     * @return
     */
    public static boolean isBetweenDates(Date start, Date end){
        Calendar firstCalendar = Calendar.getInstance();
        firstCalendar.setTime(start);
        Calendar secondCalendar = Calendar.getInstance();
        secondCalendar.setTime(end);
        Calendar target = Calendar.getInstance();
        target.setTime(new Date(System.currentTimeMillis()));

        int result1 = firstCalendar.compareTo(target);
        int result2 = secondCalendar.compareTo(target);
        if(result1 <= 0 && result2 >= 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 时间转化字符串
     * 
     * @param context
     * @param secs 秒
     * @return
     */
    public static String makeTimeString(Context context, long secs) {
        String durationformat = secs < 3600 ? "%2$d:%5$02d" : "%1$d:%3$02d:%5$02d";

        final Object[] timeArgs = new Object[5];
        timeArgs[0] = secs / 3600;
        timeArgs[1] = secs / 60;
        timeArgs[2] = (secs / 60) % 60;
        timeArgs[3] = secs;
        timeArgs[4] = secs % 60;

        return String.format(durationformat, timeArgs);
    }

    public static long parseDateTime(String datetime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (!TextUtils.isEmpty(datetime)) {
                return dateFormat.parse(datetime).getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 格式化 MM-dd HH:mm
     * 
     * @param time 时间戳，单位是秒
     */
    public static String formatDate(int time) {
        if (time <= 0) {
            return "";
        }
        Date nowTime = new Date(time * 1000L);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(nowTime);
    }

    /**
     * 格式化 MM-dd HH:mm
     * 
     * @param time 时间戳，单位是毫秒
     */
    public static String formatDate(long time) {
        if (time <= 0) {
            return "";
        }
        Date nowTime = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(nowTime);
    }

    /**
     * 格式化 毫秒数: 00:00
     */
    public static String formatMillisSecond(long l) {
        long h = l / (1000 * 60 * 60) % 60;
        long m = l / (1000 * 60) % 60;
        long s = l / 1000 % 60;
        if (h > 0) {
            return String.format("%02d:%02d:%02d", h, m, s);
        } else {
            return String.format("%02d:%02d", m, s);
        }
    }

    /**
     * 格式化 秒数: 00:00
     */
    public static String formatSecond(long second) {
        long h = second / (60 * 60);
        long m = (second - 60 * h) / 60;
        long s = second % 60;
        String result = "";
        if (h > 0) {
            result = String.format("%02d:%02d:%02d", h, m, s);
        } else {
            result = String.format("%02d:%02d", m, s);
        }
        return result;
    }

    /**
     * 格式化 秒数: 00:00 倒计时
     * 
     * @param time
     * @return
     */
    public static String phpTimeTransform(long time) {
        long diff = System.currentTimeMillis() / 1000 - time;
        return timeTransform((int) diff);
    }

    /**
     * 根据时间转换成相应的字符串提示语
     * 
     * @param time 时间（单位:秒）
     * @return
     */
    public static String timeTransform(long time) {
        String s = "刚刚";
        if (time < 60) {
            s = "刚刚";
        } else if (time > 59 && time < 3600) {
            s = time / 60 + "分钟前";
        } else if (time > 3599 && time < 3600 * 24) {
            s = time / 3600 + "小时前";
        } else if (time > 86399) {
            s = time / 86400 + "天前";
        }
        return s;
    }


    /**
     * 将getMediaTime(),getDurationTime()获得的时间 转为 以分:秒为单位
     *
     * @param time
     * @return
     */
    public static String toMinuteAndSecondMode(long time) {
        time = time / 1000;
        long minute = time / 60;
        long second = time % 60;
        StringBuffer sb = new StringBuffer();
        if (minute < 10) {
            sb.append('0');
        }
        sb.append(minute);
        sb.append(':');
        if (second < 10) {
            sb.append('0');
        }
        sb.append(second);
        return sb.toString();
    }

    /**
     * 把秒转换成YYYY-MM-DD
     *
     * @param sec
     * @return
     */
    public static String getDateTimeBySecond(long sec) {
        Date date = new Date(sec * 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(date);
        return time;
    }


    public static String getTimeDiff(long standard, long time) {
        long timeDistance = standard - time;
        if (timeDistance < 60 * 60) {
            long temp = timeDistance / 60;
            if (temp == 0) {
                temp = 1;
            }
            if (temp <= 1) {
                return "刚刚";
            } else {
                String minute = String.valueOf(temp);
                return minute + "分钟前";
            }
        } else if (timeDistance >= 60 * 60 && timeDistance < 24 * 60 * 60) {
            String hour = String.valueOf(timeDistance / (60 * 60));
            return hour + "小时前";
        } else if (timeDistance >= 24 * 60 * 60 && timeDistance < 31 * 24 * 60 * 60) {
            String day = String.valueOf(timeDistance / (24 * 60 * 60));
            return day + "天前";
        } else if (timeDistance >= 31 * 24 * 60 * 60) {
            return "30天前";
        }
        return "刚刚";
    }


    public static String getTimeforDate(long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        Date date = new Date();
        calendar.setTime(date);
        int taday = calendar.get(Calendar.DATE);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        String hourstr = String.valueOf(hour);
        if (hour == 0) {
            hourstr = "00";
        } else if (String.valueOf(hour).length() == 1) {
            hourstr = "0" + hour;
        }
        String minutesstr = String.valueOf(minutes);
        if (minutes == 0) {
            minutesstr = "00";
        } else if (String.valueOf(minutes).length() == 1) {
            minutesstr = "0" + minutes;
        }
        long timeDistance = (System.currentTimeMillis() - timeMillis) / 1000; // 秒
        if (timeDistance > 0 && timeDistance < 60 * 60) {
            long temp = timeDistance / 60;
            if (temp == 0) {
                temp = 1;
            }
            if (temp < 5 * 60) {
                return "刚刚";
            } else {
                String minute = String.valueOf(temp);
                return minute + "分钟前";
            }
        } else if (currentYear == year && month == currentMonth && day == taday) {
            return "今天" + hourstr + ":" + minutesstr;
        } else if (currentYear == year && month == currentMonth && day == taday - 1) {
            return "昨天" + hourstr + ":" + minutesstr;
        } else if (currentYear == year) {
            return month + "-" + day;
        } else {
            return year + "-" + month + "-" + day;
        }
    }

    /**
     * 处理格式yyyy-MM-dd HH:mm:ss的时间
     *
     * @param date
     * @return
     */
    public static String getTimeforDate(String date) {
        try {
            if (TextUtils.isEmpty(date)) {
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            }
            Date dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
            return getTimeforDate(dateTime.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String getTimeString(long distance) {
        long time = System.currentTimeMillis() / 1000;
        long timeDistance = time - distance;
        if (timeDistance < 60 * 60) {
            long temp = timeDistance / 60;
            if (temp == 0) {
                temp = 1;
            }
            if (temp < 1) {
                return "刚刚";
            } else {
                String minute = String.valueOf(temp);
                return minute + "分钟前";
            }
        } else if (timeDistance >= 60 * 60 && timeDistance < 24 * 60 * 60) {
            String hour = String.valueOf(timeDistance / (60 * 60));
            return hour + "小时前";
        } else if (timeDistance >= 24 * 60 * 60 && timeDistance < 31 * 24 * 60 * 60) {
            String day = String.valueOf(timeDistance / (24 * 60 * 60));
            return day + "天前";
        } else if (timeDistance >= 31 * 24 * 60 * 60) {
            return "30天前";
        }
        return "刚刚";
    }


}
