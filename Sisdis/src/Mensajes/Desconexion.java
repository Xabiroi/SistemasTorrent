package Mensajes;

import java.io.Serializable;

public class Desconexion implements Serializable {
	private int idOrigen;
	
	public Desconexion(int idOrigen) {
		this.idOrigen=idOrigen;
	}
	public int getIdOrigen() {
		return idOrigen;
	}
	public void setIdOrigen(int idOrigen) {
		this.idOrigen = idOrigen;
	}
}
