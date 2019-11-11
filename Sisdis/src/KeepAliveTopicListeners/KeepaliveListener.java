package KeepAliveTopicListeners;

import java.util.ArrayList;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import Mensajes.Keepalive;
import Objetos.Tracker;

public class KeepaliveListener implements MessageListener {
	private ArrayList<Tracker> trackers;
	private Tracker miTracker;
		
	public KeepaliveListener(ArrayList<Tracker> trackers, Tracker miTracker) {
		super();
		this.trackers = trackers;
		this.setMiTracker(miTracker);
	}

	@Override
	//This method is call when a new Message arrives to the topic
	public void onMessage(Message message) {
		int max=0;
		if (message != null) {
			try {
				ObjectMessage objectMessage = (ObjectMessage) message;					
				Keepalive keepAlive = (Keepalive) objectMessage.getObject();
				//Si llega keepalive con id 0
				if(keepAlive.getI()==0) {
					//Si vemos que nuestro id no es 0 pasamos(no somos nosotros)
					if(miTracker.getId()==0) {
						for(Tracker tracker:trackers) {
							if(tracker.getId()>max) {
								max=tracker.getId();
							}
						}
						miTracker.setId(max+1);
						System.out.println("Asignando id al tracker nuevo...");
						System.out.println("     - Keep Alive ID: " + miTracker.getId());
						System.out.println("     - Keep Alive IP: " + keepAlive.getIp());
						
					}
				}else {
					System.out.println("     - Keep Alive ID: " + keepAlive.getI());
					System.out.println("     - Keep Alive IP: " + keepAlive.getIp());
				
					//Si no hay nadie te anyades como master
					if(trackers.size()==0) {
						trackers.add(new Tracker(keepAlive.getI(),keepAlive.getIp(),"20",true,System.currentTimeMillis()));
					}else {
						//Compruebas que no hay ids iguales para actualizar los tiempos
						boolean encontrado=false;
						for(Tracker tracker:trackers) {
							if(tracker.getId()==keepAlive.getI()) {
								encontrado=true;
								tracker.setTiempo(System.currentTimeMillis());
							}
						}	
						
						//Si no se ha encontrado anyadir tracker
						if(encontrado==false) {					
							trackers.add(new Tracker((max+1),keepAlive.getIp(),"20",false,System.currentTimeMillis()));
//							trackers.add(new Tracker(keepAlive.getI(),keepAlive.getIp(),"20",false,System.currentTimeMillis()));
						}
					}
					System.out.println("trackers:"+trackers);
					
					for(Tracker tracker:trackers) {
						System.out.println("tracker1="+trackers.get(trackers.indexOf(tracker)).getIP());
					}
					
				}
				
			} catch (Exception ex) {
				System.err.println("# KeepAlive Listener TopicListener error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}		
	}

	public Tracker getMiTracker() {
		return miTracker;
	}

	public void setMiTracker(Tracker miTracker) {
		this.miTracker = miTracker;
	}


	

}