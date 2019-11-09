package Objetos;
import java.util.Observable;

public class Peer extends Observable{
	
	private String IP;
	private String Puerto;
	private String identificadorSwarm;

	public Peer() {
		this.setIP("");
		this.setPuerto("");
		
	}

	public Peer(String iP, String puerto, String identificadorSwarm) {
		super();
		IP = iP;
		Puerto = puerto;
		this.identificadorSwarm = identificadorSwarm;
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
	public String getIdentificadorSwarm() {
		return identificadorSwarm;
	}
	public void setIdentificadorSwarm(String identificadorSwarm) {
		this.identificadorSwarm = identificadorSwarm;
	}

}
