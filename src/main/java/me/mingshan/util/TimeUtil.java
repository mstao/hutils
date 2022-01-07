/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.mingshan.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Provides common methods to operate time with JDK8.
 *
 * @author mingshan
 */
public class TimeUtil {
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = TimeFormat.LONG_DATE_PATTERN_LINE_24.formatter;

    private TimeUtil() {
        throw new UnsupportedOperationException("It's prohibited to create instances of the class.");
    }

    /**
     * Gets the current datetime by default format.
     *
     * @return the current datetime
     */
    public static String getCurrentDateTime() {
        return DEFAULT_DATE_TIME_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * Gets the current datetime by specified format.
     *
     * @param format the specified format
     * @return the current datetime
     */
    public static String getCurrentDateTime(TimeFormat format) {
        return format.formatter.format(LocalDateTime.now());
    }

    /**
     * Formats a date-time into a date/time string by default format.
     *
     * @param time the time value to be formatted into a time string
     * @return the formatted time string
     */
    public static String format(LocalDateTime time) {
        return DEFAULT_DATE_TIME_FORMATTER.format(time);
    }

    /**
     * Formats a date-time into a date/time string by default format.
     *
     * @param time   the time value to be formatted into a time string
     * @param format the specified format
     * @return the formatted string
     */
    public static String format(LocalDateTime time, TimeFormat format) {
        return format.formatter.format(time);
    }

    /**
     * 格式化日期
     *
     * @param date   日期
     * @param format 格式
     * @return 日期字符串
     */
    public static String format(Date date, TimeFormat format) {
        LocalDateTime localDateTime = date2LocalDateTime(date);

        return format(localDateTime, format);
    }

    /**
     * Obtains an instance of {@code LocalDateTime} from a text string using a specific formatter.
     * <p>
     * The text is parsed using the formatter, returning a date-time.
     *
     * @param text the text to parse, not null
     * @return the parsed local date-time, not null
     */
    public static LocalDateTime parse(String text) {
        return LocalDateTime.parse(text, DEFAULT_DATE_TIME_FORMATTER);
    }

    /**
     * Obtains an instance of {@code LocalDateTime} from a text string using a specific formatter.
     * <p>
     * The text is parsed using the formatter, returning a date-time.
     *
     * @param text   the text to parse, not null
     * @param format the formatter to use, not null
     * @return the parsed local date-time, not null
     */
    public static LocalDateTime parse(String text, TimeFormat format) {
        return LocalDateTime.parse(text, format.formatter);
    }

    /**
     * 解析时间
     *
     * @param text   时间字符串
     * @param format 格式化
     * @return 时间
     */
    public static Date parseDate(String text, TimeFormat format) {
        LocalDateTime parse = parse(text, format);
        return localDateTime2Date(parse);
    }

    /**
     * Date to LocalDateTime
     *
     * @param date the specified date, not null
     * @return the parsed local date-time, not null
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * LocalDateTime to Date
     *
     * @param localDateTime the specified local date-time, not null
     * @return the parsed date, not null
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * Date to LocalDate
     *
     * @param date the date
     * @return the LocalDateTime
     */
    public static LocalDate date2LocalDate(Date date) {
        LocalDateTime localDateTime = date2LocalDateTime(date);
        return localDateTime.toLocalDate();
    }

    /**
     * 日期相隔天数
     *
     * @param startDateInclusive 开始时间
     * @param endDateExclusive   结束时间
     * @return 天数
     */
    public static int periodDays(LocalDate startDateInclusive, LocalDate endDateExclusive) {
        return (int) (endDateExclusive.toEpochDay() - startDateInclusive.toEpochDay());
    }

    /**
     * 日期相隔天数
     *
     * @param startDateInclusive 开始时间
     * @param endDateExclusive   结束时间
     * @return 天数
     */
    public static int periodDays(Date startDateInclusive, Date endDateExclusive) {
        LocalDate startLocalDate = date2LocalDate(startDateInclusive);
        LocalDate endLocalDate = date2LocalDate(endDateExclusive);

        return (int) (endLocalDate.toEpochDay() - startLocalDate.toEpochDay());
    }

    /**
     * 传入时间是否在当天之前
     *
     * @param date 传入时间
     * @return true，传入时间在当天之前；false，传入时间在当天之后或是当天
     */
    public static boolean isBeforeToday(Date date) {
        LocalDate now = LocalDate.now();
        return date2LocalDate(date).isBefore(now);
    }

    /**
     * 传入时间是否在当天之后
     *
     * @param date 传入时间
     * @return true，传入时间在当天之后；false，传入时间在当天之前或当天
     */
    public static boolean isAfterToday(Date date) {
        LocalDate now = LocalDate.now();
        return date2LocalDate(date).isAfter(now);
    }

    public static Date localDate2Date(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * 获取当前的前n天时间
     *
     * @param beforeDays 前n天
     * @return 当前的前n天时间
     */
    public static Date beforeDay(int beforeDays) {
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.minusDays(beforeDays);
        return localDate2Date(localDate);
    }

    /**
     * 获取传入日期的起始时间，比如传入2021-08-09 12:00:00
     * <p>
     * 返回值为 2021-08-09 00:00:00
     *
     * @param date 传入日期
     * @return 传入日期的起始时间
     */
    public static Date dayOfStartTime(Date date) {
        LocalDateTime beginTime = LocalDateTime.of(date2LocalDate(date), LocalTime.MIN);
        return localDateTime2Date(beginTime);
    }

    /**
     * 获取传入日期的结束时间，比如传入2021-08-09 12:00:00
     * <p>
     * 返回值为 2021-08-09 23:59:59
     *
     * @param date 传入日期
     * @return 传入日期的结束时间
     */
    public static Date dayOfEndTime(Date date) {
        LocalDateTime endTime = LocalDateTime.of(date2LocalDate(date), LocalTime.MAX);
        return localDateTime2Date(endTime);
    }

    /**
     * The format of time
     */
    public enum TimeFormat {

        /**
         * Short time format
         */
        SHORT_DATE_PATTERN_NONE("yyyyMMdd"),
        SHORT_DATE_PATTERN_LINE("yyyy-MM-dd"),
        SHORT_DATE_PATTERN_SLASH("yyyy/MM/dd"),
        SHORT_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd"),

        /**
         * Long time format, 12-hour clock
         */
        LONG_DATE_PATTERN_NONE_12("yyyyMMdd hh:mm:ss"),
        LONG_DATE_PATTERN_LINE_12("yyyy-MM-dd hh:mm:ss"),
        LONG_DATE_PATTERN_SLASH_12("yyyy/MM/dd hh:mm:ss"),
        LONG_DATE_PATTERN_DOUBLE_SLASH_12("yyyy\\MM\\dd hh:mm:ss"),

        /**
         * Long time format, 24-hour clock
         */
        LONG_DATE_PATTERN_NONE_24("yyyyMMdd HH:mm:ss"),
        LONG_DATE_PATTERN_LINE_24("yyyy-MM-dd HH:mm:ss"),
        LONG_DATE_PATTERN_SLASH_24("yyyy/MM/dd HH:mm:ss"),
        LONG_DATE_PATTERN_DOUBLE_SLASH_24("yyyy\\MM\\dd HH:mm:ss"),

        /**
         * Long time format with millisecond, 12-hour clock
         */
        LONG_DATE_PATTERN_WITH_MILSEC_LINE_12("yyyy-MM-dd hh:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_SLASH_12("yyyy/MM/dd hh:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_DOUBLE_SLASH_12("yyyy\\MM\\dd hh:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_NONE_12("yyyyMMdd hh:mm:ss.SSS"),

        /**
         * Long time format with millisecond, 24-hour clock
         */
        LONG_DATE_PATTERN_WITH_MILSEC_LINE_24("yyyy-MM-dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_SLASH_24("yyyy/MM/dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_DOUBLE_SLASH_24("yyyy\\MM\\dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_NONE_24("yyyyMMdd HH:mm:ss.SSS");

        private transient DateTimeFormatter formatter;
        private String pattern;

        TimeFormat(String pattern) {
            this.formatter = DateTimeFormatter.ofPattern(pattern);
            this.pattern = pattern;
        }

        public String getPattern() {
            return pattern;
        }
    }
}
