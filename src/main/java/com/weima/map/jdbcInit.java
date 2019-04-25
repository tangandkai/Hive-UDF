package com.weima.map;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;




public class jdbcInit {

	private String user = "root";
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String password = "Tang199475&kai";
	private String url = "jdbc:mysql://10.40.150.32:3306/weima";
	private static jdbcInit jdbcinit = null;
	private Properties property = null;
	private Connection connection = null;
	private PreparedStatement pstmt = null;
	private jdbcInit() {
//		property = propertyInit.getInstance().getPropertirs();
	}
	
	/**
	 * *获取jdbc实例
	 * @return
	 */
	public static jdbcInit getInstance() {
		if(null==jdbcinit) {
			return new jdbcInit();
		}
		return jdbcinit;
	}
	
	/**
	 * *获取jdbc连接
	 * @return 
	 */
	public void getConnection() {
//		user = property.getProperty("user");
//		driver = property.getProperty("driver");
//		password = property.getProperty("password"); 
//		url = property.getProperty("url");
		System.out.println("user="+user+"\n"+"driver"+driver+"\n"+"password"+password+"\n"+"url="+url);
		try {
			Class.forName(driver);
			try {
				connection = DriverManager.getConnection(url, user, password);
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * *建表
	 * @param tableName
	 * @param sql
	 */
	public void createTable(String tableName,String sql) {
		if(null==tableName) {
			System.out.println("请给出表名！");
			return;
		}
		try {
			pstmt = connection.prepareStatement(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * *latitude,longitude,country,province,city,city_level,district,town,street,street_number
	 * @param tableName
	 * @param location
	 */
	public void insertIntoTable(String tableName,LocationModel location) {
		try{
			pstmt = connection.prepareStatement("insert into ? values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setString(0, tableName);
			pstmt.setString(1, location.getLatitude());
			pstmt.setString(2, location.getLongitude());
			pstmt.setString(3, location.getCountry());
			pstmt.setString(4, location.getProvince());
			pstmt.setString(5, location.getCity());
			pstmt.setString(6, location.getCity_level());
			pstmt.setString(7, location.getDistrict());
			pstmt.setString(8, location.getTown());
			pstmt.setString(9, location.getAdcode());
			pstmt.setString(10,location.getStreet());
			pstmt.setString(11,location.getStreet_number());
			pstmt.setString(12,location.getDirection());
			pstmt.setString(13,location.getDistance());
			pstmt.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * *关闭mysql
	 */
	public void stop() {
		if(pstmt!=null) {try {
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		if(connection!=null) {try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
//	public static void main(String[] args) {
//		jdbcInit init = jdbcInit.getInstance();
//		init.getConnection();
//		System.out.println(init.connection);
//	}
}
