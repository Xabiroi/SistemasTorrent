package Objetos;
import java.util.Observable;

public class Peer extends Observable{
	
	private String IP;
	private int Puerto;
	private String identificadorSwarm;
	private int transactionId;
	private int connectionIdPrincipal;
	private int connectionIdSecundario;

	public Peer() {
		this.setIP("");
		this.setPuerto(0);
		
	}

	public Peer(String iP, int puerto, String identificadorSwarm) {
		super();
		IP = iP;
		Puerto = puerto;
		this.identificadorSwarm = identificadorSwarm;
	}
	

	public Peer(String iP, int puerto, String identificadorSwarm, int transactionId, int connectionIdPrincipal,
			int connectionIdSecundario) {
		super();
		IP = iP;
		Puerto = puerto;
		this.identificadorSwarm = identificadorSwarm;
		this.transactionId = transactionId;
		this.connectionIdPrincipal = connectionIdPrincipal;
		this.connectionIdSecundario = connectionIdSecundario;
	}

	public int getConnectionIdPrincipal() {
		return connectionIdPrincipal;
	}

	public void setConnectionIdPrincipal(int connectionIdPrincipal) {
		this.connectionIdPrincipal = connectionIdPrincipal;
	}

	public int getConnectionIdSecundario() {
		return connectionIdSecundario;
	}

	public void setConnectionIdSecundario(int connectionIdSecundario) {
		this.connectionIdSecundario = connectionIdSecundario;
	}

	public int getPuerto() {
		return Puerto;
	}


	public void setPuerto(int puerto) {
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

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

}
