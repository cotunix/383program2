package server;

import org.apache.xmlrpc.webserver.*;
import org.apache.xmlrpc.server.*;

public class Server {

	int port = -1;

	public static void main (String [] args) {
		int port = -1;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception err) {
			System.err.println("You must specify a port for the server");
			return;
		}

		new Server(port).Main();
	}

	public Server(int p) {
		port = p;
	}

	public void Main() {

		try {

			PropertyHandlerMapping mapping = new PropertyHandlerMapping();
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			mapping.load(cl,"server.properties");

			System.out.println("Attempting to start XML-RPC Server...");
			WebServer webServer = new WebServer(port);

			XmlRpcServerConfigImpl config = new XmlRpcServerConfigImpl();
			XmlRpcServer server = webServer.getXmlRpcServer();
			server.setConfig(config);
			server.setHandlerMapping(mapping);

			webServer.start();
			System.out.println("Started successfully.");
			System.out.println("Accepting requests. (Halt program to stop.)");

		} catch (Exception exception) {
			System.err.println("Server: " + exception);
			exception.printStackTrace();
		}
	}	
}