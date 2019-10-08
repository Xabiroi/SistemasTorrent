package Mensajes;

public class SugerenciaActualizacion {
	private int idPeer;
	
	public SugerenciaActualizacion() {}
	public SugerenciaActualizacion(int idPeer) {
		this.setIdPeer(idPeer);
	}
	public int getIdPeer() {
		return idPeer;
	}
	public void setIdPeer(int idPeer) {
		this.idPeer = idPeer;
	}
}
