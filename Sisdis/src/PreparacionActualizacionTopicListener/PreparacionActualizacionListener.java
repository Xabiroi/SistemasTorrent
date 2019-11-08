package PreparacionActualizacionTopicListener;

import java.util.ArrayList;
import java.util.Date;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import ConfirmacionTopicListener.ConfirmacionTopicPublisher;
import Mensajes.NuevoMaster;
import Mensajes.PreparacionActualizacion;
import Objetos.Tracker;

/*
 * Esto estoy pensando en quitarlo porque no tiene sentido sólamente va a recibir un mensaje de preparación, el siguiente será de actualización por lo que no pasa a un tercer estado
 * cuando recibe manda una confirmación y ya
 * el siguiente Subscriber que actua es ActualizacionBD
 * 
enum EstadosPreparacionActualizacion
{ 
		esperando,
		preparadoParaActualizar
}
*/
public class PreparacionActualizacionListener implements MessageListener {
	private ArrayList<Tracker> trackers;
	private Tracker miTracker;
	private int IdMasBajo = 0, numeroDeMensajesRecibidos = 0;
	private ConfirmacionTopicPublisher confirmacionTopicPublisher;
	//public EstadosPreparacionActualizacion estadosPreparacionActualizacion;
	
	
	public PreparacionActualizacionListener(ArrayList<Tracker> trackers, Tracker miTracker) {
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
				PreparacionActualizacion preparacionActualizacion = (PreparacionActualizacion) objectMessage.getObject();
				
				System.out.println("     - Received "+ PreparacionActualizacion.class);
				
				//TODO enviar confirmación
				try {
				
					confirmacionTopicPublisher = new ConfirmacionTopicPublisher(trackers, miTracker);
					confirmacionTopicPublisher.start();
				
				}catch(Exception e) {
					
					System.out.print("Error publishing confirmation for Preparacion: ");
					e.printStackTrace();
					
				}
				/*
				switch (estadosPreparacionActualizacion) {
					
					case esperando:
						
						//TODO enviar confirmacion
						estadosPreparacionActualizacion = EstadosPreparacionActualizacion.preparadoParaActualizar;
					case preparadoParaActualizar:
						if(miTracker.getId() == IdMasBajo)
							miTracker.setMaster(true);
						//no hay else porque no hay reasignación hasta que no se va el master
						break;
					default:
						System.out.println("Error: Estado de Nuevo Master no contemplado");
				} 
*/
			} catch (Exception ex) {
				
				System.err.println("# TopicListener error: " + ex.getMessage());
				ex.printStackTrace();
				
			}
		}		
	}


	

}