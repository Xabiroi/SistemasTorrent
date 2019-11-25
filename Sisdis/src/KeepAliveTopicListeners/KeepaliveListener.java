package KeepAliveTopicListeners;

import java.util.ArrayList;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import BDFileQueueListener.QueueFileReceiver;
import BDFileQueueListener.QueueFileSender;
import Mensajes.Keepalive;
import Objetos.Tracker;

public class KeepaliveListener implements MessageListener {
	private ArrayList<Tracker> trackers;
	private Tracker miTracker;
	private QueueFileSender enviadorBD;
	private QueueFileReceiver recibidorBD;
	private int counter;
		
	
	public KeepaliveListener(ArrayList<Tracker> trackers, Tracker miTracker, QueueFileSender enviadorBD,
			QueueFileReceiver recibidorBD) {
		super();
		this.trackers = trackers;
		this.miTracker = miTracker;
		this.enviadorBD = enviadorBD;
		this.recibidorBD = recibidorBD;
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
//					//System.out.println("HA LLEGADO UN TRACKER CON ID 0");
					//Si vemos que nuestro id no es 0 pasamos(no somos nosotros)
					if(miTracker.getId()==0) {
//						//System.out.println("AÑADIENDO TRACKER CON ID 0");
						//########################################
							//Compruebas que no hay ids iguales para actualizar los tiempos
							boolean encontrado=false;
							for(Tracker tracker:trackers) {
								if(tracker.getId()==keepAlive.getI()) {
									encontrado=true;
								}
							}	
							if(encontrado==false) {	
								//Anyadirse a si mismo
								trackers.add(new Tracker(keepAlive.getI(),keepAlive.getIp(),"20",keepAlive.isB(),System.currentTimeMillis()));
							}
							
							//########################################						

						if(encontrado) {

						for(Tracker tracker:trackers) {
							if(tracker.getId()>=max) {
								max=tracker.getId();
//								//System.out.println("EL VALOR DE MAX="+max);
							}

						}

						
						miTracker.setId(max+1);

						if(trackers.size()==1) {
							miTracker.setMaster(true);
						}

						//System.out.println("Asignando id al tracker nuevo...");
						//System.out.println("ID: " + miTracker.getId());
						//System.out.println("IP: " + keepAlive.getIp());
						}
					}
					
				}else {
					//System.out.println("     - Keep Alive ID: " + keepAlive.getI());
					//System.out.println("     - Keep Alive IP: " + keepAlive.getIp());
				
					if(trackers.size()==0) {
						if(!(keepAlive.getI()==0)) {
						trackers.add(new Tracker(max+1,keepAlive.getIp(),"20",keepAlive.isB(),System.currentTimeMillis()));
						}
					}else {
						
						for(Tracker tracker:trackers) {
							if(tracker.getId()>=max) {
								max=tracker.getId();
//								//System.out.println("EL VALOR DE MAX EN EL ELSE="+max);
							}
						}
						//Compruebas que no hay ids iguales para actualizar los tiempos
						boolean encontrado=false;
						for(Tracker tracker:trackers) {
							if(tracker.getId()==keepAlive.getI()) {
								encontrado=true;
								tracker.setTiempo(System.currentTimeMillis());
							}
						}	
						
						//Si no se ha encontrado anyadir tracker
						//FIXME quitar el puerto de los trackers ya que es 6161?
						if(encontrado==false) {											
							
							trackers.add(new Tracker((max+1),keepAlive.getIp(),"20",keepAlive.isB(),System.currentTimeMillis()));

							//enviadorBD.start();FIXME ejecutarlo como el metodo 1 vez

						}
					}

					
					for(Tracker tracker:trackers) {
						//System.out.println("tracker "+trackers.get(trackers.indexOf(tracker))+"="+trackers.get(trackers.indexOf(tracker)).getIP());
					}
					
				}
				
			} catch (Exception ex) {
				System.err.println("# KeepAlive Listener TopicListener error 1 : " + ex.getMessage());
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

	public QueueFileSender getEnviadorBD() {
		return enviadorBD;
	}

	public void setEnviadorBD(QueueFileSender enviadorBD) {
		this.enviadorBD = enviadorBD;
	}

	public QueueFileReceiver getRecibidorBD() {
		return recibidorBD;
	}

	public void setRecibidorBD(QueueFileReceiver recibidorBD) {
		this.recibidorBD = recibidorBD;
	}


	

}