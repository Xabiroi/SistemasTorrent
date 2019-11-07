package NuevoMasterSelectListener;

import java.util.ArrayList;
import java.util.Date;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import Mensajes.NuevoMaster;
import Objetos.Tracker;

enum EstadosNuevoMaster
{ 
		comprobandoMensajes,
		tomandoDecision
}

public class NuevoMasterListener implements MessageListener {
	private ArrayList<Tracker> trackers;
	private Tracker miTracker;
	private int IdMasBajo = 0, numeroDeMensajesRecibidos = 0;
	public EstadosNuevoMaster estadosNuevoMaster;
	
	
	public NuevoMasterListener(ArrayList<Tracker> trackers, Tracker miTracker) {
		super();
		this.trackers = trackers;
		this.miTracker = miTracker;
	}

	@Override
	//This method is call when a new Message arrives to the topic
	public void onMessage(Message message) {		
		if (message != null) {
			try {
				ObjectMessage objectMessage = (ObjectMessage) message;					
				NuevoMaster nuevoMaster = (NuevoMaster) objectMessage.getObject();
				
				System.out.println("     - Received ID: " + nuevoMaster.getIdMaster());

				switch (estadosNuevoMaster) {
					
					case comprobandoMensajes:
						
						IdMasBajo = nuevoMaster.getIdMaster();
						numeroDeMensajesRecibidos++;
						
						if(numeroDeMensajesRecibidos == trackers.size()-1)
							estadosNuevoMaster = EstadosNuevoMaster.tomandoDecision;
						
						break;
						
					case tomandoDecision:
						if(miTracker.getId() == IdMasBajo)
							miTracker.setMaster(true);
						//no hay else porque no hay reasignación hasta que no se va el master
						break;
					default:
						System.out.println("Error: Estado de Nuevo Master no contemplado");
				} 
				
			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}		
	}


	

}