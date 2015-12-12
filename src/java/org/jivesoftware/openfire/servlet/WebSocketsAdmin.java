package org.jivesoftware.openfire.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jivesoftware.util.JiveGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketsAdmin extends HttpServlet {
	private static Logger Log = LoggerFactory.getLogger("WebSocketsAdmin");
	private String webAppName = "ws";
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
	}
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
		this.webAppName = request.getParameter("webAppName");
		if(validateWebAppName(this.webAppName)){
			JiveGlobals.setProperty("websockets.webapp.name", this.webAppName);
			Log.info("WebSockets Properties updated");
			out.print("{\"result\":true,\"message\":\"\"}");
		}else{
			out.print("{\"result\":false,\"message\":\"您要修改的webAppName有问题\"}");
		}
		out.flush();
		out.close();
	}

	private boolean validateWebAppName(String webAppName){
		if(webAppName == null)
			return false;
		if(webAppName.equals(""))
			return false;
		if(webAppName.length()<1)
			return false;
		return true;
	}
}
