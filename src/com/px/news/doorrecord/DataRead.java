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
	public static Date lastDoor_record_date=null;
	static public Long lastDoor_record_id=Long.valueOf(0);
	static public Integer pagesize=ProjectProperties.getPropertyAsInt("pagesize", 30);
	/**
	 * 读取新变化的用户读取数据.
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
public static List<DoorUserJsonform> readCardUserListByChange() throws Exception {
		
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
			//查询我们的
			String SQL = "SELECT CardID,UserID,UserName,IdNo,LastEditRq  from carduser where cardid+userid not in(select cardid+userid from px_carduser)";
		       pstmt = con.prepareStatement(SQL);
		       rs = pstmt.executeQuery();
		       
		  	while (rs.next()) {
		  		DoorUserJsonform user=new DoorUserJsonform();
		  		user.setCardid(rs.getString(1));
		  		user.setUserid(rs.getString(2));
				user.setUserName(rs.getString(3));
				user.setIdNo(rs.getString(4));
				list.add(user);
			}
			if(rs!=null)rs.close();
			if(rs!=null)pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

/**
 * 读取新变化的用户读取数据.
 * @param startDate
 * @param endDate
 * @return
 * @throws Exception
 */
public static void saveCardUserListByChange(List<Object[]> list) throws Exception {
	if(list==null||list.size()==0)return;
	SQLServerDataSource ds = new SQLServerDataSource();
	ds.setUser(ProjectProperties.getProperty("user", ""));
	ds.setPassword(ProjectProperties.getProperty("password", ""));
	ds.setServerName(ProjectProperties.getProperty("serverName", ""));
	ds.setPortNumber(ProjectProperties.getPropertyAsInt("portNumber", 1433)); 
	ds.setDatabaseName(ProjectProperties.getProperty("databaseName", ""));
	Connection con = ds.getConnection();
	con.setAutoCommit(true);
	
	try {
		ResultSet rs=null;
		PreparedStatement pstmt =null;
		//查询我们的
		String SQL = "insert into px_carduser  (cardid,userid,flag,createdate) values(?,?,?,getdate())";
	       pstmt = con.prepareStatement(SQL);
	       
	       for(Object[] obj:list){
	    	   DoorUserJsonform user=(DoorUserJsonform)obj[0];
	    	   String msg=(String)obj[1];
	    	   pstmt.setString(1, user.getCardid());
	    	   pstmt.setString(2, user.getUserid());
	    	   pstmt.setString(3, msg);
	    	   pstmt.addBatch();
	       }
	       pstmt.executeBatch();
	       con.commit();
		if(rs!=null)rs.close();
		if(rs!=null)pstmt.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
	/**
	 * 读取所有用户
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
@Deprecated
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
			if(lastUserModifyDate!=null){
				String SQL = "SELECT CardID,UserID,UserName,IdNo,LastEditRq FROM CardUser where LastEditRq>? order by LastEditRq";
				 pstmt = con.prepareStatement(SQL);
				pstmt.setTimestamp(1, new Timestamp(lastUserModifyDate.getTime()));
				 rs = pstmt.executeQuery();
			}else{
				String SQL = "SELECT CardID,UserID,UserName,IdNo,LastEditRq FROM CardUser order by LastEditRq";
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
				
				
				lastUserModifyDate=rs.getTimestamp(5);
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
			//String SQL = "SELECT id,cardid,dt,EquNo,DoorID,ErrCode,OperDt,OperName FROM door_record where dt>? and dt<=? ORDER BY dt";
			String SQL = "SELECT top "+pagesize+" id,cardid,dt,EquNo,DoorID,ErrCode,OperDt,OperName FROM door_record where dt>? and id>?  ORDER BY dt";
			     PreparedStatement pstmt = con.prepareStatement(SQL);
		      pstmt.setTimestamp(1, new Timestamp(startDate.getTime()));
		      //防止重复数据提交
		      pstmt.setLong(2, lastDoor_record_id);
		     // pstmt.setTimestamp(2, new Timestamp(endDate.getTime()));
		      ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				DoorRecord d=new DoorRecord();
				d.setCardid(rs.getString(2));
				d.setDt(rs.getTimestamp(3));
				String rs4=rs.getString(4);
				String rs5=rs.getString(5);
				d.setEquno(rs4);
				String showkey="door_show_"+rs4+"_"+rs5;
				//System.out.print(showkey);
				d.setDoorid(ProjectProperties.getProperty(showkey,rs5));
				//d.setDoorid(rs.getString(5));
				d.setErrcode(rs.getString(6));
				//记录最后
				lastDoor_record_date=d.getDt();
				lastDoor_record_id=rs.getLong(1);
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
