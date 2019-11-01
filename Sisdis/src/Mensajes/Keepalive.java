package Mensajes;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class Keepalive implements Serializable {
	//Prueba
	private String ip;
	//
    
    
	private long date;
	private int i;
	private boolean b;
	
	public Keepalive(int i, long date, boolean b) {
		this.i = i;
		this.date = date;
		this.b = b;
		try {
			this.setIp((InetAddress.getLocalHost()).toString());
			this.ip=this.ip.substring(this.ip.lastIndexOf("/") + 1);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public boolean isB() {
		return b;
	}

	public void setB(boolean b) {
		this.b = b;
	}

	
	//PRUEBA
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	//


}