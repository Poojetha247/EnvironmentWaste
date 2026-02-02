package com.waste.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	public static Connection getDBConnection() {
	    Connection connection = null;
	    try {
	        Class.forName("oracle.jdbc.driver.OracleDriver");
	        String url = "jdbc:oracle:thin:@localhost:1521:XE";
	        String username = "poojetha";
	        String password = "poojetha";

	        connection = DriverManager.getConnection(url, username, password);
	        connection.setAutoCommit(false); // 🔴 THIS LINE FIXES ORA-17273

	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    }
	    return connection;
	}

}