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
import com.company.news.jsonform.DoorUserJsonform;
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
	
	
	/**
	 * 根据卡ID获取卡用户信息
	 * @param cardid
	 * @return
	 * @throws Exception
	 */
	public static DoorUserJsonform getCard(String cardid) throws Exception {
		
		SQLServerDataSource ds = new SQLServerDataSource();
		ds.setUser(ProjectProperties.getProperty("user", ""));
		ds.setPassword(ProjectProperties.getProperty("password", ""));
		ds.setServerName(ProjectProperties.getProperty("serverName", ""));
		ds.setPortNumber(ProjectProperties.getPropertyAsInt("portNumber", 1433)); 
		ds.setDatabaseName(ProjectProperties.getProperty("databaseName", ""));
		Connection con = ds.getConnection();

		DoorUserJsonform user=null;
		
		try {
			String SQL = "SELECT UserName,IdNo FROM CardUser where CardID=?";
		      PreparedStatement pstmt = con.prepareStatement(SQL);
		      pstmt.setString(1,cardid);

		      ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				user=new DoorUserJsonform();
				user.setIdNo(rs.getString(2));
				user.setUserName(rs.getString(1));
				user.setCardid(cardid);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}

}
