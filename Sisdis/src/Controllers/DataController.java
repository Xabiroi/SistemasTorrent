package Controllers;
import java.util.ArrayList;

import BDFileQueueListener.QueueFileReceiver;
import BDFileQueueListener.QueueFileSender;
import BDUpdateTopicListener.BDTopicPublisher;
import BDUpdateTopicListener.BDTopicSubscriber;
import Objetos.Swarm;

public class DataController {
	
	
	private ArrayList<Swarm> Enjambres;
	private QueueFileSender enviadorBD;
	private QueueFileReceiver recibidorBD;
	private BDTopicPublisher topicActualizarPublisher;
	private BDTopicSubscriber topicActualizarSubscriber;
	private String estadoActual;
	
	
	
	public DataController(ArrayList<Swarm> enjambres, BDTopicPublisher topicActualizarPublisher,
			BDTopicSubscriber topicActualizarSubscriber, String estadoActual) {
		super();
		Enjambres = enjambres;
		this.enviadorBD = new QueueFileSender();
		this.recibidorBD = new QueueFileReceiver();
		this.topicActualizarPublisher = topicActualizarPublisher;
		this.topicActualizarSubscriber = topicActualizarSubscriber;
		this.estadoActual = estadoActual;
	}


	
	
	
	public static void main(String args[]) {
		
		ArrayList<Swarm> arr=new ArrayList<Swarm>();
		Swarm s1=new Swarm();
		arr.add(s1);
		
		String estadoactual = "normal";
				
		BDTopicPublisher bDTopicPublisher = new BDTopicPublisher(estadoactual);
		
		BDTopicSubscriber bDTopicSubscriber = new BDTopicSubscriber(0, estadoactual);
		
		
		
		DataController datacontroller = new DataController(arr, bDTopicPublisher, bDTopicSubscriber, estadoactual);
		
		
		//TODO ejecutar los hilos para probar el funcionamiento con unos mensajes hardcodeados
		
	}
	
	
	
	public ArrayList<Swarm> getEnjambres() {
		return Enjambres;
	}


	public void setEnjambres(ArrayList<Swarm> enjambres) {
		Enjambres = enjambres;
	}

	public void insertarPeer() {}
	public void actualizarPeer() {}
	public void borrarPeer() {}
	public void obtenerPeers() {}
	
	public void insertarSwarm() {}
	public void actualizarSwarm() {}
	public void borrarSwarm() {}
	public void obtenerSwarms() {}

	public void insertarPeerSwarmRelation() {}
	public void actualizarPeerSwarmRelation() {}
	public void borrarPeerSwarmRelation() {}
	public void obtenerPeerSwarmRelation() {}


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




	public BDTopicPublisher getTopicActualizarPublisher() {
		return topicActualizarPublisher;
	}




	public void setTopicActualizarPublisher(BDTopicPublisher topicActualizarPublisher) {
		this.topicActualizarPublisher = topicActualizarPublisher;
	}




	public BDTopicSubscriber getTopicActualizarSubscriber() {
		return topicActualizarSubscriber;
	}




	public void setTopicActualizarSubscriber(BDTopicSubscriber topicActualizarSubscriber) {
		this.topicActualizarSubscriber = topicActualizarSubscriber;
	}




	public String getEstadoActual() {
		return estadoActual;
	}




	public void setEstadoActual(String estadoActual) {
		this.estadoActual = estadoActual;
	}
	
	
	


}

