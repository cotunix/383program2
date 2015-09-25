package server;

import java.sql.*;

public class DB {
	private String url = "jdbc:mysql://localhost/mazedata";
	private Connection conn = null;
	private String user = "user";
	private String pwd = "383";
	private Statement state;
	

	public DB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, pwd);
			state = conn.createStatement();
		} catch (ClassNotFoundException e) {
			System.err.println("Could not find proper class: " + e);
		} catch (SQLException e) {
			System.err.println("Could not connect to database: " + e);
		}
	}

	public void addUser(String user, int x, int y) throws SQLException {
		String query = "INSERT INTO maze VALUES(" + "\"" + user + "\", " + x
				+ ", " + y + ", " + System.currentTimeMillis() / 1000L + ", "
				+ 0 + ", " + "'ACTIVE')";
		state.executeUpdate(query);
	}
	
	public int[] getXY(String user) throws SQLException {
		
		String query = "SELECT x, y, state FROM maze WHERE user = " + "\"" + user + "\"";
		ResultSet rs = state.executeQuery(query);
		rs.next();
		if (!rs.getString("state").equals("ACTIVE")) {
			throw new SQLException("User state not ACTIVE");
		}
		int[] xy = {rs.getInt("x"), rs.getInt("y")};
		return xy;
	}
	
	public void move(int x, int y, String user) throws SQLException {
		String query = "UPDATE maze SET X = " + x + ", Y = " + y + ", moves = moves + 1 WHERE user = \"" + user + "\"";
		state.executeUpdate(query);
		
	}
	
	public void deleteUser(String user) throws SQLException {
		String sql = "DELETE FROM maze WHERE name = " + "\"" + user + "\"";
		state.executeUpdate(sql);
	}
	
	public void finished(String user) throws SQLException {
		String sql = "UPDATE maze SET state = 'FINISHED' WHERE user = " + "\"" + user + "\"";
		state.executeUpdate(sql);
	}
	
	public ResultSet getMaze()throws SQLException {
		return state.executeQuery("SELECT * FROM maze");
	}
}
