package Controllers;
import java.util.ArrayList;
import java.util.Iterator;

import DesconexionTopicListeners.DesconexionTopicPublisher;
import DesconexionTopicListeners.DesconexionTopicSubscriber;
import KeepAliveTopicListeners.KeepaliveTopicPublisher;
import KeepAliveTopicListeners.KeepaliveTopicSubscriber;
import NuevoMasterSelectListener.NuevoMasterTopicPublisher;
import Objetos.Tracker;

public class RedundantController extends Thread{
	
	public static Tracker miTracker;
	private static ArrayList<Tracker> TrackersRedundantes=new ArrayList<Tracker>();
	private static KeepaliveTopicPublisher KeepaliveTopicPublisher;
	private static KeepaliveTopicSubscriber KeepaliveTopicSubscriber;
	private static DesconexionTopicSubscriber DesconexionTopicSubscriber;
	private static DesconexionTopicPublisher DesconexionTopicPublisher;
	private static RedundantController RedundantController;
	
	
	//master-slave
	public RedundantController(ArrayList<Tracker> trackersRedundantes, KeepaliveTopicPublisher keepaliveTopicPublisher,
			KeepaliveTopicSubscriber keepaliveTopicSubscriber, DesconexionTopicPublisher desconexionTopicPublisher,
			DesconexionTopicSubscriber desconexionTopicSubscriber,Tracker myTracker) {
		super();
		TrackersRedundantes = trackersRedundantes;
		miTracker = myTracker;
		setKeepaliveTopicPublisher(keepaliveTopicPublisher);
		setKeepaliveTopicSubscriber(keepaliveTopicSubscriber);
		setDesconexionTopicPublisher(desconexionTopicPublisher);
		setDesconexionTopicSubscriber(desconexionTopicSubscriber);
	}
	


	public void comprobar() {
		if(TrackersRedundantes.size()!=0) {
			for(Iterator<Tracker> iterator = TrackersRedundantes.iterator(); iterator.hasNext();) {
				Tracker tracker =iterator.next();
				System.out.println("System.currentTimeMillis()"+System.currentTimeMillis());
				System.out.println("tracker.getTiempo()"+tracker.getTiempo());
				System.out.println("Resta="+(System.currentTimeMillis()-tracker.getTiempo()));
				if(System.currentTimeMillis()-tracker.getTiempo()>1500) {
					System.out.println("Quitando el tracker");
					iterator.remove();
				}
				
			}
		}
	}
	
	public void run() {
		int loop=0;
		while(loop<6000) {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Comprobando...");
		comprobar();
		loop++;
		}
	}
	
	public static void main(String args[]) {
		//al tracker asignarle la ip de cada uno en la realidad
		Tracker miTracker = new Tracker(0,"192.168.5.46","30",false,System.currentTimeMillis());
		
		KeepaliveTopicSubscriber= new KeepaliveTopicSubscriber(getTrackersRedundantes(), miTracker);
		KeepaliveTopicPublisher = new KeepaliveTopicPublisher(TrackersRedundantes, miTracker);
	
		DesconexionTopicPublisher = new DesconexionTopicPublisher(DataController.EstadosEleccionMaster.Esperando, miTracker);
		DesconexionTopicSubscriber = new DesconexionTopicSubscriber(TrackersRedundantes, DataController.EstadosEleccionMaster.Esperando, new NuevoMasterTopicPublisher(TrackersRedundantes, miTracker, DataController.EstadosEleccionMaster.Esperando));

		RedundantController = new RedundantController(TrackersRedundantes, KeepaliveTopicPublisher, KeepaliveTopicSubscriber,  DesconexionTopicPublisher, DesconexionTopicSubscriber, miTracker);
				
		conectarJMS();
		System.out.println("CONECTADO");
	}
	
	
	
	public void expulsar() {}
	public void unirseARed() {}
	public static void conectarJMS() {
		KeepaliveTopicSubscriber.start();
		KeepaliveTopicPublisher.start();
		DesconexionTopicPublisher.start();
		DesconexionTopicSubscriber.start();
		RedundantController.start();
	}
	public static void desconexion() {
		DesconexionTopicPublisher.interrupt();
		DesconexionTopicSubscriber.interrupt();
		KeepaliveTopicPublisher.interrupt();
		KeepaliveTopicSubscriber.interrupt();
		RedundantController.interrupt();
	}
	
	public static ArrayList<Tracker> getTrackersRedundantes() {
		return TrackersRedundantes;
	}

	public void setTrackersRedundantes(ArrayList<Tracker> trackersRedundantes) {
		TrackersRedundantes = trackersRedundantes;
	}

	public static KeepaliveTopicSubscriber getKeepaliveTopicSubscriber() {
		return KeepaliveTopicSubscriber;
	}

	public static void setKeepaliveTopicSubscriber(KeepaliveTopicSubscriber keepaliveTopicSubscriber) {
		KeepaliveTopicSubscriber = keepaliveTopicSubscriber;
	}

	public static KeepaliveTopicPublisher getKeepaliveTopicPublisher() {
		return KeepaliveTopicPublisher;
	}

	public static void setKeepaliveTopicPublisher(KeepaliveTopicPublisher keepaliveTopicPublisher) {
		KeepaliveTopicPublisher = keepaliveTopicPublisher;
	}
	
	public static DesconexionTopicSubscriber getDesconexionTopicSubscriber() {
		return DesconexionTopicSubscriber;
	}

	public static void setDesconexionTopicSubscriber(DesconexionTopicSubscriber desconexionTopicSubscriber) {
		DesconexionTopicSubscriber = desconexionTopicSubscriber;
	}

	public static DesconexionTopicPublisher getDesconexionTopicPublisher() {
		return DesconexionTopicPublisher;
	}

	public static void setDesconexionTopicPublisher(DesconexionTopicPublisher desconexionTopicPublisher) {
		DesconexionTopicPublisher = desconexionTopicPublisher;
	}

	
}
