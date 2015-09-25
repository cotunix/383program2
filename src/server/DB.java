package server;

import java.sql.*;
import java.io.*;

public class DB {
	private String user = "user";
	private String pwd = "383";
	private Statement stmnt;
	private String dbURL = "jdbc:mysql://localhost/mazedata";
	private Connection conn = null;

	public DB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, user, pwd);
			stmnt = conn.createStatement();
		} catch (ClassNotFoundException e) {
			System.err.println("Could not find proper class: " + e);
		} catch (SQLException e) {
			System.err.println("Could not connect to database: " + e);
		}
	}

	public void addUser(String user, int x, int y) throws SQLException {
		String sql = "INSERT INTO maze VALUES(" + "\"" + user + "\", " + x
				+ ", " + y + ", " + System.currentTimeMillis() / 1000L + ", "
				+ 0 + ", " + "'ACTIVE')";
		stmnt.executeUpdate(sql);
	}
}
