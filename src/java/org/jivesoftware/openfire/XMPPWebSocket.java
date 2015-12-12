package org.jivesoftware.openfire;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.jivesoftware.openfire.auth.AuthFactory;
import org.jivesoftware.openfire.auth.AuthToken;
import org.jivesoftware.openfire.multiplex.UnknownStanzaException;
import org.jivesoftware.openfire.session.LocalClientSession;
import org.jivesoftware.util.JiveGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;

public class XMPPWebSocket implements WebSocketListener{ 
	private static Logger Log = LoggerFactory.getLogger(XMPPWebSocket.class);

	private Session session;
	private String deigst;
	private static ConcurrentHashMap<String, WSConnection> connections = new ConcurrentHashMap<String, WSConnection>(); 
   
    public static ConcurrentHashMap<String, WSConnection> getConnections() {
		return connections;
	}
    public void deliver(String packet) {
    	try {
			this.session.getRemote().sendString(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void disconnect() {
    	this.session.close();
    }

	@Override
	public void onWebSocketBinary(byte[] abyte0, int i, int j) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onWebSocketClose(int i, String s) {
		// TODO Auto-generated method stub
		WSConnection wsConnection = connections.get(deigst);
		wsConnection.close();
	}

	@Override
	public void onWebSocketConnect(Session session) {
		// TODO Auto-generated method stub
		session.setIdleTimeout(-1L);
		UpgradeRequest req = session.getUpgradeRequest();
	    Map<String, List<String>> parameters = req.getParameterMap();
	   String username = parameters.get("username").get(0);
	   String password = parameters.get("password").get(0);
	   String resource = parameters.get("resource").get(0);
	   this.deigst =username;
	   this.session = session;
	   WSConnection wsConnection = new WSConnection(session.getRemoteAddress().toString(), "5222");
	   wsConnection.setSocket(this);
	   LocalClientSession lsession = (LocalClientSession)SessionManager.getInstance().getSession(new JID(JID.escapeNode(username) + "@" + JiveGlobals.getProperty("xmpp.domain") + "/" + resource));
	   AuthToken authToken = null;
        if (lsession != null) {
        	lsession.close();
        	lsession = SessionManager.getInstance().createClientSession(wsConnection, new BasicStreamID("url" + System.currentTimeMillis()));
        	wsConnection.setRouter(new SessionPacketRouter(lsession));
	        authToken = new AuthToken(username);
        } else {
        	lsession = SessionManager.getInstance().createClientSession(wsConnection, new BasicStreamID("url" + System.currentTimeMillis()));
	
	          wsConnection.setRouter(new SessionPacketRouter(lsession));
	          if ((username.equals("null")) && (password.equals("null")))
	        	  	authToken = new AuthToken(resource, Boolean.valueOf(true));
	          else {
		            try {
			              String userName = JID.unescapeNode(username);
			              authToken = AuthFactory.authenticate(userName, password);
		            } catch (Exception e) {
		            	this.session.close();
			              Log.error("An error occurred while attempting to create a web socket (USERNAME: " + username + " RESOURCE: " + resource + " ) : ", e);
		            }
	          }
	        }
        lsession.setAuthToken(authToken, resource);
        try {
			wsConnection.getRouter().route(DocumentHelper.parseText("<presence/>").getRootElement());
			connections.put(username, wsConnection);
		} catch (UnsupportedEncodingException | UnknownStanzaException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onWebSocketError(Throwable throwable) {
		// TODO Auto-generated method stub
		throwable.printStackTrace();
		this.session.close();
	}

	@Override
	public void onWebSocketText(String data) {
		// TODO Auto-generated method stub
		//发消息给openfire服务器，暂时关闭，不支持
		/* if (!"".equals(data.trim()))
		        try {
		         // this.wsConnection.getRouter().route(DocumentHelper.parseText(data).getRootElement());
		        } catch (Exception e) {
		          //XMPPServlet.Log.error("An error occurred while attempting to route the packet : ", e);
		        }*/
	}
  }
