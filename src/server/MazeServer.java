package server;
import java.util.*;
import java.io.*;
import java.sql.*;

public class MazeServer {
	
	public static boolean mazeExists;
	public static char[][] maze;
	public static final int MAZESIZE = 9;
	public static Map<String, Token> users = new HashMap<String, Token>();
	
	public String connect(String username, String password) {
		if (!mazeExists)
			buildMaze();
		
		if (users.containsKey(username)){
			if (users.get(username).password == password){
				return users.get(username).userToken;
			}
		}
		return null;
		
		
	}
	public void buildMaze() {
		
		maze = new char[MAZESIZE][MAZESIZE];
		try{
			BufferedReader in = new BufferedReader(new FileReader("maze.in"));
			int row = 0, column = 0;
			while (in.ready()){
				String line = in.readLine();
				for (char c : line.toCharArray()){
					maze[row][column] = c;
					column++;
				}
				row++;
				column = 0;
			}
				
		in.close();	
		} catch (FileNotFoundException e) {
			System.err.println("Maze file could not be found. Make sure maze file is in the proper directory.");
		}
		 catch (IOException e){
			 System.err.println("Error reading from file.");
		 }
		
	}
	
}
