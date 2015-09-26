package server;

import java.sql.SQLException;
import java.util.*;
import java.io.*;

public class MazeServer {

	public static boolean mazeExists = false;
	public static char[][] maze;
	public static final int MAZESIZE = 9;
	public static int startX = 0, startY = 0;
	public static Map<String, User> users = new HashMap<String, User>();
	public static DB db = new DB();

	public String connect(String username, String password) {
		if (!mazeExists)
			buildMaze();

		// Thanks to StackOverflow for iterating over map
		for (Map.Entry<String, User> ent : users.entrySet()) {
			if (ent.getValue().username.equals(username)) {
				if (ent.getValue().password.equals(password)) {
					return ent.getKey();
				} else
					return "PASSWORD FAIL";
			}

		}
		try {
			db.addUser(username, startX, startY);
		} catch (SQLException e) {
			System.err.println("Error adding user to database.");
			return "DATABASE ERROR";
		}

		String t = generateToken();
		users.put(t, new User(username, password));
		return t;

	}

	public String close(String token, String password) {
		if (users.containsKey(token)) {
			User tempUser = users.get(token);
			System.out.println(tempUser.password);
			System.out.println(password);
			if (tempUser.password.equals(password)) {
				try {
					db.deleteUser(tempUser.username);
					users.remove(token);
					return "OK";
				} catch (SQLException e) {
					System.err.println("Error deleting user from database.");
					return "SQL FAIL";
				}

			} else
				return "Password Invalid";
		} else {
			return "Token Invalid";
		}

	}

	public String look(String token) {
		try {
			int[] xy = db.getXY(users.get(token).username);
			char[] temp = getSurrounding(xy[0], xy[1]);
			return new String(temp);
		} catch (SQLException e) {
			System.err.println("Error accessing DB in look()");
			return "DB FAIL";
		}

	}

	public String move(String token, String direction) {
		String user = users.get(token).username;
		int newX = 0;
		int newY = 0;
		int[] temp;
		try {
			temp = db.getXY(user);

		} catch (SQLException e) {
			return "SQL FAIL";
		}
		int x = temp[0];
		int y = temp[1];

		switch (direction) {
		case "N":
			newX = x;
			newY = y - 1;
			break;
		case "E":
			newX = x + 1;
			newY = y;
			break;
		case "S":
			newX = x;
			newY = y + 1;
			break;
		case "W":
			newX = x - 1;
			newY = y;
			break;
		}

		try {
			if (maze[newX][newY] == 'E') {
				db.finished(user);
				return "DONE";
			} else if (maze[newX][newY] == 'P') {
				db.finished(user);
				return "DIED";
			} else if (maze[newX][newY] != ' ')
				return "INVALID";
		} catch (ArrayIndexOutOfBoundsException e) {
			return "INVALID";
		} catch (SQLException e) {
			return "SQL FAIL";
		}
		try {
			db.move(newX, newY, user);
		} catch (SQLException e) {
			return "SQL FAIL";
		}
		return "OK";
	}

	private String generateToken() {
		Random rd = new Random();
		Integer userNum = rd.nextInt(Integer.MAX_VALUE);
		return userNum.toString();

	}

	private void buildMaze() {

		maze = new char[MAZESIZE][MAZESIZE];
		try {
			BufferedReader in = new BufferedReader(new FileReader("maze.in"));
			int row = 0, column = 0;
			while (in.ready()) {
				String line = in.readLine();
				for (char c : line.toCharArray()) {
					if (c == 'S') {
						startX = row;
						startY = column;
					}
					maze[row][column] = c;
					column++;
				}
				row++;
				column = 0;
			}

			in.close();
		} catch (FileNotFoundException e) {
			System.err
					.println("Maze file could not be found. Make sure maze file is in the proper directory.");
		} catch (IOException e) {
			System.err.println("Error reading from file.");
		}

	}

	private char[] getSurrounding(int x, int y) {
		char[] ret = new char[4];
		for (int i = 0; i < 4; i++) {
			try {
				if (i == 0)
					ret[i] = maze[x][y - 1];
				if (i == 1)
					ret[i] = maze[x + 1][y];
				if (i == 2)
					ret[i] = maze[x][y + 1];
				if (i == 3)
					ret[i] = maze[x - 1][y];
			} catch (ArrayIndexOutOfBoundsException e) {
				ret[i] = 'X';
			}
		}
		return ret;
	}

}
