package com.tgw360.uip.DataImporter;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class JdbcUtils {
	private static Connection conn;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://172.18.44.5:3306/jydb", "root", "qfx@123456");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Connection getConnection() {
		return conn;
	}
}
