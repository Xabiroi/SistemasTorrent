package Mensajes;

import java.util.Date;

public class Keepalive {
	private int idOrigen;
	private Date horaEnvio;
	private Date horaRecibido;
	private boolean master;
	
	public Keepalive() {

	}
	public Keepalive(int idOrigen,Date horaEnvio,Date horaRecibido,boolean master) {
		this.setIdOrigen(idOrigen);
		this.setHoraEnvio(horaEnvio);
		this.setHoraRecibido(horaRecibido);
		this.setMaster(master);
	}
	public Date getHoraEnvio() {
		return horaEnvio;
	}
	public void setHoraEnvio(Date horaEnvio) {
		this.horaEnvio = horaEnvio;
	}
	public int getIdOrigen() {
		return idOrigen;
	}
	public void setIdOrigen(int idOrigen) {
		this.idOrigen = idOrigen;
	}
	public Date getHoraRecibido() {
		return horaRecibido;
	}
	public void setHoraRecibido(Date horaRecibido) {
		this.horaRecibido = horaRecibido;
	}
	public boolean isMaster() {
		return master;
	}
	public void setMaster(boolean master) {
		this.master = master;
	}
}
