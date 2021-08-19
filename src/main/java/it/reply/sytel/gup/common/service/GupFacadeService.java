package it.reply.sytel.gup.common.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public abstract class GupFacadeService {

	private static final String date = "yyyy-MM-dd";
	private static final String time = "HH:mm:ss.SSS";
	
	public static String getUTCDateAsString() {
		SimpleDateFormat sdfDate = new SimpleDateFormat(date);
		sdfDate.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat sdfTime = new SimpleDateFormat(time);
		sdfTime.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date d = new Date();
		return sdfDate.format(d) + "T" + sdfTime.format(d) + "Z";
	}
	
	public String getGMTDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(new Date());
	}


	public String getCurrentLocalDateTimeStamp() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
	}

	public static String getGMTDateAsString() {
		SimpleDateFormat sdfDate = new SimpleDateFormat(date);
		sdfDate.setTimeZone(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat sdfTime = new SimpleDateFormat(time);
		sdfTime.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date d = new Date();
		return sdfDate.format(d) + "T" + sdfTime.format(d) + "Z";
	}
	
	
	public static String formatData(String data) throws ParseException {
		DateFormat formatter = new SimpleDateFormat(date);	
		Date date = formatter.parse(data);
		return formatter.format(date);
	}
	
	
}
