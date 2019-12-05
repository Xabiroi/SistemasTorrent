package Controllers;
import java.util.ArrayList;
import java.util.LinkedList;
import BDFileQueueListener.QueueFileReceiver;
import BDFileQueueListener.QueueFileSender;
import BDUpdateTopicListener.BDTopicPublisher;
import BDUpdateTopicListener.BDTopicSubscriber;
import Mensajes.Desconexion;
import Objetos.Peer;
import Objetos.Swarm;
import Objetos.Tracker;
import Sqlite.SQLiteDBManager;

public class DataController extends Thread{
	
	public enum EstadosBaseDeDatos
	{ 
		Esperando,
		Sugerencia,
		Preparacion,
		Actualizacion
	}
	public enum EstadosEleccionMaster{
		Esperando,
		Decidiendo
	}
	
	private static ArrayList<Tracker> TrackersRedundantes=new ArrayList<Tracker>();
	private static ArrayList<Swarm> Enjambres=new ArrayList<Swarm>();
	private static QueueFileSender enviadorBD;
	private static QueueFileReceiver recibidorBD;
	private static BDTopicPublisher topicActualizarPublisher;
	private static BDTopicSubscriber topicActualizarSubscriber;
	private static ArrayList<EstadosBaseDeDatos> estadoActual = new ArrayList<EstadosBaseDeDatos>(1);
	private ArrayList<Boolean> cambio=new ArrayList<Boolean>(1);
	private static ArrayList<Integer> ContadorVersionBD=new ArrayList<Integer>(1);
	private static LinkedList<Peer> PeersEnCola = new LinkedList<Peer>();
	private ArrayList<Boolean> desconexion=new ArrayList<Boolean>(1);
	private SQLiteDBManager manager = new SQLiteDBManager("bd/test.db");

	public DataController(ArrayList<Tracker> trackersRedundantes, ArrayList<Swarm> enjambres,
			QueueFileSender enviadorBD, QueueFileReceiver recibidorBD, BDTopicPublisher topicActualizarPublisher,
			BDTopicSubscriber topicActualizarSubscriber, ArrayList<EstadosBaseDeDatos> estadoActual, ArrayList<Boolean> cambio,ArrayList<Boolean> desconexion) {
		super();
		TrackersRedundantes = trackersRedundantes;
		Enjambres = enjambres;
		DataController.enviadorBD = enviadorBD;
		DataController.recibidorBD = recibidorBD;
		DataController.topicActualizarPublisher = topicActualizarPublisher;
		DataController.topicActualizarSubscriber = topicActualizarSubscriber;
		DataController.estadoActual = estadoActual;
		this.cambio = cambio;
		this.setDesconexion(desconexion);
	}

	//Esperar un rato y comprobar si ha habido cambios en ese instante
	public void comprobar() {
		System.out.println("Enjambres=="+Enjambres);
		if(!PeersEnCola.isEmpty()) {
			System.out.println("estadoActual="+estadoActual.get(0));
			cambio.set(0, true);

			if(estadoActual.get(0)==EstadosBaseDeDatos.Actualizacion) {
				Peer aux = PeersEnCola.poll();
				boolean swarmDisponible=false;
				for(Swarm swarm:Enjambres) {

					if(aux.getIdentificadorSwarm().equals(swarm.getIdentificadorSwarm())) {
						swarm.getListaPeers().add(aux);
						manager.insertPeer(aux.getIP(), aux.getPuerto(),aux.getIdentificadorSwarm());
						//TODO habria que meter los hashes de archivos aqui en vez de las ids
						manager.insertSwarmPeer(swarm.getIdentificadorSwarm(),aux.getIdentificadorSwarm(),0);
						swarmDisponible=true;
					}

				}
				//Enjambres.add(PeersEnCola.poll());
				if(!swarmDisponible) {
					System.out.println("Entras en nuevo swarm");
					ArrayList<Peer> ListaPeers= new ArrayList<Peer>();
					ListaPeers.add(aux);
					Enjambres.add(new Swarm(ListaPeers,aux.getIdentificadorSwarm()));	
					
					manager.insertPeer(aux.getIP(), aux.getPuerto(),aux.getIdentificadorSwarm());
					//TODO habria que meter los hashes de archivos aqui en vez de las ids
					manager.insertSwarm(aux.getIdentificadorSwarm());
					manager.insertSwarmPeer(aux.getIdentificadorSwarm(),aux.getIdentificadorSwarm(),0);
				}
			}
		}
		else {System.out.println("HA LLEGADO AQUI Y HA CAMBIADO A FALSO");
			cambio.set(0, false);}
	}
	
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


	public ArrayList<Swarm> getEnjambres() {
		return Enjambres;
	}


	public void setEnjambres(ArrayList<Swarm> enjambres) {
		Enjambres = enjambres;
	}

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



	public ArrayList<Tracker> getTrackersRedundantes() {
		return TrackersRedundantes;
	}


	public static ArrayList<EstadosBaseDeDatos> getEstadoActual() {
		return estadoActual;
	}

	public static void setEstadoActual(ArrayList<EstadosBaseDeDatos> estadoActual) {
		DataController.estadoActual = estadoActual;
	}

	public void setTrackersRedundantes(ArrayList<Tracker> trackersRedundantes) {
		TrackersRedundantes = trackersRedundantes;
	}


	public static ArrayList<Integer> getContadorVersionBD() {
		return ContadorVersionBD;
	}

	public static void setContadorVersionBD(ArrayList<Integer> contadorVersionBD) {
		ContadorVersionBD = contadorVersionBD;
	}

	public static LinkedList<Peer> getPeersEnCola() {
		return PeersEnCola;
	}

	public static void setPeersEnCola(LinkedList<Peer> peersEnCola) {
		PeersEnCola = peersEnCola;
	}


	public ArrayList<Boolean> getCambio() {
		return cambio;
	}

	public void setCambio(ArrayList<Boolean> cambio) {
		this.cambio = cambio;
	}

	public SQLiteDBManager getManager() {
		return manager;
	}

	public void setManager(SQLiteDBManager manager) {
		this.manager = manager;
	}

	public ArrayList<Boolean> getDesconexion() {
		return desconexion;
	}

	public void setDesconexion(ArrayList<Boolean> desconexion) {
		this.desconexion = desconexion;
	}
	


}

