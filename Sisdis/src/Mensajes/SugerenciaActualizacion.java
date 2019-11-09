package Mensajes;

import java.io.Serializable;

//FIXME id peer o ip de peer (?)
public class SugerenciaActualizacion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ipPeer;
	
	public SugerenciaActualizacion() {}
	public SugerenciaActualizacion(String ipPeer) {
		this.setIpPeer(ipPeer);
	}
	public String getIpPeer() {
		return ipPeer;
	}
	public void setIpPeer(String idPeer) {
		this.ipPeer = idPeer;
	}
}
