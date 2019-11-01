package Listeners;

import java.util.ArrayList;
import java.util.Date;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import Mensajes.Keepalive;
import Objetos.Tracker;

public class KeepaliveListener implements MessageListener {
	private ArrayList<Tracker> trackers;
	
	
		
	public KeepaliveListener(ArrayList<Tracker> trackers) {
		super();
		this.trackers = trackers;
	}

	@Override
	//This method is call when a new Message arrives to the topic
	public void onMessage(Message message) {		
		if (message != null) {
			try {
				ObjectMessage objectMessage = (ObjectMessage) message;					
				Keepalive keepAlive = (Keepalive) objectMessage.getObject();
				
				System.out.println("     - Keep Alive ID: " + keepAlive.getI());
				System.out.println("     - Keep Alive IP: " + keepAlive.getIp());

				//TODO PROCESO DE AÑADIR 2 TRACKERS A LA VEZ y asignar ids reales
				if(trackers.size()==0) {
					trackers.add(new Tracker(keepAlive.getI(),keepAlive.getIp(),"20",true,System.currentTimeMillis()));
				}

				
				boolean encontrado=false;
				for(Tracker tracker:trackers) {
					if(tracker.getId()==keepAlive.getI()) {
						encontrado=true;
						tracker.setTiempo(System.currentTimeMillis());
//						trackers.get(trackers.indexOf(tracker)).setTiempo(System.currentTimeMillis());
					}
				}	
				
				//Si no se ha encontrado añadir
				if(encontrado==false) {
					trackers.add(new Tracker(keepAlive.getI(),keepAlive.getIp(),"20",false,System.currentTimeMillis()));
				}
				
				System.out.println("trackers:"+trackers);
				
			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}		
	}


	

}