package edu.sjsu.cmpe273.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {
	String url;
	String driver;
	String userName;
	String password;
	Connection conn;
	Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

	public DatabaseConnection() {
		url = null;
		userName = null;
		password = null;
		conn = null;
	}

	public Connection getDatabaseConnetion() throws SQLException, IOException {
		
		Properties properties = new Properties();
		FileInputStream fin = new FileInputStream("src/main/resources/db.properties"); 
		properties.load(fin);
		fin.close();
		logger.info("Trying to connection to MySql Database");
		url = properties.getProperty("mysql.url");
		driver = properties.getProperty("mysql.driver");
		userName = properties.getProperty("mysql.user");
		password = properties.getProperty("mysql.password");
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager
					.getConnection(url, userName, password);
			logger.info("Database Connection URL: {}", properties.getProperty("mysql.url"));
		}
		catch (Exception e) {
			logger.debug("Connecting to MySql DB failed: {} ", e);
		}
		return conn;
	}
}
