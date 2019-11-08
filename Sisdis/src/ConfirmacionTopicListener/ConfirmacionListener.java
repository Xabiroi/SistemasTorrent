package ConfirmacionTopicListener;

import java.util.ArrayList;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import Mensajes.Confirmacion;
import Objetos.Tracker;

//TODO elegir estados en los que puede estar al recibir una confirmacion
enum EstadosConfirmacion
{ 
	parado, //este lo he pensado para cuando no tenga nada que hacer
			//el resto de estados pueden acabar seleccionando este al acabar
	esperandoBD, 
	avisandoDeDesconexion, 
	decidiendoNuevoMaster, 
	preparadoParaActualizar, 
	sugerenciaEnviada,
}

public class ConfirmacionListener implements MessageListener {
	private ArrayList<Tracker> trackers;
	private Tracker miTracker;
	public EstadosConfirmacion estadosConfirmacion;
	
	
	public ConfirmacionListener(ArrayList<Tracker> trackers, Tracker miTracker) {
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
				Confirmacion confirmacion = (Confirmacion) objectMessage.getObject();
				
				System.out.println("     - Received Origin ID: " + confirmacion.getIdOrigen());

				switch(estadosConfirmacion) {
				case esperandoBD:
					//TODO guardar base de datos
					break;
					
				case avisandoDeDesconexion:
					//TODO desconectarse
					break;
				
				case decidiendoNuevoMaster:
					//TODO miTracker.setMaster(true/false)
					break;
					
				case preparadoParaActualizar:
					//TODO actualizar BD
					break;
					
				case sugerenciaEnviada:
					//TODO enviar PreparacionDeActualización
					break;
				default :
					break;
				}
				
			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}		
	}


	

}