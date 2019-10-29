package Listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TimerTask;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import Mensajes.Keepalive;
import Objetos.Tracker;

public class KeepaliveListener implements MessageListener {

	private ArrayList<Tracker> trackers=new ArrayList<Tracker>();//Arraylist de trackers activos
	
	private ArrayList<Integer> SeguimientoKeepalive = new ArrayList<Integer>(1);//array que contiene numero de keepalives, en caso de que haya diferencia de tres unidades->expulsion
	private int ContadorKeepAlives;
	
	
	//FIXME Cuando no se envian keepalives, no se actualizan los metodos (no se ejecuta comprobar para eliminar el tracker, ya que hay solo uno de momento)
	//FIXME Hay que implementar el ejecutar varios trackers y desconectar uno para ver que funciona (dos ordenadores en el mismo server activeMQ)
	//y que los datos del tracker sean diferentes (asignar id, la ip y eso bien y no hardcodeado)
	
	//FUNCIONALIDAD DE MASTER (meter comprobaciones mas adelante)
	public void comprobar() {
		int i;
		//evitar que sume numeros muy altos
		if(Collections.min(SeguimientoKeepalive)==1000) {
			for(i=0;i<SeguimientoKeepalive.size();i++) {
				SeguimientoKeepalive.set(i, SeguimientoKeepalive.get(i)-1000);
			}
			setContadorKeepAlives(getContadorKeepAlives()-1000);
		}
		//comprobar que no hay fallos de conexion 
		for(i=0;i<SeguimientoKeepalive.size();i++) {
			if(ContadorKeepAlives-SeguimientoKeepalive.get(i)>=3) {
				//Si los keepalive se retrasan, se expulsa
				SeguimientoKeepalive.remove(i);
				trackers.remove(i);
				System.out.println("Expulsado");
			}
			
		}
	}

	
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
				//FIXME esto es una prueba de almacenamiento de trackers
				
				if(trackers.size()==0) {
//					System.out.println("Insertando en trackers");
					trackers.add(new Tracker(keepAlive.getI(),keepAlive.getIp(),"20",true));
					SeguimientoKeepalive.add(1);
					setContadorKeepAlives(1);
//					System.out.println("Insertado");
				}
				
				
				
				//Comprobacion
				for(Tracker tracker:trackers) {
					if(tracker.getId()==keepAlive.getI()) {
						System.err.println("Esta en la lista el tracker con id= "+tracker.getId());
						//Array de ints manteniendo diferencias
						int auxIndex=trackers.indexOf(tracker);
						SeguimientoKeepalive.set((auxIndex), (SeguimientoKeepalive.get(auxIndex)+1));
					}
					else {
						System.err.println("No hay tracker");
						//Crear el tracker
						trackers.add(new Tracker(keepAlive.getI(),keepAlive.getIp(),"20",false));
						//añadir cuenta
						SeguimientoKeepalive.add(getContadorKeepAlives());
						}
						
						
					
				}		
				setContadorKeepAlives(Collections.max(SeguimientoKeepalive));
				comprobar();
				System.out.println("trackers:"+trackers);
			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}		
	}


	
	public ArrayList<Integer> getSeguimientoKeepalive() {
		return SeguimientoKeepalive;
	}

	public void setSeguimientoKeepalive(ArrayList<Integer> seguimientoKeepalive) {
		SeguimientoKeepalive = seguimientoKeepalive;
	}

	public int getContadorKeepAlives() {
		return ContadorKeepAlives;
	}


	public void setContadorKeepAlives(int contadorKeepAlives) {
		ContadorKeepAlives = contadorKeepAlives;
	}
}