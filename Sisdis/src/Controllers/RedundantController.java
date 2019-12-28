package Controllers;
import java.util.ArrayList;
import java.util.Iterator;

import BDFileQueueListener.QueueFileReceiver;
import BDFileQueueListener.QueueFileSender;
import KeepAliveTopicListeners.KeepaliveTopicPublisher;
import KeepAliveTopicListeners.KeepaliveTopicSubscriber;
import NuevoMasterSelectListener.NuevoMasterTopicPublisher;
import NuevoMasterSelectListener.NuevoMasterTopicSubscriber;
import Objetos.Tracker;

public class RedundantController extends Thread{
	
	public static Tracker miTracker;
	private static ArrayList<Tracker> TrackersRedundantes=new ArrayList<Tracker>();
	private static KeepaliveTopicPublisher KeepaliveTopicPublisher;
	private static KeepaliveTopicSubscriber KeepaliveTopicSubscriber;
	private static RedundantController RedundantController;
	private static NuevoMasterTopicSubscriber NuevoMasterTopicSubscriber;
	private static NuevoMasterTopicPublisher NuevoMasterTopicPublisher;
	private static ArrayList<DataController.EstadosEleccionMaster> estadosEleccionMasters = new ArrayList<DataController.EstadosEleccionMaster>(1);
	private static QueueFileSender enviadorBD;
	private static QueueFileReceiver recibidorBD;
	private ArrayList<Boolean> desconexion=new ArrayList<Boolean>(1);
	
	public RedundantController(ArrayList<Tracker> trackersRedundantes, KeepaliveTopicPublisher keepaliveTopicPublisher,
			KeepaliveTopicSubscriber keepaliveTopicSubscriber,Tracker myTracker, NuevoMasterTopicPublisher nuevoMasterTopicPublisher,ArrayList<Boolean> desconexion) {
		super();
		TrackersRedundantes = trackersRedundantes;
		miTracker = myTracker;
		setKeepaliveTopicPublisher(keepaliveTopicPublisher);
		setKeepaliveTopicSubscriber(keepaliveTopicSubscriber);
		setNuevoMasterTopicPublisher(nuevoMasterTopicPublisher);
		setDesconexion(desconexion);

	}
	


	public void comprobar() {
		if(TrackersRedundantes.size()!=0) {
			for(Iterator<Tracker> iterator = TrackersRedundantes.iterator(); iterator.hasNext();) {
				Tracker tracker =iterator.next();
//				System.out.println("System.currentTimeMillis()"+System.currentTimeMillis());
//				System.out.println("tracker.getTiempo()"+tracker.getTiempo());
//				System.out.println("Resta="+(System.currentTimeMillis()-tracker.getTiempo()));
				if(System.currentTimeMillis()-tracker.getTiempo()>1500) {
					System.out.println("Quitando el tracker");
					iterator.remove();
					if(tracker.isMaster()) {
						NuevoMasterTopicPublisher.elegirNuevoMaster();
					}
				}
				if(miTracker.getId()==tracker.getId()) {
					if(miTracker.isMaster()) {
						tracker.setMaster(true);
					}
				}
				
			}
		}
	}
	
	public void run() {

			while(desconexion.get(0)) {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			System.out.println("Comprobando...");
			synchronized(TrackersRedundantes) {
			comprobar();
			}
		}
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
	
	public static QueueFileSender getEnviadorBD() {
		return enviadorBD;
	}



	public static void setEnviadorBD(QueueFileSender enviadorBD) {
		RedundantController.enviadorBD = enviadorBD;
	}



	public static QueueFileReceiver getRecibidorBD() {
		return recibidorBD;
	}



	public static void setRecibidorBD(QueueFileReceiver recibidorBD) {
		RedundantController.recibidorBD = recibidorBD;
	}



	public static NuevoMasterTopicSubscriber getNuevoMasterTopicSubscriber() {
		return NuevoMasterTopicSubscriber;
	}



	public static void setNuevoMasterTopicSubscriber(NuevoMasterTopicSubscriber nuevoMasterTopicSubscriber) {
		NuevoMasterTopicSubscriber = nuevoMasterTopicSubscriber;
	}



	public static ArrayList<DataController.EstadosEleccionMaster> getEstadosEleccionMasters() {
		return estadosEleccionMasters;
	}



	public static void setEstadosEleccionMasters(ArrayList<DataController.EstadosEleccionMaster> estadosEleccionMasters) {
		RedundantController.estadosEleccionMasters = estadosEleccionMasters;
	}

	public static NuevoMasterTopicPublisher getNuevoMasterTopicPublisher() {
		return NuevoMasterTopicPublisher;
	}



	public static void setNuevoMasterTopicPublisher(NuevoMasterTopicPublisher nuevoMasterTopicPublisher) {
		NuevoMasterTopicPublisher = nuevoMasterTopicPublisher;
	}
	
	public ArrayList<Boolean> getDesconexion() {
		return desconexion;
	}



	public void setDesconexion(ArrayList<Boolean> desconexion) {
		this.desconexion = desconexion;
	}


}
