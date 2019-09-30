import java.util.ArrayList;

public class RedundantController {
	
	private ArrayList<Tracker> TrackersRedundantes;
	
	//Lo de master-slave?
	public RedundantController() {
		this.setTrackersRedundantes(new ArrayList<Tracker>());
		
	}

	public ArrayList<Tracker> getTrackersRedundantes() {
		return TrackersRedundantes;
	}

	public void setTrackersRedundantes(ArrayList<Tracker> trackersRedundantes) {
		TrackersRedundantes = trackersRedundantes;
	}
	
	//TODO Funciones de eleccion amster slave
	//COmunicaciones entre los trackers(?)
	
}
