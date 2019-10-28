package Listeners;

import java.util.ArrayList;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import Mensajes.Keepalive;
import Objetos.Tracker;

public class KeepaliveListener implements MessageListener {

	private ArrayList<Tracker> trackers=new ArrayList<Tracker>();
	
	@Override
	//This method is call when a new Message arrives to the topic
	public void onMessage(Message message) {		
		if (message != null) {
			try {
//				System.out.println("   - TopicListener: " + message.getClass().getSimpleName() + " received!");
//				System.out.println("     - TopicListener: ObjectMessage id = '" + message.getClass().getCanonicalName());
				ObjectMessage objectMessage = (ObjectMessage) message;					
				Keepalive keepAlive = (Keepalive) objectMessage.getObject();
				
				System.out.println("     - Keep Alive ID: " + keepAlive.getI());
				System.out.println("     - Keep Alive IP: " + keepAlive.getIp());
				
				//TODO Crear la maquina de estados?
				//TODO FIXME Mover esta actividad a una funcion que se le llame desde fuera(?)
				//FIXME esto es una prueba de almacenamiento de trackers
				if(trackers.size()==0) {
					System.out.println("Insertando en trackers");
					trackers.add(new Tracker(keepAlive.getI(),keepAlive.getIp(),"20",true));
				}
				//Comprobacion
				for(Tracker tracker:trackers) {
					if(tracker.getId()==keepAlive.getI()) {
						System.err.println("Esta en la lista el tracker con id= "+tracker.getId());
					}else {
						System.err.println("No hay tracker");
						trackers.add(new Tracker(keepAlive.getI(),keepAlive.getIp(),"20",false));
						//añadir tracker
					}
				}
				
					
					
					
				
			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
				//ex.printStackTrace();
			}
		}		
	}
}