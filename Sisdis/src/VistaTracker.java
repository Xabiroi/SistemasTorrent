import java.util.Observable;
import java.util.Observer;

public class VistaTracker implements Observer{

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	private TorrentController Tcontroller;
	private RedundantController RController;
	private DataController DController;
	

	public VistaTracker() {
		this.setTcontroller(new TorrentController());
		this.setRController(new RedundantController());
		this.setDController(new DataController());

		
	}
	public VistaTracker(TorrentController TC,RedundantController RC,DataController DC) {
		this.setTcontroller(TC);
		this.setRController(RC);
		this.setDController(DC);
		
		
		
	}
	public TorrentController getTcontroller() {
		return Tcontroller;
	}
	public void setTcontroller(TorrentController tcontroller) {
		Tcontroller = tcontroller;
	}
	public RedundantController getRController() {
		return RController;
	}
	public void setRController(RedundantController rController) {
		RController = rController;
	}
	public DataController getDController() {
		return DController;
	}
	public void setDController(DataController dController) {
		DController = dController;
	}
	
	
	
	
	
	
	
	
}

