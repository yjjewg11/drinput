package com.px.news.doorrecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.company.news.ProjectProperties;
import com.company.news.entity.DoorRecord;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

/**
 * ��ݶ�ȡ
 * 
 * @author Administrator
 * 
 */
public class DataRead {

	public static List<DoorRecord> readList(Date startDate,Date endDate) throws Exception {
		
		SQLServerDataSource ds = new SQLServerDataSource();
		ds.setUser(ProjectProperties.getProperty("user", ""));
		ds.setPassword(ProjectProperties.getProperty("password", ""));
		ds.setServerName(ProjectProperties.getProperty("serverName", ""));
		ds.setPortNumber(ProjectProperties.getPropertyAsInt("portNumber", 1433)); 
		ds.setDatabaseName(ProjectProperties.getProperty("databaseName", ""));
		Connection con = ds.getConnection();

		List<DoorRecord> list=new ArrayList<DoorRecord>();
		
		try {
			String SQL = "SELECT * FROM door_record where dt>? and dt<=? ORDER BY dt";
		      PreparedStatement pstmt = con.prepareStatement(SQL);
		      pstmt.setTimestamp(1, new Timestamp(startDate.getTime()));
		      pstmt.setTimestamp(2, new Timestamp(endDate.getTime()));
		      ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				DoorRecord d=new DoorRecord();
				d.setCardid(rs.getString(2));
				d.setDt(rs.getTimestamp(3));
				d.setEquno(rs.getString(4));
				d.setDoorid(rs.getString(5));
				d.setErrcode(rs.getString(6));
				list.add(d);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
