package client;

import java.net.*;
import java.sql.SQLException;
import java.util.*;
import java.io.*;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class MazeClient {
	XmlRpcClient xmlRPCclient = null;
	int port = -1;
	String token = "";
	String username;
	String password;
	Scanner in;

	public MazeClient(String host, int p) throws IOException {
		port = p;
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL("http://" + host + ":" + port));
		xmlRPCclient = new XmlRpcClient();
		xmlRPCclient.setConfig(config);
		in = new Scanner(System.in);
	}

	public void run() {
		try {
			connect();
			String l = look();
			String input = "";
			if (l.equals("DB FAIL")) {
				throw new XmlRpcException(
						"Error getting coordinates for user: In look()(User not active OR user not found");
			}
			System.out.println(l);

			while (true) {
				commands();
				input = in.nextLine();
				switch (input) {
				case "N":
				case "S":
				case "W":
				case "E":
					String move = move(input);
					if (move.equals("Done")){
						String ret = close(password);
						if(ret.equals("OK")){
							System.out.println("Congratulation, you win!");
							System.exit(0);
						} else {
							System.err.println(ret);
						}
					}
					
				}
			}

		} catch (Exception e) {

		}

	}

	public void connect() throws XmlRpcException {

		System.out.println("Input username");
		username = in.next();

		System.out.println("Input password");
		password = in.next();

		token = connectServer(username, password);
		if (token.equals("PASSWORD FAIL")) {
			in.close();
			throw new XmlRpcException("Invalid credentials");
		} else if (token.equals("DATABASE ERROR")) {
			in.close();
			throw new XmlRpcException("Database error");
		}
	}

	private void commands() {
		System.out.println("N|E|S|W to move.");
		System.out.println("G to get maze.");
		System.out.println("Q to quit.");
		System.out.println("C to quit and end session.\n");
	}

	public String move(String direction) throws XmlRpcException {
		Object[] params = new Object[] { token, direction };
		String ret = (String) xmlRPCclient.execute("mazeserver.move", params);
		return ret;
	}

	public String get() throws XmlRpcException {
		Object[] params = new Object[] {};
		String ret = (String) xmlRPCclient.execute("mazeserver.get", params);
		return ret;
	}

	public String look() throws XmlRpcException {
		Object[] params = new Object[] { token };
		String ret = (String) xmlRPCclient.execute("mazeserver.look", params);
		return ret;
	}

	public String connectServer(String user, String pass)
			throws XmlRpcException {
		Object[] params = new Object[] { user, pass };
		String ret = (String) xmlRPCclient
				.execute("mazeserver.connect", params);
		return ret;
	}

	public String close(String pass) throws XmlRpcException {
		Object[] params = new Object[] { token, pass };
		String ret = (String) xmlRPCclient.execute("mazeserver.close", params);
		return ret;
	}
}
