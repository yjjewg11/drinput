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
	private static Date lastUserModifyDate=null;
	
	/**
	 * 读取所有用户
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
public static List<DoorUserJsonform> readCardUserListByLastEditRq() throws Exception {
		
		SQLServerDataSource ds = new SQLServerDataSource();
		ds.setUser(ProjectProperties.getProperty("user", ""));
		ds.setPassword(ProjectProperties.getProperty("password", ""));
		ds.setServerName(ProjectProperties.getProperty("serverName", ""));
		ds.setPortNumber(ProjectProperties.getPropertyAsInt("portNumber", 1433)); 
		ds.setDatabaseName(ProjectProperties.getProperty("databaseName", ""));
		Connection con = ds.getConnection();

		List<DoorUserJsonform> list=new ArrayList<DoorUserJsonform>();
		
		try {
			ResultSet rs=null;
			PreparedStatement pstmt =null;
			if(lastUserModifyDate==null){
				String SQL = "SELECT CardID,UserID,UserName,IdNo,LastEditRq FROM CardUser where LastEditRq>? order by LastEditRq";
				 pstmt = con.prepareStatement(SQL);
				pstmt.setTimestamp(1, new Timestamp(lastUserModifyDate.getTime()));
				 rs = pstmt.executeQuery();
			}else{
				String SQL = "SELECT CardID,UserID,UserName,IdNo FROM CardUser order by LastEditRq";
			       pstmt = con.prepareStatement(SQL);
			       rs = pstmt.executeQuery();

			}
		  	while (rs.next()) {
		  		DoorUserJsonform user=new DoorUserJsonform();
		  		user.setCardid(rs.getString(1));
		  		user.setUserid(rs.getString(2));
				user.setUserName(rs.getString(3));
				user.setIdNo(rs.getString(4));
				list.add(user);
				
				
				lastUserModifyDate=rs.getDate(5);
			}
			if(rs!=null)rs.close();
			if(rs!=null)pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	/**
	 * 读取所有用户
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static List<DoorUserJsonform> readAllCardUserList() throws Exception {
		
		SQLServerDataSource ds = new SQLServerDataSource();
		ds.setUser(ProjectProperties.getProperty("user", ""));
		ds.setPassword(ProjectProperties.getProperty("password", ""));
		ds.setServerName(ProjectProperties.getProperty("serverName", ""));
		ds.setPortNumber(ProjectProperties.getPropertyAsInt("portNumber", 1433)); 
		ds.setDatabaseName(ProjectProperties.getProperty("databaseName", ""));
		Connection con = ds.getConnection();

		List<DoorUserJsonform> list=new ArrayList<DoorUserJsonform>();
		
		try {
			String SQL = "SELECT CardID,UserID,UserName,IdNo FROM CardUser order by LastEditRq";
		      PreparedStatement pstmt = con.prepareStatement(SQL);
		      ResultSet rs = pstmt.executeQuery();

		  	while (rs.next()) {
		  		DoorUserJsonform user=new DoorUserJsonform();
		  		user.setCardid(rs.getString(1));
		  		user.setUserid(rs.getString(2));
				user.setUserName(rs.getString(3));
				user.setIdNo(rs.getString(4));
				list.add(user);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

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
			String SQL = "SELECT id,cardid,dt,EquNo,DoorID,ErrCode,OperDt,OperName FROM door_record where dt>? and dt<=? ORDER BY dt";
		      PreparedStatement pstmt = con.prepareStatement(SQL);
		      pstmt.setTimestamp(1, new Timestamp(startDate.getTime()));
		      pstmt.setTimestamp(2, new Timestamp(endDate.getTime()));
		      ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				DoorRecord d=new DoorRecord();
				d.setCardid(rs.getString(2));
				d.setDt(rs.getTimestamp(3));
				String rs4=rs.getString(4);
				String rs5=rs.getString(5);
				d.setEquno(rs4);
				String showkey="door_show_"+rs4+"_"+rs5;
				System.out.print(showkey);
				d.setDoorid(ProjectProperties.getProperty(showkey,rs5));
				//d.setDoorid(rs.getString(5));
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
			String SQL = "SELECT CardID,UserID,UserName,IdNo FROM CardUser where CardID=?";
		      PreparedStatement pstmt = con.prepareStatement(SQL);
		      pstmt.setString(1,cardid);

		      ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				user=new DoorUserJsonform();
				user.setCardid(rs.getString(1));
		  		user.setUserid(rs.getString(2));
				user.setUserName(rs.getString(3));
				user.setIdNo(rs.getString(4));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}

}
