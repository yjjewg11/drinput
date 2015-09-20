package com.px.news.doorrecord.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import com.company.news.rest.util.TimeUtils;

public class ReadWriteUtil {
	private final static String filename = "timestamp";
	private final static String filename_cardstr = "cardFilter";
	/**
	 * 
	 * @param date
	 * @throws IOException
	 */
	public static void writeTimestamp(Date date) throws IOException {
		// 输出流
		FileWriter fw = new FileWriter(filename);

		fw.write(TimeUtils.timestamp2String(TimeUtils.DEFAULTFORMAT,
				new Timestamp(date.getTime())));

		fw.close();

	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Date readTimestamp() throws IOException {
		// 输出流
		FileReader fr = new FileReader(filename);

		int ch = 0;
		String s = "";
		while ((ch = fr.read()) != -1) {
			s += (char)ch;
		}

		Date time = TimeUtils.string2Timestamp(TimeUtils.DEFAULTFORMAT, s);
		if(time==null){
			System.out.println("读取格式联正确=" +s);
			s="2000-01-01 00:00:00";
			System.out.println("设置为=" +s);
			time= TimeUtils.string2Timestamp(TimeUtils.DEFAULTFORMAT,s );
		}
		fr.close();
		return time;

	}
	
	/**
	 * 
	 * @param date
	 * @throws IOException
	 */
	public static void writeCardStr(String s) throws IOException {
		// 输出流
		FileWriter fw = new FileWriter(filename_cardstr);

		fw.write(s);

		fw.close();

	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String readCardStr() throws IOException {
		// 输出流
		FileReader fr = new FileReader(filename_cardstr);

		int ch = 0;
		String s = "";
		while ((ch = fr.read()) != -1) {
			s += (char)ch;
		}

		fr.close();
		return s;

	}

}
