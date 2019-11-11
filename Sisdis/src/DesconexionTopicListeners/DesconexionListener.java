package DesconexionTopicListeners;

import java.util.ArrayList;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import Controllers.DataController.EstadosEleccionMaster;
import Mensajes.Desconexion;
import NuevoMasterSelectListener.NuevoMasterTopicPublisher;
import Objetos.Tracker;

public class DesconexionListener implements MessageListener {
	private ArrayList<Tracker> trackers;
	private NuevoMasterTopicPublisher nuevoMasterTopicPublisher;
	
	public DesconexionListener(ArrayList<Tracker> trackers, NuevoMasterTopicPublisher nuevoMasterTopicPublisher) {
		super();
		this.trackers = trackers;
		this.nuevoMasterTopicPublisher = nuevoMasterTopicPublisher;
	}

	@Override
	//This method is call when a new Message arrives to the topic
	public void onMessage(Message message) {		
		if (message != null) {
			try {
				ObjectMessage objectMessage = (ObjectMessage) message;					
				Desconexion desconexion = (Desconexion) objectMessage.getObject();
				
				System.out.println("     - Received Desconexion ID: " + desconexion.getIdOrigen());
				
				//TODO eliminar al tracker de mi lista de trackers
				for(Tracker tracker : trackers) {
					if(tracker.getId() == desconexion.getIdOrigen())
						trackers.remove(tracker);
						
						if(tracker.isMaster()) {
							ArrayList<EstadosEleccionMaster> estadosEleccionMasters = new ArrayList<EstadosEleccionMaster>();
							estadosEleccionMasters.add(EstadosEleccionMaster.Decidiendo);
							nuevoMasterTopicPublisher.setEstadoActual(estadosEleccionMasters); 
						}
				}
								
			} catch (Exception ex) {
				System.err.println("# Desconexion Listener TopicListener error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}		
	}


	

}