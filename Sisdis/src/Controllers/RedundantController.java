package Controllers;
import java.util.ArrayList;
import java.util.Iterator;

import KeepAliveTopicListeners.KeepaliveTopicPublisher;
import KeepAliveTopicListeners.KeepaliveTopicSubscriber;
import Objetos.Tracker;

public class RedundantController extends Thread{
	
	public static Tracker miTracker;
	private static ArrayList<Tracker> TrackersRedundantes=new ArrayList<Tracker>();
	private static KeepaliveTopicPublisher KeepaliveTopicPublisher;
	private static KeepaliveTopicSubscriber KeepaliveTopicSubscriber;
	
	
	//master-slave
	public RedundantController(ArrayList<Tracker> trackersRedundantes, KeepaliveTopicPublisher keepaliveTopicPublisher,
			KeepaliveTopicSubscriber keepaliveTopicSubscriber, Tracker myTracker) {
		super();
		TrackersRedundantes = trackersRedundantes;
		miTracker = myTracker;
		setKeepaliveTopicPublisher(keepaliveTopicPublisher);
		setKeepaliveTopicSubscriber(keepaliveTopicSubscriber);
	}
	
	
//	for(Tracker tracker:TrackersRedundantes) {
//		System.out.println("System.currentTimeMillis()"+System.currentTimeMillis());
//		System.out.println("tracker.getTiempo()"+tracker.getTiempo());
//		System.out.println("Resta="+(System.currentTimeMillis()-tracker.getTiempo()));
//		if(System.currentTimeMillis()-tracker.getTiempo()>1500) {
//			TrackersRedundantes.remove(TrackersRedundantes.indexOf(tracker));
//		}
//		
//	}

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
		
		KeepaliveTopicSubscriber keepaliveTopicSubscriber=new KeepaliveTopicSubscriber(getTrackersRedundantes(), miTracker);
		keepaliveTopicSubscriber.start();
		
		KeepaliveTopicPublisher keepaliveTopicPublisher=new KeepaliveTopicPublisher(TrackersRedundantes, miTracker);
		keepaliveTopicPublisher.start();

		RedundantController redundantController = new RedundantController(TrackersRedundantes, keepaliveTopicPublisher, keepaliveTopicSubscriber, miTracker);

		redundantController.start();
		
	}
	
	
	
	public void expulsar() {}
	public void unirseARed() {}
	public void conectarJMS() {}
	public void desconexion() {}
	
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
	


	
}
