package client;

import java.net.*;
import java.util.*;
import java.io.*;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class MazeClient {
	XmlRpcClient xmlRPCclient = null;
	int port = -1;
	String id = "";
	String username;
	String password;
	
	public MazeClient(String host,int p) throws IOException {
		port = p;
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL("http://" + host+":"+port));
		xmlRPCclient = new XmlRpcClient();
		xmlRPCclient.setConfig(config);
	}

}
