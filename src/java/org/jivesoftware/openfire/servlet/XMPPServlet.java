package org.jivesoftware.openfire.servlet;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.jivesoftware.openfire.XMPPWebSocket;
public final class XMPPServlet extends WebSocketServlet
{
	private static final long serialVersionUID = 7293374374875384946L;
	@Override
	public void configure(WebSocketServletFactory websocketservletfactory) {
		websocketservletfactory.register(XMPPWebSocket.class);
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		// TODO Auto-generated method stub
		super.service(request, response);
	}
	
	 private static String getMD5(String input) {
		    try {
			      MessageDigest md = MessageDigest.getInstance("MD5");
			      byte[] messageDigest = md.digest(input.getBytes());
			      BigInteger number = new BigInteger(1, messageDigest);
			      String hashtext = number.toString(16);
			
			      while (hashtext.length() < 32) {
			        hashtext = "0" + hashtext;
			      }
			      return hashtext; 
		    } 
		    catch (NoSuchAlgorithmException e) 
		    {
		        //Log.error("用户名、密码解析出错，输入是:"+input);
		        return null;
		    }
	  }
	 
}