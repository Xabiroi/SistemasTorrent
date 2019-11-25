package NuevoMasterSelectListener;

import java.util.ArrayList;
import java.util.Date;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import Controllers.DataController.EstadosEleccionMaster;
import Mensajes.NuevoMaster;
import Objetos.Tracker;


public class NuevoMasterListener implements MessageListener {
	private ArrayList<Tracker> trackers;
	private Tracker miTracker;
	private int IdMasBajo = 0, numeroDeMensajesRecibidos = 0;
	public ArrayList<EstadosEleccionMaster> estadosEleccionMaster;
	
	
	public NuevoMasterListener(ArrayList<Tracker> trackers, Tracker miTracker, ArrayList<EstadosEleccionMaster> estadosEleccionMaster) {
		super();
		this.trackers = trackers;
		this.miTracker = miTracker;
		this.estadosEleccionMaster = estadosEleccionMaster;
	}

	@Override
	//This method is call when a new Message arrives to the topic
	public void onMessage(Message message) {		
		if (message != null) {
			try {
				ObjectMessage objectMessage = (ObjectMessage) message;					
				NuevoMaster nuevoMaster = (NuevoMaster) objectMessage.getObject();
				
				System.out.println("     - Received ID: " + nuevoMaster.getIdMaster());
				while(true) {
					System.out.println(estadosEleccionMaster.get(0));
					switch (estadosEleccionMaster.get(0)) {
					
					case Esperando:
						
						IdMasBajo = nuevoMaster.getIdMaster();
						numeroDeMensajesRecibidos++;
						
						if(numeroDeMensajesRecibidos == trackers.size()-1)
							estadosEleccionMaster.set(0, EstadosEleccionMaster.Decidiendo);
						
						break;
						
					case Decidiendo:
						
						System.out.println("Nuevo Estado De Elección: "+ estadosEleccionMaster.toString());
						if(miTracker.getId() == IdMasBajo)
							miTracker.setMaster(true);
						System.out.println("Decisión tomada: "+miTracker.isMaster());
						//no hay else porque no hay reasignación hasta que no se va el master
						estadosEleccionMaster.set(0, EstadosEleccionMaster.Decidiendo);
//						System.out.println("Estado de elección de master: "+estadosEleccionMaster.toString());
						break;
					default:
						System.out.println("Error: Estado de Nuevo Master no contemplado");
						break;
				} 
				}
				
				
			} catch (Exception ex) {
				System.err.println("# NuevoMaster Listener TopicListener error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}		
	}


	

}