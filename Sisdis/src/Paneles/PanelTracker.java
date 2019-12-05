package Paneles;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import BDFileQueueListener.QueueFileReceiver;
import BDFileQueueListener.QueueFileSender;
import Controllers.DataController.EstadosEleccionMaster;
import Controllers.RedundantController;
import KeepAliveTopicListeners.KeepaliveTopicPublisher;
import KeepAliveTopicListeners.KeepaliveTopicSubscriber;
import NuevoMasterSelectListener.NuevoMasterTopicPublisher;
import NuevoMasterSelectListener.NuevoMasterTopicSubscriber;
import Objetos.Tracker;

public class PanelTracker extends JPanel{


	/**
	 * 
	 */
	private static final long serialVersionUID = -412970576837431423L;

	public static Tracker miTracker;
	private static ArrayList<Tracker> TrackersRedundantes=new ArrayList<Tracker>();
	private static KeepaliveTopicPublisher KeepaliveTopicPublisher;
	private static KeepaliveTopicSubscriber KeepaliveTopicSubscriber;
	private static RedundantController RedundantController;
	private static NuevoMasterTopicPublisher NuevoMasterTopicPublisher;
	private static NuevoMasterTopicSubscriber NuevoMasterTopicSubscriber;
	private static ArrayList<EstadosEleccionMaster> estadosEleccionMasters = new ArrayList<EstadosEleccionMaster>(1);
	private static QueueFileSender enviadorBD;
	private static QueueFileReceiver recibidorBD;
	private ArrayList<Boolean> desconexion=new ArrayList<Boolean>(1);
	
	/**
	 * Create the panel.
	 */
	public PanelTracker(ArrayList<Boolean> desconexion) {
		
		setDesconexion(desconexion);
		
		ArrayList<Boolean> cambio = new ArrayList<Boolean>();
		cambio.add(0, false);
		ArrayList<Boolean> cambio2 = new ArrayList<Boolean>();
		cambio2.add(0, false);
		estadosEleccionMasters.add(EstadosEleccionMaster.Esperando);
		this.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		this.add(scrollPane_1);
		
		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);

		//Valores de prueba, crear JTable personalizada con diferentes metodos
		model.addColumn("Ip");
		model.addColumn("Puerto");
		model.addColumn("Master");
		scrollPane_1.setViewportView(table);
		//FIXME Elegir el tracker de cada uno con su ip y demas
		Tracker miTracker = new Tracker(0,"192.168.5.46","30",false,System.currentTimeMillis());
		
		enviadorBD=new QueueFileSender();
		recibidorBD= new QueueFileReceiver();
		
		NuevoMasterTopicPublisher = new NuevoMasterTopicPublisher(TrackersRedundantes, miTracker, estadosEleccionMasters, cambio2);
		NuevoMasterTopicSubscriber = new NuevoMasterTopicSubscriber(TrackersRedundantes, miTracker, estadosEleccionMasters,desconexion);
		KeepaliveTopicSubscriber= new KeepaliveTopicSubscriber(getTrackersRedundantes(), miTracker,enviadorBD,recibidorBD,desconexion);
		KeepaliveTopicPublisher = new KeepaliveTopicPublisher(TrackersRedundantes, miTracker,desconexion);
		KeepaliveTopicSubscriber.start();
		KeepaliveTopicPublisher.start();
//		NuevoMasterTopicPublisher.start();
		NuevoMasterTopicSubscriber.start();
		
		recibidorBD.start();
		setRedundantController(new RedundantController(TrackersRedundantes, KeepaliveTopicPublisher, KeepaliveTopicSubscriber,miTracker,NuevoMasterTopicPublisher,desconexion));
		RedundantController.start();
		
//		RedundantController.conectarJMS();
		System.out.println("CONECTADO");
			
		Thread thread = new Thread(){
		    public void run(){

		    	System.out.println("Thread Running");
				
				while(desconexion.get(0)) {
			    	synchronized(TrackersRedundantes) {
					//System.out.println("TrackersRedundantes="+TrackersRedundantes);
					model.setRowCount(0);
					//Iterator
					for(Iterator<Tracker> iterator = TrackersRedundantes.iterator(); iterator.hasNext();) {
						Tracker tracker =iterator.next();
						if(tracker.getId()!=0) {
						model.addRow(new Object[] {tracker.getIP(),tracker.getId(),tracker.isMaster()});
						System.out.println("Añadiendo en la interfaz="+tracker.getIP());
						}
					}
					model.fireTableDataChanged();
			    	}
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
		    	}
		    }
		  };

		  thread.start();
		

		
	}



	public static ArrayList<Tracker> getTrackersRedundantes() {
		return TrackersRedundantes;
	}

	public void setTrackersRedundantes(ArrayList<Tracker> trackersRedundantes) {
		TrackersRedundantes = trackersRedundantes;
	}


	public static RedundantController getRedundantController() {
		return RedundantController;
	}

	public static void setRedundantController(RedundantController redundantController) {
		RedundantController = redundantController;
	}



	public static QueueFileSender getEnviadorBD() {
		return enviadorBD;
	}



	public static void setEnviadorBD(QueueFileSender enviadorBD) {
		PanelTracker.enviadorBD = enviadorBD;
	}



	public static QueueFileReceiver getRecibidorBD() {
		return recibidorBD;
	}



	public static void setRecibidorBD(QueueFileReceiver recibidorBD) {
		PanelTracker.recibidorBD = recibidorBD;
	}



	public ArrayList<Boolean> getDesconexion() {
		return desconexion;
	}



	public void setDesconexion(ArrayList<Boolean> desconexion) {
		this.desconexion = desconexion;
	}






}
