package Objetos;
//import java.io.File; //FIXME
import java.util.ArrayList;
import java.util.Observable;

public class Swarm extends Observable{
	
	private ArrayList<Peer> ListaPeers;
//  FIXME
//	private String identificadorSwarm;
//	private File HashArchivo;
//	o juntar los dos con un separador # entre los dos valores y luego substring
	
	
	public Swarm() {
		this.setListaPeers(new ArrayList<Peer>());

		
	}

	public ArrayList<Peer> getListaPeers() {
		return ListaPeers;
	}

	public void setListaPeers(ArrayList<Peer> listaPeers) {
		ListaPeers = listaPeers;
	}

//	public String getIdentificadorSwarm() {
//		return identificadorSwarm;
//	}
//
//	public void setIdentificadorSwarm(String identificadorSwarm) {
//		this.identificadorSwarm = identificadorSwarm;
//	}
//
//	public File getHashArchivo() {
//		return HashArchivo;
//	}
//
//	public void setHashArchivo(File hashArchivo) {
//		HashArchivo = hashArchivo;
//	}

}
