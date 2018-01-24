package com.freightcom.api.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FormattingUtil
{
    public static String DECIMAL_2_PLACES_PATTERN = "####.00";
    public static String DECIMAL_2_PLACES_PATTERN_LENGTH_8 = "#####.00";

    private static SimpleDateFormat BASIC_DATE_FORMAT = new SimpleDateFormat("MMMMM dd, yyyy");
    public static SimpleDateFormat DATE_FORMAT_YYMMDD = new SimpleDateFormat("yyMMdd");
    public static SimpleDateFormat DATE_FORMAT_yyyyMMDD = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat DATE_FORMAT_YYYYMMDD = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat DATE_FORMAT_MMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");
    public static SimpleDateFormat DATE_FORMAT_DDMMYYYY = new SimpleDateFormat("ddMMyyyy");
    public static SimpleDateFormat DATE_FORMAT_DDMMMYYYY = new SimpleDateFormat("dd/MMM/yyyy");
    public static SimpleDateFormat DATE_FORMAT_WEB = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat TIME_FORMAT_WEB = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat PUROLATOR_LIVE_DATE_FORMAT_MMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");
    public static SimpleDateFormat DATE_FORMAT_YYMMDD_HHMM = new SimpleDateFormat("yyMMdd HHmm");
    public static SimpleDateFormat DATE_FORMAT_YYMMDDHHMM = new SimpleDateFormat("yyMMddHHmm");
    public static SimpleDateFormat DATE_FORMAT_YYYYMMDD_HHMMSS = new SimpleDateFormat("yyyyMMdd HHmmss");
    public static SimpleDateFormat DATE_FORMAT_DDMMMYY = new SimpleDateFormat("ddMMMyy");
    public static SimpleDateFormat DATE_FORMAT_DDMMMYY_HHMM = new SimpleDateFormat("ddMMMyy HH:mm");

    public static SimpleDateFormat DATE_FORMAT_YYYY_MM_DD_HHMM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat DATE_FORMAT_MMDDYYYY_HHMMSS = new SimpleDateFormat("MMddyyyyHHmmss");

    public static SimpleDateFormat DATE_FORMAT_YYYYMMDDHHMM = new SimpleDateFormat("yyyyMMddHHmm");
    public static SimpleDateFormat DATE_FORMAT_YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

    public static SimpleDateFormat DATE_FORMAT_MMDDYYYY_HHMM = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    public static SimpleDateFormat MG_LABEL_DATE_FORMAT = new SimpleDateFormat("MMMMM d, yyyy");
    public static final NumberFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat("#.0#################");
    public static final BigDecimal ZERO = new BigDecimal("0");

    public static SimpleDateFormat DATE_FORMAT_DDMMMYYYY_DASH = new SimpleDateFormat("dd-MMM-yyyy");

    private static final String PLAIN_ASCII = "AaEeIiOoUu" // grave
            + "AaEeIiOoUuYy" // acute
            + "AaEeIiOoUuYy" // circumflex
            + "AaEeIiOoUuYy" // tilde
            + "AaEeIiOoUuYy" // umlaut
            + "Aa" // ring
            + "Cc" // cedilla
    ;

    private static final String UNICODE = "\u00C0\u00E0\u00C8\u00E8\u00CC\u00EC\u00D2\u00F2\u00D9\u00F9"
            + "\u00C1\u00E1\u00C9\u00E9\u00CD\u00ED\u00D3\u00F3\u00DA\u00FA\u00DD\u00FD"
            + "\u00C2\u00E2\u00CA\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177"
            + "\u00C2\u00E2\u00CA\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177"
            + "\u00C4\u00E4\u00CB\u00EB\u00CF\u00EF\u00D6\u00F6\u00DC\u00FC\u0178\u00FF" + "\u00C5\u00E5"
            + "\u00C7\u00E7";

    public static String formatDecimalTo2Places(float val, String pattern)
    {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(val);
    }

    public static String formatDecimalTo2Places(double val, String pattern)
    {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(val);
    }

    public static String getBasicDateFormat(Date d)
    {
        return BASIC_DATE_FORMAT.format(d);
    }

    public static String getFormattedDate(ZonedDateTime zonedDateTime, SimpleDateFormat sdf)
    {
        return zonedDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String getFormattedDate(Date d, String timeZone, SimpleDateFormat sdf)
    {
        TimeZone t = sdf.getTimeZone();
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String date = sdf.format(d);
        sdf.setTimeZone(t);
        return date;
    }

    public static Date getDate(String date)
    {
        try {
            return DATE_FORMAT_WEB.parse(date);
        } catch (Exception e) {
            return new Date();
        }
    }

    public static Date getDate(String date, SimpleDateFormat sdf)
    {
        try {
            Date d = sdf.parse(date);
            return d;
        } catch (Exception e) {
            return new Date();
        }
    }

    public static String getDateValueAhead(Date date, int numOfDays, boolean start)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, numOfDays);
        return getDateValue(c.getTime(), start);
    }

    private static String getDateValue(Date date, boolean start)
    {
        StringBuilder stb = new StringBuilder();
        stb.append(DATE_FORMAT_WEB.format(date));
        if (start)
            stb.append(" 00:00");
        else
            stb.append(" 23:59");

        return stb.toString();
    }

    public static String removeLeadingZeros(String s)
    {

        if (s == null || s.length() == 0)
            return "";

        String strTrimmed = null;
        char[] carr = s.toCharArray();
        int iEnd = 0;
        // need this only if string begins with zero
        if (carr[0] == '0') {
            for (int k = 1; k < carr.length; k++) {
                if (carr[k] != '0') {
                    iEnd = k - 1;
                    break;
                }
            } // for loop ends
            strTrimmed = s.substring(iEnd + 1, s.length());
            return strTrimmed;
        } // if ends">
        return s;
    }

    public static String truncateString(String value, int length)
    {
        if (value != null && value.length() > length)
            value = value.substring(0, length);
        return value;
    }

    public static String maximizeStringLength(String s, int maxLength)
    {
        if (s == null)
            return "";
        if (s.length() <= maxLength)
            return s;

        return s.substring(0, maxLength);
    }

    public static final String cleanPhoneNum(String phone)
    {
        if (phone == null || phone.length() == 0)
            return "";

        return (phone.replaceAll("-", "")
                .replaceAll(" ", ""));
    }

    // remove accentuated from a string and replace with ascii equivalent
    public static String convertNonAscii(String s)
    {
        if (s == null)
            return s;

        StringBuffer sb = new StringBuffer();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            int pos = UNICODE.indexOf(c);
            if (pos > -1) {
                sb.append(PLAIN_ASCII.charAt(pos));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static double add(double a, double b)
    {
        String s = DEFAULT_DECIMAL_FORMAT.format(a);
        BigDecimal bd = new BigDecimal(s);
        return add(bd, b).doubleValue();
    }

    public static BigDecimal add(BigDecimal a, double b)
    {
        String s = DEFAULT_DECIMAL_FORMAT.format(b);
        BigDecimal bd = new BigDecimal(s);
        return add(a, bd);
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b)
    {
        if (a == null)
            return (b == null) ? ZERO : b;
        return a.add(b);
    }

    public static double formatDecimalToPrecision(double amount, int decimal)
    {

        double p = (double) Math.pow(10, decimal);
        amount = amount * p;
        double tmp = Math.round(amount);
        return (double) tmp / p;
    }

    public static int getWeekends(Calendar start, Calendar end)
    {
        Calendar date = (Calendar) start.clone();
        int daysBetween = 0;
        while (date.before(end) && !(date.equals(end))) {

            int day = date.get(Calendar.DAY_OF_WEEK);
            if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
                daysBetween = daysBetween + 2;
                date.add(Calendar.DAY_OF_MONTH, 1);
            }
            date.add(Calendar.DAY_OF_MONTH, 1);
        }
        return daysBetween;
    }

    public static int daysBetween(Calendar startDate, Calendar endDate, int myFlag)
    {
        Calendar date = (Calendar) startDate.clone();
        int daysBetween = 0;

        switch (myFlag) {
        case 1:
            // To Calculate days between excluding weekends
            while (date.before(endDate)) {

                int day = date.get(Calendar.DAY_OF_WEEK);
                if (!(day == Calendar.SATURDAY || day == Calendar.SUNDAY)) {
                    daysBetween++;
                }
                date.add(Calendar.DAY_OF_MONTH, 1);
            }
            break;

        case 2:
            // To Calculate whole days in between
            while (date.before(endDate)) {
                date.add(Calendar.DAY_OF_MONTH, 1);
                daysBetween++;
            }
            break;

        }
        return daysBetween;
    }

    public static boolean isWorkingDay(Calendar currentDate)
    {
        if (currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return false;
        } else if (currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return false;
        } else {
            return true;
        }
    }

    // Replacing all Special Characters for XML data
    public static String removeSpecialCharsForXML(String str)
    {
        if (str == null)
            return "";
        String result = str;
        if (result != null) {
            if (result.contains("&"))
                result = result.replaceAll("&", "&amp;");
            if (result.contains("\""))
                result = result.replaceAll("\"", "");
            if (result.contains("<"))
                result = result.replaceAll("<", "&lt;");
            if (result.contains(">"))
                result = result.replaceAll(">", "&gt;");
        }
        return result;
    }

    /* CCT CARRIER DATE FORMAT IN CSV */
    public static String getDateForCCT(String date)
    {
        String ndate;
        try {
            if (date.equals("") || date == "")
                date = "0000-00-00";

            String yyyy = date.substring(0, 4);
            String mm = date.substring(4, 6);
            String dd = date.substring(6, 8);
            ndate = yyyy + "-" + mm + "-" + dd;

            return ndate;
        } catch (Exception e) {
            return date;
        }
    }

    /* CCT CARRIER TIME FORMAT IN CSV */
    public static String getTimeForCCT(String time)
    {
        try {

            if (time.equals("") || time == "")
                time = "00:00";
            if (time.length() == 3)
                time = "0".concat(time);

            String hh = time.substring(0, 2);
            String mm = time.substring(2, 4);
            String ntime = hh + ":" + mm;
            return ntime;
        } catch (Exception e) {
            return "00:00";
        }
    }

    // to round figure the rates upto 2 decimal places
    public static Double roundFigureRates(double rateValue, int digitsAfterDecimal)
    {
        BigDecimal value = new BigDecimal(rateValue);
        value = value.setScale(digitsAfterDecimal, BigDecimal.ROUND_HALF_UP);

        return formatDecimalTo2PlacesDouble(value.doubleValue());
    }

    public static Double formatDecimalTo2PlacesDouble(double val)
    {
        String s = formatDecimalTo2Places(val, FormattingUtil.DECIMAL_2_PLACES_PATTERN);
        return Double.valueOf(s);
    }

}
