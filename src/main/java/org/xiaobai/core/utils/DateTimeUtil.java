package org.xiaobai.core.utils;

import org.xiaobai.core.enums.DateTypeEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @ClassName DateTimeUtil
 * @Description 时间工具类
 * @Author dingfeng.xiao
 * @Date 2023/7/10 10:38
 * @Version 1.0
 */
public class DateTimeUtil {

    /*
    从现在开始 ，获取n 天后的日期
     */
    public static Date nowNextDay(Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, day);

        return calendar.getTime();
    }

    /*
    从现在开始 ，获取count type后的日期,比如两周，两年
 */
    public static Date nextTime(Integer count, DateTypeEnum type) {
        return nextTime(null, count, type);
    }

    /*
从rDate开始 ，获取count type后的日期,比如两周，两年
*/
    public static Date nextTime(Date rDate, Integer count, DateTypeEnum type) {
        Calendar calendar = Calendar.getInstance();
        if (Objects.nonNull(rDate)) {
            calendar.setTime(rDate);
        }
        switch (type) {
            case YEAR: {
                calendar.add(Calendar.YEAR, count);
            } break;
            case MONTH: {
                calendar.add(Calendar.MONTH, count);
            } break;
            case WEEK: {
                calendar.add(Calendar.WEEK_OF_YEAR, count);
            } break;
            case DAY: {
                calendar.add(Calendar.DAY_OF_YEAR, count);
            } break;
            case HOUR: {
                calendar.add(Calendar.HOUR, count);
            } break;
            case MINUTE: {
                calendar.add(Calendar.MINUTE, count);
            } break;
            case SECOND: {
                calendar.add(Calendar.SECOND, count);
            } break;
        }
        return calendar.getTime();
    }

    /**
     * 格式化日期
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
       return dateFormat.format(date);
    }

    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当天日期 yyyy-MM-dd
     * @return String
     */
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    /**
     * 获取当天开始日期
     * @return
     */
    public static Date currentDayStart() {
        LocalDate today = LocalDate.now();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDateTime startDateTime = LocalDateTime.of(today, midnight);
        return java.sql.Timestamp.valueOf(startDateTime);
    }

    /**
     * 获取当月开始日期
     * @return
     */
    public static Date currentMonthStart() {
        YearMonth currentYearMonth = YearMonth.now();
        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        LocalDateTime startDateTime = LocalDateTime.of(firstDayOfMonth, LocalDateTime.MIN.toLocalTime());
        return java.sql.Timestamp.valueOf(startDateTime);
    }
}
