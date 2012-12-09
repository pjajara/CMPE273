package edu.sjsu.cmpe273.client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import edu.sjsu.cmpe273.util.DatabaseConnection;

public class MainApp {
	
	public static Connection conn = null;

	public static void main(String[] args) throws SQLException, IOException {
		DatabaseConnection dbConn = new DatabaseConnection();
		conn = dbConn.getDatabaseConnetion();
		conn.close();
		System.out.println("Connection created and dropped successfully");
	}

}
