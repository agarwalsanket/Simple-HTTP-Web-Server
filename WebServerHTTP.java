/* 
 * WebServerHTTP.java 
 * 
 * Version: 
 *     $Id$
 * 
 * Revisions: Initial Version
 *    
 */
/**
 *WebServerHTTP.java is an implementation for building a simple HTTP web server
 *
 * @author Sanket Agarwal
 *
 */
package project01;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;


public class WebServerHTTP {
public static void main(String[] args) throws IOException{
	InetAddress locIP = InetAddress.getByName("127.0.0.1"); //local host
	HttpServer server = null;
	try{
	server = HttpServer.create(new InetSocketAddress(locIP, 8080), 0);} // Creating a HttpServer instance which is bind to the localhost
	catch(BindException e){
		System.out.println(e.getMessage());
		System.out.println("The port is busy");
		System.exit(0);
	}

	server.createContext("/CN", new ReqHandlerCN()); // Mapping the URI path with a handler object
	server.createContext("/text", new ReqHandlerPdf()); // Mapping the URI path with a handler object
	server.start();  // Staring separate thread for each HTTP request
	System.out.println("Server is listening..");
}
	static class ReqHandlerCN implements HttpHandler{

		@Override
		public void handle(HttpExchange msg) throws IOException {
			
			Headers h = msg.getResponseHeaders();  // Getting the response header object which is a mutable map
			h.add("Content-Type", "text/html");   // Adding the header types
			h.add("Connection", "close"); 
			File f = new File("./ComputerNetworks.html");  //Creating File object
			
			 byte [] msgByteArr  = new byte [(int)f.length()];  // Byte Array for the input stream
			 FileInputStream fis = new FileInputStream(f); 
			 BufferedInputStream bis = new BufferedInputStream(fis);
			 bis.read(msgByteArr, 0, msgByteArr.length);
			 
			 msg.sendResponseHeaders(200, f.length());  // Sending the response header
			 OutputStream os = msg.getResponseBody();
			 os.write(msgByteArr,0,msgByteArr.length);  // Writing the file
			 bis.close();
			 os.close();
			 
		}
		
	}
	
	static class ReqHandlerPdf implements HttpHandler{

		@Override
		public void handle(HttpExchange msg) throws IOException {
		
			Headers h = msg.getResponseHeaders();
			h.add("Content-Type", "application/pdf");
			h.add("Connection", "close");
			
			File f = new File("./text.pdf");
			
			 byte [] bytearray  = new byte [(int)f.length()];
			 FileInputStream fis = new FileInputStream(f);
			 BufferedInputStream bis = new BufferedInputStream(fis);
			 bis.read(bytearray, 0, bytearray.length);
			 
			 msg.sendResponseHeaders(200, f.length());
			 OutputStream os = msg.getResponseBody();
			 os.write(bytearray,0,bytearray.length);
			 bis.close();
			 os.close();
			
		} 
	}
}

