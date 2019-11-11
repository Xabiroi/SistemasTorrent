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
	private static LinkedList<Peer> PeersEnCola = new LinkedList<Peer>();
	private SQLiteDBManager manager = new SQLiteDBManager("bd/test.db");
	private static DataController DC;
	
	
	
	
	
	
	/**
	 * Create the panel.
	 */
	public PanelPeer() {
		this.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		this.add(scrollPane);
		
		//Ejemplo de como tendria que ser
		DefaultMutableTreeNode titulo = new DefaultMutableTreeNode("Swarms");
		//####################################
//		DefaultMutableTreeNode subtitulo1 = new DefaultMutableTreeNode("Swarm#ABCD1234");
//		DefaultMutableTreeNode subtitulo2 = new DefaultMutableTreeNode("Swarm#EFGH1234");
//		DefaultMutableTreeNode subtitulo3 = new DefaultMutableTreeNode("Swarm#IJKL1234");
		
//		Peer p1=new Peer("192.168.10.2","42","1");
//		Peer p2=new Peer("192.168.10.3","41","1");
//		Peer p3=new Peer("192.168.10.4","42","1");
//		Peer p4=new Peer("192.168.10.5","46","1");
//		Peer p5=new Peer("192.168.10.6","42","2");
//		Peer p6=new Peer("192.168.10.7","67","2");
//				
//		titulo.add(subtitulo1);
//		titulo.add(subtitulo2);
//		titulo.add(subtitulo3);
//		
//		subtitulo1.add(new DefaultMutableTreeNode("Peer		"+p1.getIP()+":"+p1.getPuerto()));
//		subtitulo2.add(new DefaultMutableTreeNode("Peer		"+p2.getIP()+":"+p2.getPuerto()));
//		subtitulo2.add(new DefaultMutableTreeNode("Peer		"+p3.getIP()+":"+p3.getPuerto()));
//		subtitulo2.add(new DefaultMutableTreeNode("Peer		"+p4.getIP()+":"+p4.getPuerto()));
//		subtitulo3.add(new DefaultMutableTreeNode("Peer		"+p5.getIP()+":"+p5.getPuerto()));
//		subtitulo3.add(new DefaultMutableTreeNode("Peer		"+p6.getIP()+":"+p6.getPuerto()));
//		subtitulo3.add(new DefaultMutableTreeNode("Peer		"+p1.getIP()+":"+p1.getPuerto()));
		//####################################
		
		JTree tree = new JTree(titulo);
		scrollPane.setViewportView(tree);
		
		
		//####################################
		
		ArrayList<Peer> a =new ArrayList<Peer>();
		ArrayList<Boolean> cambio = new ArrayList<Boolean>();
		cambio.add(new Boolean(false));
		a.add(new Peer("192.168.1.56","30","1"));
		a.add(new Peer("192.168.1.57","31","1"));
		a.add(new Peer("192.168.1.58","34","2"));
		Swarm s1=new Swarm(a,"2");
		Enjambres.add(s1);
		ContadorVersionBD.add(1);
		
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
		
		topicActualizarPublisher = new BDTopicPublisher(ContadorVersionBD,estadosBaseDeDatos,Enjambres,cambio,PeersEnCola);
		topicActualizarSubscriber = new BDTopicSubscriber(ContadorVersionBD, estadosBaseDeDatos,TrackersRedundantes);

		
		
		DC = new DataController(TrackersRedundantes, Enjambres, enviadorBD, recibidorBD, topicActualizarPublisher, topicActualizarSubscriber, estadosBaseDeDatos,cambio);

		topicActualizarSubscriber.start();
		topicActualizarPublisher.start();
		DC.start();
		//####################################
		

		
		//####################################
		Thread thread = new Thread(){
		    public void run(){
		    	int loop=0;
		    	while(loop<60) {
		    		ArrayList<Peer> auxPeer=manager.loadPeers();
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
		    		
		    		//
		    		loop++;
		    		
		    		try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    	}

		    	//
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




}
