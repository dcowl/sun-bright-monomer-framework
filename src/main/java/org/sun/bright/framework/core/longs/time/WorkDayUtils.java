package org.sun.bright.framework.core.longs.time;

import java.util.Calendar;

/**
 * 工作日计算工具类
 *
 * @author <a href="mailto:2867665887@qq.com">SunlightBright</a>
 * @version 1.0
 */
public class WorkDayUtils {

    private WorkDayUtils() {
    }

    /**
     * 获取日期之间的天数
     */
    public int getDaysBetween(Calendar startCalendar, Calendar endCalendar) {
        // swap dates so that d1 is start and d2 is end
        if (startCalendar.after(endCalendar)) {
            Calendar swap = startCalendar;
            startCalendar = endCalendar;
            endCalendar = swap;
        }
        int days = endCalendar.get(Calendar.DAY_OF_YEAR)
                - startCalendar.get(Calendar.DAY_OF_YEAR);
        int year = endCalendar.get(Calendar.YEAR);
        if (startCalendar.get(Calendar.YEAR) != year) {
            startCalendar = (Calendar) startCalendar.clone();
            do {
                days += startCalendar.getActualMaximum(Calendar.DAY_OF_YEAR);
                startCalendar.add(Calendar.YEAR, 1);
            } while (startCalendar.get(Calendar.YEAR) != year);
        }
        return days;
    }

    /**
     * 获取工作日
     */
    public int getWorkingDay(Calendar startCalendar, Calendar endCalendar) {
        int result;
        if (startCalendar.after(endCalendar)) {
            Calendar swap = startCalendar;
            startCalendar = endCalendar;
            endCalendar = swap;
        }
        // 开始日期的日期偏移量
        int chargeStartDate = 0;
        // 结束日期的日期偏移量
        int chargeEndDate = 0;
        // 日期不在同一个日期内
        int startTmp = 7 - startCalendar.get(Calendar.DAY_OF_WEEK);
        int endTmp = 7 - endCalendar.get(Calendar.DAY_OF_WEEK);
        // 开始日期为星期六和星期日时偏移量为0
        if (startTmp != 0 && startTmp != Calendar.DAY_OF_YEAR) {
            chargeStartDate = startTmp - 1;
        }
        // 结束日期为星期六和星期日时偏移量为0
        if (endTmp != 0 && endTmp != Calendar.DAY_OF_YEAR) {
            chargeEndDate = endTmp - 1;
        }
        result = (getDaysBetween(this.getNextMonday(startCalendar),
                this.getNextMonday(endCalendar)) / 7) * 5 + chargeStartDate - chargeEndDate;
        return result;
    }

    /**
     * 获取中文日期
     */
    public String getChineseWeek(Calendar date) {
        final String[] dayNames = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        return dayNames[dayOfWeek - 1];
    }

    /**
     * 获得日期的下一个星期一的日期
     */
    public Calendar getNextMonday(Calendar date) {
        Calendar result = date;
        do {
            result = (Calendar) result.clone();
            result.add(Calendar.DATE, 1);
        } while (result.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY);
        return result;
    }

    /**
     * 获取休息日
     */
    public int getHolidays(Calendar startCalendar, Calendar endCalendar) {
        return this.getDaysBetween(startCalendar, endCalendar) - this.getWorkingDay(startCalendar, endCalendar);
    }

}
