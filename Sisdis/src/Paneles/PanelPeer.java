package Paneles;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import BDFileQueueListener.QueueFileReceiver;
import BDFileQueueListener.QueueFileSender;
import BDUpdateTopicListener.BDTopicPublisher;
import BDUpdateTopicListener.BDTopicSubscriber;
import Controllers.DataController;
import Controllers.DataController.EstadosBaseDeDatos;
import Objetos.Peer;
import Objetos.Swarm;
import Objetos.Tracker;
import Sqlite.SQLiteDBManager;

public class PanelPeer extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static ArrayList<Tracker> TrackersRedundantes=new ArrayList<Tracker>();
	private static ArrayList<Swarm> Enjambres=new ArrayList<Swarm>();
	private static QueueFileSender enviadorBD;
	private static QueueFileReceiver recibidorBD;
	private static BDTopicPublisher topicActualizarPublisher;
	private static BDTopicSubscriber topicActualizarSubscriber;
	private static ArrayList<EstadosBaseDeDatos> estadoActual = new ArrayList<EstadosBaseDeDatos>(1);
	private ArrayList<Boolean> cambio=new ArrayList<Boolean>(1);
	private static ArrayList<Integer> ContadorVersionBD=new ArrayList<Integer>(1);
	private LinkedList<Peer> PeersEnCola;
	private SQLiteDBManager manager = new SQLiteDBManager("bd/test.db");//FIXME cambio al nombre con timestamp o algo asi
	private static DataController DC;
	private ArrayList<Boolean> desconexion=new ArrayList<Boolean>(1);
	

	


	/**
	 * Create the panel.
	 */
	public PanelPeer(ArrayList<Boolean> desconexion,ArrayList<Tracker> trackersRedundantes,LinkedList<Peer> peersEnCola) {

		setTrackersRedundantes(trackersRedundantes);
		setDesconexion(desconexion);
		this.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		this.add(scrollPane);

		DefaultMutableTreeNode titulo = new DefaultMutableTreeNode("Swarms");
		//####################################
		JTree tree = new JTree(titulo);
		scrollPane.setViewportView(tree);
		//####################################
		
//		ArrayList<Peer> a =new ArrayList<Peer>();
		ArrayList<Boolean> cambio = new ArrayList<Boolean>();
		cambio.add(new Boolean(false));
//		a.add(new Peer("192.168.1.56",30,"1"));
//		a.add(new Peer("192.168.1.57",31,"1"));
//		a.add(new Peer("192.168.1.58",34,"2"));
//		Swarm s1=new Swarm(a,"2");
//		Enjambres.add(s1);
		
		SQLiteDBManager.deleteSwarmPeers();
		SQLiteDBManager.deletePeers();
		SQLiteDBManager.deleteSwarms();
		
		ContadorVersionBD.add(1);
		
//		Tracker t1=new Tracker(1,"192.168.2.1","49",true,System.currentTimeMillis());
//		Tracker t2=new Tracker(2,"192.168.2.2","44",true,System.currentTimeMillis());
//		Tracker t3=new Tracker(3,"192.168.2.3","42",true,System.currentTimeMillis());
//		TrackersRedundantes.add(t1);
//		TrackersRedundantes.add(t2);
//		TrackersRedundantes.add(t3);
		
		QueueFileSender enviadorBD=new QueueFileSender();
		QueueFileReceiver recibidorBD= new QueueFileReceiver();
		
		ArrayList<EstadosBaseDeDatos> estadosBaseDeDatos= new ArrayList<EstadosBaseDeDatos>();
		estadosBaseDeDatos.add(0, EstadosBaseDeDatos.Esperando);
		
		topicActualizarPublisher = new BDTopicPublisher(ContadorVersionBD,estadosBaseDeDatos,Enjambres,cambio,peersEnCola,desconexion);
		topicActualizarSubscriber = new BDTopicSubscriber(ContadorVersionBD, estadosBaseDeDatos,TrackersRedundantes,desconexion);

		//Borrado de la base de datos al iniciar

		
		
		DC = new DataController(TrackersRedundantes, Enjambres, enviadorBD, recibidorBD, topicActualizarPublisher, topicActualizarSubscriber, estadosBaseDeDatos,cambio,desconexion,peersEnCola);

		topicActualizarSubscriber.start();
		topicActualizarPublisher.start();
		DC.start();
		//####################################
		

		
		//####################################
		Thread thread = new Thread(){
		    public void run(){
		    	while(desconexion.get(0)) {
		    		ArrayList<Peer> auxPeer=SQLiteDBManager.loadPeers();
		    		ArrayList<Swarm> auxSwarm=manager.loadSwarms();
		    		
		    		for(Swarm swarm:auxSwarm) {
		    			for(Peer peer:auxPeer) {
			    			if(swarm.getIdentificadorSwarm().equals(peer.getIdentificadorSwarm())) {
			    				swarm.getListaPeers().add(peer);
			    				
			    			}
		    			}
		    		}
		    		
		    		//Actualizar nodos
		    		titulo.removeAllChildren();
		    		for(Swarm swarm:auxSwarm) {
		    			DefaultMutableTreeNode subtitulo = new DefaultMutableTreeNode(swarm.getIdentificadorSwarm());
		    			titulo.add(subtitulo);
		    			for(Peer peer:swarm.getListaPeers()) {

		    				subtitulo.add(new DefaultMutableTreeNode("Peer="+peer.getIP()+":"+peer.getPuerto()));
		    				
		    			}
		    			
		    			
		    		}
		    		
		    		tree.repaint();
		    		
		    		
		    		try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    	}
		    }
		  };
		  thread.start();
		//####################################
	
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

	public static ArrayList<EstadosBaseDeDatos> getEstadoActual() {
		return estadoActual;
	}

	public static void setEstadoActual(ArrayList<EstadosBaseDeDatos> estadoActual) {
		PanelPeer.estadoActual = estadoActual;
	}

	public ArrayList<Boolean> getCambio() {
		return cambio;
	}

	public void setCambio(ArrayList<Boolean> cambio) {
		this.cambio = cambio;
	}

	public static QueueFileReceiver getRecibidorBD() {
		return recibidorBD;
	}

	public static void setRecibidorBD(QueueFileReceiver recibidorBD) {
		PanelPeer.recibidorBD = recibidorBD;
	}

	public static QueueFileSender getEnviadorBD() {
		return enviadorBD;
	}

	public static void setEnviadorBD(QueueFileSender enviadorBD) {
		PanelPeer.enviadorBD = enviadorBD;
	}
	
	public static ArrayList<Tracker> getTrackersRedundantes() {
		return TrackersRedundantes;
	}

	public static void setTrackersRedundantes(ArrayList<Tracker> trackersRedundantes) {
		TrackersRedundantes = trackersRedundantes;
	}

	public LinkedList<Peer> getPeersEnCola() {
		return PeersEnCola;
	}

	public void setPeersEnCola(LinkedList<Peer> peersEnCola) {
		PeersEnCola = peersEnCola;
	}
	

}
