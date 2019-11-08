package Mensajes;

import java.io.Serializable;

public class Confirmacion implements Serializable {
	private int idOrigen;
	
	public Confirmacion(int idOrigen) {
		this.idOrigen=idOrigen;
	}
	public Confirmacion() {
		
	}

	public int getIdOrigen() {
		return idOrigen;
	}

	public void setIdOrigen(int idOrigen) {
		this.idOrigen = idOrigen;
	}
	
}
