import java.util.Observable;

public class Peer extends Observable{
	
	private String IP;
	private String Puerto;


	public Peer() {
		this.setIP("");
		this.setPuerto("");
		
	}
	public Peer(String IP,String Puerto) {
		this.setIP(IP);
		this.setPuerto(Puerto);
		
	}

	public String getPuerto() {
		return Puerto;
	}


	public void setPuerto(String puerto) {
		Puerto = puerto;
	}


	public String getIP() {
		return IP;
	}


	public void setIP(String iP) {
		IP = iP;
	}

}
