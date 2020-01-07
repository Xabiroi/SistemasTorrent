package Objetos;
import java.util.Observable;

public class Peer extends Observable{
	
	private String IP;
	private int Puerto;
	private String identificadorSwarm;
	private int transactionId;
	private long connectionIdPrincipal;
	private long connectionIdSecundario;
	private long tiempo;
	private long descargado;
	
	///////////////////////
	//Los atributos 
	private long left;
	


	public Peer(String iP, int puerto, String identificadorSwarm, long descargado, long left) {
		super();
		IP = iP;
		Puerto = puerto;
		this.identificadorSwarm = identificadorSwarm;
		this.descargado = descargado;
		this.left = left;
	}
	

	public Peer(String iP, int puerto, long descargado) {
		super();
		IP = iP;
		Puerto = puerto;
		this.descargado = descargado;
	}

	public Peer() {
		this.setIP("");
		this.setPuerto(0);
		
	}

	public Peer(String iP, int puerto) {
		super();
		IP = iP;
		Puerto = puerto;
	}
	
	public Peer(String iP, int puerto, String identificadorSwarm) {
		super();
		IP = iP;
		Puerto = puerto;
		this.identificadorSwarm = identificadorSwarm;
	}

	public Peer(String iP, int puerto, String identificadorSwarm, int transactionId, long connectionIdPrincipal,
			long connectionIdSecundario) {
		super();
		IP = iP;
		Puerto = puerto;
		this.identificadorSwarm = identificadorSwarm;
		this.transactionId = transactionId;
		this.connectionIdPrincipal = connectionIdPrincipal;
		this.connectionIdSecundario = connectionIdSecundario;
		this.tiempo = System.currentTimeMillis();
	}
	
	public Peer(String iP, int puerto, String identificadorSwarm, int transactionId, long connectionIdPrincipal,
			long connectionIdSecundario, long tiempo) {
		super();
		IP = iP;
		Puerto = puerto;
		this.identificadorSwarm = identificadorSwarm;
		this.transactionId = transactionId;
		this.connectionIdPrincipal = connectionIdPrincipal;
		this.connectionIdSecundario = connectionIdSecundario;
		this.tiempo = tiempo;
	}
	

	public long getConnectionIdPrincipal() {
		return connectionIdPrincipal;
	}

	public void setConnectionIdPrincipal(long connectionIdPrincipal) {
		this.connectionIdPrincipal = connectionIdPrincipal;
	}

	public long getConnectionIdSecundario() {
		return connectionIdSecundario;
	}

	public void setConnectionIdSecundario(long connectionIdSecundario) {
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

	public long getTiempo() {
		return tiempo;
	}

	public void setTiempo(long tiempo) {
		this.tiempo = tiempo;
	}

	

	public long getDescargado() {
		return descargado;
	}

	public void setDescargado(long descargado) {
		this.descargado = descargado;
	}

	public long getLeft() {
		return left;
	}

	public void setLeft(long left) {
		this.left = left;
	}


}
