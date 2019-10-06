import java.util.Observable;

public class Tracker extends Observable{

	private String IP;
	private String Puerto;
	private boolean Master;
	
	
	public Tracker(){
		this.setIP("");
		this.setPuerto("");
		this.setMaster(false);
	}
	
	public Tracker(String IP,String Puerto,boolean Master){
		this.setIP(IP);
		this.setPuerto(Puerto);
		this.setMaster(Master);
	}


	public boolean isMaster() {
		return Master;
	}


	public void setMaster(boolean master) {
		Master = master;
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
	
	
	//En alguna funcion (detector de evento de que se desconecta o se cierra la app) -> notifyObservers()
}
