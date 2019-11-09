package BDUpdateTopicListener;

import java.util.ArrayList;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import Controllers.DataController.EstadosBaseDeDatos;
import Mensajes.ActualizacionBD;
import Mensajes.PreparacionActualizacion;
import Mensajes.SugerenciaActualizacion;
import Objetos.Tracker;




public class BDTopicListener implements MessageListener {

	private int ContadorVersionBD;
	private EstadosBaseDeDatos estadoActual;
	private ArrayList<Tracker> TrackersRedundantes;
	private ArrayList<String> ips;



	public BDTopicListener(int contadorVersionBD, EstadosBaseDeDatos estadoActual,
			ArrayList<Tracker> trackersRedundantes) {
		super();
		ContadorVersionBD = contadorVersionBD;
		this.estadoActual = estadoActual;
		TrackersRedundantes = trackersRedundantes;
		this.ips = new ArrayList<String>();

	}



	@Override
	//This method is call when a new Message arrives to the topic
	public void onMessage(Message message) {		
		if (message != null) {
			try {
				switch(estadoActual) {
				  case Esperando:
					break;
				
				  case Sugerencia:
					  	ObjectMessage objectMessage = (ObjectMessage) message;					
						SugerenciaActualizacion sugerenciaActualizacion = (SugerenciaActualizacion) objectMessage.getObject();
						
						System.out.println("     - Detectado nuevo peer con IP: " + sugerenciaActualizacion.getIpPeer());
						ips.add(sugerenciaActualizacion.getIpPeer());
						
						if(ips.size()==TrackersRedundantes.size()){
							setEstadoActual(EstadosBaseDeDatos.Preparacion);
							ips=new ArrayList<String>();
						}
				    break;
				  case Preparacion:
					  	ObjectMessage objectMessage1 = (ObjectMessage) message;					
						PreparacionActualizacion preparacionActualizacion = (PreparacionActualizacion) objectMessage1.getObject();
						
						System.out.println("     - Preparandose para actualizar");
						setEstadoActual(EstadosBaseDeDatos.Actualizacion);
				    break;
				  case Actualizacion:
					  	ObjectMessage objectMessage3 = (ObjectMessage) message;					
						ActualizacionBD actualizacionBD = (ActualizacionBD) objectMessage3.getObject();
						
						System.out.println("     -Version de la base de datos: " + actualizacionBD.getIdentificador());
						setContadorVersionBD(ContadorVersionBD+1);
						setEstadoActual(EstadosBaseDeDatos.Sugerencia);
						
					  break;
				  default:
					  break;
				}
				
				

			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}		
	}


	public int getContadorVersionBD() {
		return ContadorVersionBD;
	}


	public void setContadorVersionBD(int contadorVersionBD) {
		ContadorVersionBD = contadorVersionBD;
	}


	public EstadosBaseDeDatos getEstadoActual() {
		return estadoActual;
	}


	public void setEstadoActual(EstadosBaseDeDatos estadoActual) {
		this.estadoActual = estadoActual;
	}
	public ArrayList<Tracker> getTrackersRedundantes() {
		return TrackersRedundantes;
	}


	public void setTrackersRedundantes(ArrayList<Tracker> trackersRedundantes) {
		TrackersRedundantes = trackersRedundantes;
	}


}