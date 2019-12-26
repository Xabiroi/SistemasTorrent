package bitTorrent.tracker.protocol.udp;

import bitTorrent.util.ByteUtils;

/**
 * 
 * 	Size				Name
 * 	32-bit integer  	IP address
 * 	16-bit integer  	TCP port
 *  
 */

public class PeerInfo {
	private int ipAddress;
	private int port;
	
	public int getIpAddress() {
		return ipAddress;
	}
	
	public String getStringIpAddress() {
		return ByteUtils.intToIpAddress(this.ipAddress);
	}
	
	public void setIpAddress(int ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public int getPort() {		
		return port < 0 ? (Short.MAX_VALUE + 1)*2 + port : port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("ip: ");
		buffer.append(ByteUtils.intToIpAddress(this.ipAddress));
		buffer.append(" - port: ");
		buffer.append(port);
		
		return buffer.toString();
	}
}