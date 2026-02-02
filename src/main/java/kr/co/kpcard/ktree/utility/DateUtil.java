package kr.co.kpcard.ktree.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {

	public static String DATETIME_FORMAT = "yyyyMMddHHmmss";

	public static long kSecondsOfDay = 86400;
	public static long kSecondsOfWeek = 604800;

	public final static String[] DAY_OF_WEEK = { "일", "월", "화", "수", "목", "금", "토" };

	private static String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	private static String dateFormat = "yyyy-MM-dd";

	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public static int getCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int getCurrentDay() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getCurrentHour() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static int getCurrentMinute() {

		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MINUTE);
	}

	public static long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

	public static Date addYear(Date date, int years) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);

		return cal.getTime();
	}

	public static Date addMonth(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, amount);

		return cal.getTime();
	}

	public static Date addDay(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, amount);

		return cal.getTime();
	}

	public static Date addHour(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, amount);

		return cal.getTime();
	}

	public static Date addMinute(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, amount);

		return cal.getTime();
	}

	public static Date addSecond(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, amount);

		return cal.getTime();
	}

	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.YEAR);
	}

	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.MONTH) + 1;
	}

	public static int getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.MINUTE);
	}

	public static int getSecond(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.SECOND);
	}

	public static int getDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static int getDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static int getWeekOfMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	public static int getWeekOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	public static int getWeekOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getNumWeeksForYear(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, 0, 1);

		return cal.getMaximum(Calendar.WEEK_OF_YEAR);
	}

	public static Date createDate() {
		return new Date();
	}

	public static Date createDate(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, date, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	public static Date createDate(int year, int month, int date, int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, date, hour, minute, second);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	public static Date createDate(String dateString, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
		try {
			return format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static int getWeekCount() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int lastDay = cal.getMaximum(Calendar.DAY_OF_MONTH);
		int count = (dayOfWeek + lastDay - 1) / 7;
		if ((dayOfWeek + lastDay - 1) % 7 > 0)
			count++;

		return count;
	}

	public static int getWeekCount(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int count = (dayOfWeek + lastDay - 1) / 7;
		if ((dayOfWeek + lastDay - 1) % 7 > 0)
			count++;

		return count;
	}

	public static int getLastDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static int getLastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static String getCurrentDateTime() {
		Calendar cal = Calendar.getInstance();
		return getDateFormatString(cal.getTime(), dateTimeFormat);
	}

	public static String getCurrenntDate() {
		Calendar cal = Calendar.getInstance();
		return getDateFormatString(cal.getTime(), dateFormat);
	}

	public static int getCurrenntDateNumber() {
		Calendar cal = Calendar.getInstance();
		String date = getDateFormatString(cal.getTime(), "yyyyMMdd");
		return Integer.parseInt(date);
	}

	public static String getCurrentDate(String dateTimeFormat) {
		Calendar cal = Calendar.getInstance();
		return getDateFormatString(cal.getTime(), dateTimeFormat);
	}

	public static String getDateFormatString(Date date, String dateTimeFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
		return sdf.format(date);
	}

	public static Date trimTime(Date date) {
		String dateString = getDateFormatString(date, dateFormat);
		return createDate(dateString, dateFormat);
	}

	public static Date getStringToDate(String format, String strDate) {
		try {
			Locale locale = Locale.forLanguageTag("ko-KR");
			DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
			df = new SimpleDateFormat(format, locale);
			Date date = (Date) df.parse(strDate);

			return date;
		} catch (ParseException e) {
			System.out.println("getStringToDate ParseException Occured!!");
			return null;
		}
	}

	public static Date getWeekStartDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		int dateOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int dateStart = 1 - dateOfWeek;

		cal.add(Calendar.DATE, dateStart);

		return cal.getTime();
	}

	public static Date getWeekEndDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		int dateOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int dateEnd = 7 - dateOfWeek;

		cal.add(Calendar.DATE, dateEnd);

		return cal.getTime();
	}

	public static String getDateFormatStringForSchedule(Date date) {
		return getDateFormatString(date, "yyyy년 M월 d일") + " (" + DAY_OF_WEEK[getDayOfWeek(date) - 1] + ")";
	}

	public static String getDateFormatStringForSchedule2(Date date) {
		return getDateFormatString(date, "yyyy년 M월 d일 h시 m분") + " (" + DAY_OF_WEEK[getDayOfWeek(date) - 1] + ")";
	}

	public static String getDateFormatStringForSchedule3(Date date) {
		return getDateFormatString(date, "yyyy년 M월 d일");
	}

	public static String getDateFormatToYYYYMM(Date date) {
		return getDateFormatString(date, "yyyyMM");
	}

	public static String getDateFormatToYYYYMMDD(Date date) {
		return getDateFormatString(date, "yyyyMMdd");
	}

	public static String getDateFormatToYYYYMMDDHHMMSS(Date date) {
		return getDateFormatString(date, "yyyyMMddHHmmss");
	}

	public static String getDateFormatToYYYYMMDDHHMM(Date date) {
		return getDateFormatString(date, "yyyyMMddHHmm");
	}

	public static String getDateStringFormat(Date date, String format) {
		return getDateFormatString(date, format);
	}

	public static String getDateFormatStringForGetTime(Date date) {
		return getDateFormatString(date, "aa h시 m분");
	}

	public static boolean isSameDay(Date date1, Date date2) {
		boolean result = false;

		String strDate1 = getDateFormatString(date1, "yyyyMMdd");
		String strDate2 = getDateFormatString(date2, "yyyyMMdd");

		if (strDate1.equalsIgnoreCase(strDate2))
			result = true;

		return result;
	}

	// 20140828
	public static String getDayString(Date date) {
		return DAY_OF_WEEK[getDayOfWeek(date) - 1];
	}

	public static String getCountdownFromCurrentTime(Date startDate) {
		Calendar cal = Calendar.getInstance();

		int sec = (int) ((cal.getTimeInMillis() - startDate.getTime()) / 1000);
		int day = (sec / 60 / 60 / 24);
		sec = (sec - (day * 60 * 60 * 24));
		int hour = sec / 60 / 60;
		sec = (sec - (hour * 60 * 60));
		int min = (sec / 60);
		sec = (sec - (min * 60));

		return String.format("%02d:%02d:%02d", hour * -1, min * -1, sec * -1);
	}

	public static String getAgeFromBirthDay(String yyyyMMdd) {

		String resAge = "";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date birthDay = sdf.parse(yyyyMMdd);

			Calendar birth = new GregorianCalendar();
			Calendar today = new GregorianCalendar();

			birth.setTime(birthDay);
			today.setTime(new Date());

			int factor = 0;
			if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
				factor = -1;
			}
			resAge = Integer.toString(today.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + factor);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resAge;
	}
}
