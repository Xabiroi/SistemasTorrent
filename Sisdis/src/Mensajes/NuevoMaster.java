package Mensajes;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NuevoMaster implements Serializable {
	private int idMaster;
	
	public NuevoMaster() {}
	
	public NuevoMaster(int idMaster) {
		this.setIdMaster(idMaster);
	}

	public int getIdMaster() {
		return idMaster;
	}

	public void setIdMaster(int idMaster) {
		this.idMaster = idMaster;
	}
}
