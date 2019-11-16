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
				
				//FIXME contemplar el caso de no haber trackers cuando se inicia la aplicacion para la eleccion de id
				//el for no se si dara null pointer
				
				//Si llega keepalive con id 0
				if(keepAlive.getI()==0) {
					System.out.println("HA LLEGADO UN TRACKER CON ID 0");
					//Si vemos que nuestro id no es 0 pasamos(no somos nosotros)
					if(miTracker.getId()==0) {
						System.out.println("AÑADIENDO TRACKER CON ID 0");
						//########################################
							//Compruebas que no hay ids iguales para actualizar los tiempos
							boolean encontrado=false;
							for(Tracker tracker:trackers) {
								if(tracker.getId()==keepAlive.getI()) {
									encontrado=true;
								}
							}	
							if(encontrado==false) {					
								trackers.add(new Tracker(keepAlive.getI(),keepAlive.getIp(),"20",false,System.currentTimeMillis()));
							}
							
							//########################################						

						if(encontrado) {
						int master=0;
						for(Tracker tracker:trackers) {
							if(tracker.getId()>=max) {
								max=tracker.getId();
								System.out.println("EL VALOR DE MAX="+max);
							}
							if(tracker.isMaster()==true) {
								System.out.println("Tracker master encontrado");
								master++;
							}
							
						}
						if(master==0) {miTracker.setMaster(true);}
						
						miTracker.setId(max+1);
						System.out.println("EL ID NUESTRO PUESTO A miTracker ="+miTracker.getId());
						
						System.out.println("Asignando id al tracker nuevo...");
						System.out.println("     - Keep Alive ID: " + miTracker.getId());
						System.out.println("     - Keep Alive IP: " + keepAlive.getIp());
						}
					}
					
				}else {
					System.out.println("     - Keep Alive ID: " + keepAlive.getI());
					System.out.println("     - Keep Alive IP: " + keepAlive.getIp());
				
					//FIXME esto no deberia contemplarse nunca creo (el if de abajo)
					//Si no hay nadie te anyades como master
					if(trackers.size()==0) {
						trackers.add(new Tracker(max+1,keepAlive.getIp(),"20",true,System.currentTimeMillis()));
					}else {
						
						for(Tracker tracker:trackers) {
							if(tracker.getId()>=max) {
								max=tracker.getId();
								System.out.println("EL VALOR DE MAX EN EL ELSE="+max);
							}
						}
						//Compruebas que no hay ids iguales para actualizar los tiempos
						boolean encontrado=false;
						for(Tracker tracker:trackers) {
							System.out.println("trackers size=="+trackers.size());
							System.out.println("OOOOOOOOOOOOOOOOOOOO");
							System.out.println("EL ID NUESTRO PUESTO A miTracker ="+miTracker.getId());
							System.out.println("EL IP miTracker ="+miTracker.getIP());
							System.out.println("tracker.getId()="+tracker.getId());
							System.out.println("tracker.getIp()="+tracker.getIP());
							System.out.println("keepAlive.getI()="+keepAlive.getI());
							System.out.println("keepAlive.getIp()="+keepAlive.getIp());
							System.out.println("OOOOOOOOOOOOOOOOOOOO");
							if(tracker.getIP()==keepAlive.getIp()) {
								System.out.println("&&&&&&&&&&&&&&&");
								System.out.println("LO HA ENCONTRADO");
								System.out.println("&&&&&&&&&&&&&&&");
								encontrado=true;
								tracker.setTiempo(System.currentTimeMillis());
								//Break; FIXME
							}
						}	
						
						//Si no se ha encontrado anyadir tracker
						//FIXME quitar el puerto de los trackers ya que es 6161?
						if(encontrado==false) {				
							System.out.println("/////////////////////");
							System.out.println("AÑADIENDO");
							System.out.println("/////////////////////");
							
							for(Tracker tracker:trackers) {
								System.out.println("Los trackers que hay=="+tracker.getIP());
							}
							
							System.out.println("Trackers ESTA AHORA ASI ANTES DE AÑADIR=="+trackers.size());
							trackers.add(new Tracker((max+1),keepAlive.getIp(),"20",false,System.currentTimeMillis()));
							System.out.println("Trackers ESTA AHORA ASI DESPUES DE AÑADIR=="+trackers.size());
							System.out.println("/////////////////////");
							//enviadorBD.start();FIXME
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