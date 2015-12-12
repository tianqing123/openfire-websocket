package org.jivesoftware.openfire.plugin;

import java.io.File;

import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.WSConnection;
import org.jivesoftware.openfire.XMPPWebSocket;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.http.HttpBindManager;
import org.jivesoftware.openfire.servlet.XMPPServlet;
import org.jivesoftware.openfire.session.LocalClientSession;
import org.jivesoftware.util.JiveGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketsPlugin implements Plugin {
	private static Logger Log = LoggerFactory.getLogger("WebSocketsPlugin");
	private static final String NAME = "ws";
	//private static final String DESCRIPTION = "WebSockets Plugin for Openfire";
	/*private PluginManager manager;
	private File pluginDirectory;*/
	//private ConcurrentHashMap<String,WSConnection> connections = new ConcurrentHashMap();

	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		Log.info("[ws] initialize ws plugin resources");

		String appName = JiveGlobals.getProperty("websockets.webapp.name", "ws");
		try {
			ContextHandlerCollection contexts = HttpBindManager.getInstance().getContexts();
			try {
				Log.info("[ws] initialize ws initialize Websockets " + appName);
				ServletContextHandler context = new ServletContextHandler(contexts, "/" + appName);
				context.addServlet(new ServletHolder(new XMPPServlet()), "/server");
				new WebAppContext(contexts, pluginDirectory.getPath(), "/websockets");
			} catch (Exception e) {
				Log.error("An error has occurred", e);
			}
		} catch (Exception e) {
			Log.error("Error initializing WebSockets Plugin", e);
		}
	}

	@Override
	public void destroyPlugin() {
		Log.info("[ws] destroy ws plugin resources");
		try {
			for (WSConnection wsConnection : XMPPWebSocket.getConnections().values())
				try {
					LocalClientSession session = wsConnection.getSession();
					session.close();
					SessionManager.getInstance().removeSession(session);
					session = null;
				} catch (Exception e) {
				}
			XMPPWebSocket.getConnections().clear();
		} catch (Exception e) {
			Log.error("[ws] destroyPlugin exception " + e);
		}
	}

	public String getName() {
		return "ws";
	}

	public String getDescription() {
		return "WebSockets Plugin for Openfire";
	}
}
