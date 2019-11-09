package Controllers;
import java.util.ArrayList;
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
	private static ArrayList<EstadosBaseDeDatos> estadoActual = new ArrayList<EstadosBaseDeDatos>();
	private ArrayList<Boolean> cambio=new ArrayList<Boolean>();//CAMBIAR EL TIPO PRIMITIVO POR UNO COMPLEJO, ASI FUNCIONA


	private static int ContadorVersionBD=0;
	private static LinkedList<Peer> PeersEnCola = new LinkedList<Peer>();

	public DataController(ArrayList<Tracker> trackersRedundantes, ArrayList<Swarm> enjambres,
			QueueFileSender enviadorBD, QueueFileReceiver recibidorBD, BDTopicPublisher topicActualizarPublisher,
			BDTopicSubscriber topicActualizarSubscriber, ArrayList<EstadosBaseDeDatos> estadoActual, ArrayList<Boolean> cambio) {
		super();
		TrackersRedundantes = trackersRedundantes;
		Enjambres = enjambres;
		DataController.enviadorBD = enviadorBD;
		DataController.recibidorBD = recibidorBD;
		DataController.topicActualizarPublisher = topicActualizarPublisher;
		DataController.topicActualizarSubscriber = topicActualizarSubscriber;
		DataController.estadoActual = estadoActual;
		this.cambio = cambio;
	}

	//Esperar un rato y comprobar si ha habido cambios en ese instante
	public void comprobar() {
		System.out.println("Enjambres=="+Enjambres);
		if(!PeersEnCola.isEmpty()) {
			System.out.println("estadoActual="+estadoActual.get(0));
			cambio.set(0, true);//TODO

			if(estadoActual.get(0)==EstadosBaseDeDatos.Actualizacion) {
				Peer aux = PeersEnCola.poll();
				boolean swarmDisponible=false;
				for(Swarm swarm:Enjambres) {
					//FIXME que los peers con mismo id se agrupen en el mismo enjambre
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


	public static void main(String args[]) {
	
		ArrayList<Peer> a =new ArrayList<Peer>();
		ArrayList<Boolean> cambio = new ArrayList<Boolean>();
		cambio.add(new Boolean(false));
		a.add(new Peer("192.168.1.56","30","1"));
		a.add(new Peer("192.168.1.57","31","1"));
		a.add(new Peer("192.168.1.58","34","2"));
		Swarm s1=new Swarm(a);
		Enjambres.add(s1);
		ContadorVersionBD=1;
		
		Tracker t1=new Tracker(1,"192.168.2.1","49",true,System.currentTimeMillis());
		Tracker t2=new Tracker(2,"192.168.2.2","44",true,System.currentTimeMillis());
		Tracker t3=new Tracker(3,"192.168.2.3","42",true,System.currentTimeMillis());
		TrackersRedundantes.add(t1);
		TrackersRedundantes.add(t2);
		TrackersRedundantes.add(t3);
		
		QueueFileSender enviadorBD=new QueueFileSender();
		QueueFileReceiver recibidorBD= new QueueFileReceiver();
		
		ArrayList<EstadosBaseDeDatos> estadosBaseDeDatos= new ArrayList<EstadosBaseDeDatos>();
		estadosBaseDeDatos.add(0, EstadosBaseDeDatos.Esperando);
		
		BDTopicPublisher bDTopicPublisher = new BDTopicPublisher(ContadorVersionBD,estadosBaseDeDatos,Enjambres,cambio);
		BDTopicSubscriber bDTopicSubscriber = new BDTopicSubscriber(ContadorVersionBD, estadosBaseDeDatos,TrackersRedundantes);

		
		
		DataController datacontroller = new DataController(TrackersRedundantes, Enjambres, enviadorBD, recibidorBD, bDTopicPublisher, bDTopicSubscriber, estadosBaseDeDatos,cambio);

		topicActualizarSubscriber.start();
		topicActualizarPublisher.start();
		datacontroller.start();
		
		//Esperar 2 segundos y meter un nuevo peer
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		PeersEnCola.offer(new Peer("192.168.1.59","34","2"));
		
		//Esperar 2 segundos y meter un nuevo peer
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		System.out.println("OOOOOOOOOOOOOOOOOOOOOOOO");
		PeersEnCola.offer(new Peer("192.168.1.60","14","2"));
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		cambio.set(0, true);

		
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


	public ArrayList<Boolean> getCambio() {
		return cambio;
	}

	public void setCambio(ArrayList<Boolean> cambio) {
		this.cambio = cambio;
	}
	


}

