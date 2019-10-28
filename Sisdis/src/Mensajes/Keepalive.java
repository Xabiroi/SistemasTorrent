package Mensajes;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class Keepalive implements Serializable {
	//Prueba
	private String ip;
	//
    
    
	private Date date;
	private Date date2;
	private int i;
	private boolean b;
	
	public Keepalive(int i, Date date, Date date2, boolean b) {
		this.i = i;
		this.date = date;
		this.date2 = date2;
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
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
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