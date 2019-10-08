package Controllers;
import java.util.ArrayList;

import Objetos.Tracker;

public class RedundantController {
	
	private ArrayList<Tracker> TrackersRedundantes;
	
	//master-slave
	public RedundantController() {
		this.setTrackersRedundantes(new ArrayList<Tracker>());
		
	}

	public ArrayList<Tracker> getTrackersRedundantes() {
		return TrackersRedundantes;
	}

	public void setTrackersRedundantes(ArrayList<Tracker> trackersRedundantes) {
		TrackersRedundantes = trackersRedundantes;
	}
	
	public void expulsar() {}
	public void unirseARed() {}
	public void conectarJMS() {}
	public void desconexion() {}
	

	
}
