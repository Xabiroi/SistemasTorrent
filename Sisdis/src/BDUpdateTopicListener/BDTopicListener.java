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

	private ArrayList<Integer> ContadorVersionBD;
	private ArrayList<EstadosBaseDeDatos> estadoActual;
	private ArrayList<Tracker> TrackersRedundantes;
	private ArrayList<String> ips;



	public BDTopicListener(ArrayList<Integer> contadorVersionBD, ArrayList<EstadosBaseDeDatos> estadoActual,
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
//				System.out.println("Estado actual del Listener="+estadoActual);
				switch(estadoActual.get(0)) {
				  case Esperando:
					  System.out.println("De aqui no sale");
					break;
				
				  case Sugerencia:
//					  System.out.println("Ha recibido la sugerencia!¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡");
					  	ObjectMessage objectMessage = (ObjectMessage) message;					
						SugerenciaActualizacion sugerenciaActualizacion = (SugerenciaActualizacion) objectMessage.getObject();
						
//						System.out.println("     - Detectado nuevo peer con IP: " + sugerenciaActualizacion.getIpPeer());
						ips.add(sugerenciaActualizacion.getIpPeer());
						
						if(ips.size()==TrackersRedundantes.size()){
							estadoActual.set(0,EstadosBaseDeDatos.Preparacion);
							ips=new ArrayList<String>();
						}
				    break;
				  case Preparacion:
					  	ObjectMessage objectMessage1 = (ObjectMessage) message;					
						PreparacionActualizacion preparacionActualizacion = (PreparacionActualizacion) objectMessage1.getObject();
						
//						System.out.println("     - Preparandose para actualizar");
						estadoActual.set(0,EstadosBaseDeDatos.Actualizacion);
				    break;
				  case Actualizacion:
					  	ObjectMessage objectMessage3 = (ObjectMessage) message;					
						ActualizacionBD actualizacionBD = (ActualizacionBD) objectMessage3.getObject();
						
//						System.out.println("     -Version de la base de datos: " + actualizacionBD.getIdentificador());
						
						ContadorVersionBD.set(0, ContadorVersionBD.get(0)+1);
						estadoActual.set(0,EstadosBaseDeDatos.Esperando);
						
					  break;
				  default:
					  break;
				}
				
				

			} catch (Exception ex) {
				System.err.println("# BD Listener TopicListener error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}		
	}


	public ArrayList<Integer> getContadorVersionBD() {
		return ContadorVersionBD;
	}



	public void setContadorVersionBD(ArrayList<Integer> contadorVersionBD) {
		ContadorVersionBD = contadorVersionBD;
	}



	public ArrayList<EstadosBaseDeDatos> getEstadoActual() {
		return estadoActual;
	}



	public void setEstadoActual(ArrayList<EstadosBaseDeDatos> estadoActual) {
		this.estadoActual = estadoActual;
	}



	public ArrayList<String> getIps() {
		return ips;
	}



	public void setIps(ArrayList<String> ips) {
		this.ips = ips;
	}



	public ArrayList<Tracker> getTrackersRedundantes() {
		return TrackersRedundantes;
	}


	public void setTrackersRedundantes(ArrayList<Tracker> trackersRedundantes) {
		TrackersRedundantes = trackersRedundantes;
	}


}