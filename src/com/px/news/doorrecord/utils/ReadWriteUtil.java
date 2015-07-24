package com.px.news.doorrecord.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import com.company.news.rest.util.TimeUtils;

public class ReadWriteUtil {
	private final static String filename = "timestamp";

	/**
	 * 
	 * @param date
	 * @throws IOException
	 */
	public static void write(Date date) throws IOException {
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
	public static Date read() throws IOException {
		// 输出流
		FileReader fr = new FileReader(filename);

		int ch = 0;
		String s = "";
		while ((ch = fr.read()) != -1) {
			s += (char)ch;
		}

		Date time = TimeUtils.string2Timestamp(TimeUtils.DEFAULTFORMAT, s);

		fr.close();
		return time;

	}

}
