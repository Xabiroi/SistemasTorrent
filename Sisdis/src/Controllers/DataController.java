package Controllers;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import BDFileQueueListener.QueueFileReceiver;
import BDFileQueueListener.QueueFileSender;
import BDUpdateTopicListener.BDTopicPublisher;
import BDUpdateTopicListener.BDTopicSubscriber;
import Objetos.Peer;
import Objetos.Swarm;
import Objetos.Tracker;

public class DataController extends Thread{
	
	public enum EstadosBaseDeDatos
	{ 
		Esperando,
		Sugerencia,
		Preparacion,
		Actualizacion
	}
	
	private static ArrayList<Tracker> TrackersRedundantes=new ArrayList<Tracker>();
	private static ArrayList<Swarm> Enjambres=new ArrayList<Swarm>();
	private static QueueFileSender enviadorBD;
	private static QueueFileReceiver recibidorBD;
	private static BDTopicPublisher topicActualizarPublisher;
	private static BDTopicSubscriber topicActualizarSubscriber;
	private static EstadosBaseDeDatos estadoActual;
	private static boolean cambio;
	private static int ContadorVersionBD;
	private static LinkedList<Peer> PeersEnCola = new LinkedList<Peer>();

	public DataController(ArrayList<Tracker> trackersRedundantes, ArrayList<Swarm> enjambres,
			QueueFileSender enviadorBD, QueueFileReceiver recibidorBD, BDTopicPublisher topicActualizarPublisher,
			BDTopicSubscriber topicActualizarSubscriber, EstadosBaseDeDatos estadoActual, boolean cambio) {
		super();
		TrackersRedundantes = trackersRedundantes;
		Enjambres = enjambres;
		DataController.enviadorBD = enviadorBD;
		DataController.recibidorBD = recibidorBD;
		DataController.topicActualizarPublisher = topicActualizarPublisher;
		DataController.topicActualizarSubscriber = topicActualizarSubscriber;
		DataController.estadoActual = estadoActual;
		DataController.cambio = cambio;
	}

	//Esperar un rato y comprobar si ha habido cambios en ese instante
	public void comprobar() {
		System.out.println("Cambio="+cambio);
		System.out.println("!PeersEnCola.isEmpty()="+!PeersEnCola.isEmpty());
		
		if(!PeersEnCola.isEmpty()) {
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$");
			System.out.println("estadoActual="+estadoActual);
			cambio=true;
			if(estadoActual==EstadosBaseDeDatos.Actualizacion) {
				System.out.println("Entra");
				Peer aux = PeersEnCola.poll();
				boolean swarmDisponible=false;
				for(Swarm swarm:Enjambres) {
					if(aux.getIdentificadorSwarm().equals(swarm.getIdentificadorSwarm())) {
						swarm.getListaPeers().add(aux);
						swarmDisponible=true;
					}

				}
				//Enjambres.add(PeersEnCola.poll());
				if(!swarmDisponible) {
					ArrayList<Peer> ListaPeers= new ArrayList<Peer>();
					ListaPeers.add(aux);
					Enjambres.add(new Swarm(ListaPeers));			
				}
			}
		}
		else {cambio=false;}
	}
	//FIXME
	public void run() {
		int loop=0;
		while(loop<6000) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		System.out.println("Comprobando...");
		comprobar();
		loop++;
		}
	}


	public static void main(String args[]) {
	
		ArrayList<Peer> a =new ArrayList<Peer>();
		cambio=false;
		a.add(new Peer("192.168.1.56","30","1"));
		a.add(new Peer("192.168.1.57","31","1"));
		a.add(new Peer("192.168.1.58","34","2"));
		Swarm s1=new Swarm(a);
		Enjambres.add(s1);
		ContadorVersionBD=1;
		enviadorBD=new QueueFileSender();
		recibidorBD= new QueueFileReceiver();
		estadoActual=EstadosBaseDeDatos.Esperando;
		topicActualizarPublisher = new BDTopicPublisher(ContadorVersionBD,estadoActual,Enjambres,cambio);
		topicActualizarSubscriber = new BDTopicSubscriber(ContadorVersionBD, estadoActual,TrackersRedundantes);

		
		
		DataController datacontroller = new DataController(TrackersRedundantes, Enjambres, enviadorBD, recibidorBD, topicActualizarPublisher, topicActualizarSubscriber, estadoActual,cambio);
		
		topicActualizarSubscriber.start();
		topicActualizarPublisher.start();
		datacontroller.start();
		
		//Esperar 2 segundos y meter un nuevo peer
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$");
		PeersEnCola.offer(new Peer("192.168.1.59","34","2"));
		
		//Esperar 2 segundos y meter un nuevo peer
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$");
		PeersEnCola.offer(new Peer("192.168.1.60","14","3"));
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		cambio=true;
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
		DataController.enviadorBD = enviadorBD;
	}


	public QueueFileReceiver getRecibidorBD() {
		return recibidorBD;
	}


	public void setRecibidorBD(QueueFileReceiver recibidorBD) {
		DataController.recibidorBD = recibidorBD;
	}




	public BDTopicPublisher getTopicActualizarPublisher() {
		return topicActualizarPublisher;
	}




	public void setTopicActualizarPublisher(BDTopicPublisher topicActualizarPublisher) {
		DataController.topicActualizarPublisher = topicActualizarPublisher;
	}




	public BDTopicSubscriber getTopicActualizarSubscriber() {
		return topicActualizarSubscriber;
	}




	public void setTopicActualizarSubscriber(BDTopicSubscriber topicActualizarSubscriber) {
		DataController.topicActualizarSubscriber = topicActualizarSubscriber;
	}





	public EstadosBaseDeDatos getEstadoActual() {
		return estadoActual;
	}





	public void setEstadoActual(EstadosBaseDeDatos estadoActual) {
		DataController.estadoActual = estadoActual;
	}





	public ArrayList<Tracker> getTrackersRedundantes() {
		return TrackersRedundantes;
	}





	public void setTrackersRedundantes(ArrayList<Tracker> trackersRedundantes) {
		TrackersRedundantes = trackersRedundantes;
	}





	public boolean isCambio() {
		return cambio;
	}





	public void setCambio(boolean cambio) {
		DataController.cambio = cambio;
	}





	public static int getContadorVersionBD() {
		return ContadorVersionBD;
	}





	public static void setContadorVersionBD(int contadorVersionBD) {
		ContadorVersionBD = contadorVersionBD;
	}


	public static LinkedList<Peer> getPeersEnCola() {
		return PeersEnCola;
	}

	public static void setPeersEnCola(LinkedList<Peer> peersEnCola) {
		PeersEnCola = peersEnCola;
	}



	
	


}

