package Controllers;
import java.util.ArrayList;

import Objetos.Swarm;

public class DataController {
	
	
	private ArrayList<Swarm> Enjambres;

	
	public DataController() {
		
		
		
	}


	public ArrayList<Swarm> getEnjambres() {
		return Enjambres;
	}


	public void setEnjambres(ArrayList<Swarm> enjambres) {
		Enjambres = enjambres;
	}

	public void insertarPeer() {}
	public void actualizarPeer() {}
	public void borrarPeer() {}
	public void obtenerPeers() {}
	
	public void insertarSwarm() {}
	public void actualizarSwarm() {}
	public void borrarSwarm() {}
	public void obtenerSwarms() {}

	public void insertarPeerSwarmRelation() {}
	public void actualizarPeerSwarmRelation() {}
	public void borrarPeerSwarmRelation() {}
	public void obtenerPeerSwarmRelation() {}
	
	
	


}

