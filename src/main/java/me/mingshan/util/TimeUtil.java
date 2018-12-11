/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.mingshan.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides common methods to operate time with JDK8.
 *
 * @author mingshan
 */
public class TimeUtil {
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = TimeFormat.LONG_DATE_PATTERN_LINE_24.formatter;

    private TimeUtil() { throw new UnsupportedOperationException("It's prohibited to create instances of the class."); }

    /**
     * Gets the current datetime by default format.
     *
     * @return the current datetime
     */
    public static String getCurrentDatetime() {
        return DEFAULT_DATE_TIME_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * Gets the current datetime by specified format.
     *
     * @param format the specified format
     * @return the current datetime
     */
    public static String getCurrentDatetime(TimeFormat format) {
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
     * @param time the time value to be formatted into a time string
     * @param format the specified format
     * @return the formatted string
     */
    public static String format(LocalDateTime time, TimeFormat format) {
        return format.formatter.format(time);
    }

    /**
     * Obtains an instance of {@code LocalDateTime} from a text string using a specific formatter.
     * <p>
     * The text is parsed using the formatter, returning a date-time.
     *
     * @param text  the text to parse, not null
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
     * @param text  the text to parse, not null
     * @param format  the formatter to use, not null
     * @return the parsed local date-time, not null
     */
    public static LocalDateTime parse(String text, TimeFormat format) {
        return LocalDateTime.parse(text, format.formatter);
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
         * Long time format with millisecond , 12-hour clock
         */
        LONG_DATE_PATTERN_WITH_MILSEC_LINE_12("yyyy-MM-dd hh:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_SLASH_12("yyyy/MM/dd hh:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_DOUBLE_SLASH_12("yyyy\\MM\\dd hh:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_NONE_12("yyyyMMdd hh:mm:ss.SSS"),

        /**
         * Long time format with millisecond , 24-hour clock
         */
        LONG_DATE_PATTERN_WITH_MILSEC_LINE_24("yyyy-MM-dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_SLASH_24("yyyy/MM/dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_DOUBLE_SLASH_24("yyyy\\MM\\dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_NONE_24("yyyyMMdd HH:mm:ss.SSS");

        private transient DateTimeFormatter formatter;

        TimeFormat(String pattern) {
            this.formatter = DateTimeFormatter.ofPattern(pattern);
        }

    }
}
