package Mensajes;

import java.io.Serializable;

public class ActualizacionBD implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int identificador;
	
	public ActualizacionBD() {
		
	}
	
	public ActualizacionBD(int identificador) {
		this.identificador=identificador;
	}

	public int getIdentificador() {
		return identificador;
	}

	public void setIdentificador(int identificador) {
		this.identificador = identificador;
	}
	
	
	
}
