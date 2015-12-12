package org.jivesoftware.openfire;

import java.security.cert.Certificate;

import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.net.VirtualConnection;
import org.jivesoftware.openfire.XMPPWebSocket;
import org.jivesoftware.openfire.session.LocalClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

public class WSConnection extends VirtualConnection {
	private static Logger Log = LoggerFactory.getLogger("WSConnection");
	private SessionPacketRouter router;
	private String remoteAddr;
	private String hostName;
	private LocalClientSession session;
	private XMPPWebSocket socket;
	private boolean isSecure = false;

	public WSConnection(String remoteAddr, String hostName) {
		this.remoteAddr = remoteAddr;
		this.hostName = hostName;
	}

	public void setSocket(XMPPWebSocket socket) {
		this.socket = socket;
	}
	public XMPPWebSocket getSocket(){
		return this.socket;
	}

	@Override
	public boolean isSecure() {
		return this.isSecure;
	}

	public void setSecure(boolean isSecure) {
		this.isSecure = isSecure;
	}

	public SessionPacketRouter getRouter() {
		return this.router;
	}

	public void setRouter(SessionPacketRouter router) {
		this.router = router;
	}

	@Override
	public void closeVirtualConnection() {
		Log.debug("WSConnection - close ");
		this.socket.disconnect();
	}

	@Override
	public byte[] getAddress() {
		return this.remoteAddr.getBytes();
	}

	@Override
	public String getHostAddress() {
		return this.remoteAddr;
	}

	@Override
	public String getHostName() {
		return this.hostName != null ? this.hostName : "0.0.0.0";
	}

	@Override
	public void systemShutdown() {
	}

	@Override
	public void deliver(Packet packet) throws UnauthorizedException {
		if(!(packet instanceof Message))
			return;
		deliverRawText(packet.toXML());
	}

	@Override
	public void deliverRawText(String text) {
		this.socket.deliver(text);
	}

	@Override
	public Certificate[] getPeerCertificates() {
		return null;
	}

	public LocalClientSession getSession() {
		return session;
	}

	public void setSession(LocalClientSession session) {
		this.session = session;
	}
}