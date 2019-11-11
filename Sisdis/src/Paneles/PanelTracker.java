package Paneles;
import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Controllers.DataController;
import Controllers.RedundantController;
import DesconexionTopicListeners.DesconexionTopicPublisher;
import DesconexionTopicListeners.DesconexionTopicSubscriber;
import KeepAliveTopicListeners.KeepaliveTopicPublisher;
import KeepAliveTopicListeners.KeepaliveTopicSubscriber;
import NuevoMasterSelectListener.NuevoMasterTopicPublisher;
import Objetos.Tracker;

public class PanelTracker extends JPanel implements Runnable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -412970576837431423L;

	public static Tracker miTracker;
	private static ArrayList<Tracker> TrackersRedundantes=new ArrayList<Tracker>();
	private static KeepaliveTopicPublisher KeepaliveTopicPublisher;
	private static KeepaliveTopicSubscriber KeepaliveTopicSubscriber;
	private static DesconexionTopicSubscriber DesconexionTopicSubscriber;
	private static DesconexionTopicPublisher DesconexionTopicPublisher;
	private static RedundantController RedundantController;
	
	
	
	
	
	
	
	/**
	 * Create the panel.
	 */
	public PanelTracker() {
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
		//Elegir el tracker de cada uno con su ip y demas
		Tracker miTracker = new Tracker(0,"192.168.5.46","30",false,System.currentTimeMillis());
		
		KeepaliveTopicSubscriber= new KeepaliveTopicSubscriber(getTrackersRedundantes(), miTracker);
		KeepaliveTopicPublisher = new KeepaliveTopicPublisher(TrackersRedundantes, miTracker);
		KeepaliveTopicSubscriber.start();
		KeepaliveTopicPublisher.start();
		
		
		DesconexionTopicPublisher = new DesconexionTopicPublisher(DataController.EstadosEleccionMaster.Esperando, miTracker);
		DesconexionTopicSubscriber = new DesconexionTopicSubscriber(TrackersRedundantes, DataController.EstadosEleccionMaster.Esperando, new NuevoMasterTopicPublisher(TrackersRedundantes, miTracker, DataController.EstadosEleccionMaster.Esperando));
//		DesconexionTopicPublisher.start();
//		KeepaliveTopicPublisher.start();
		
		setRedundantController(new RedundantController(TrackersRedundantes, KeepaliveTopicPublisher, KeepaliveTopicSubscriber,  DesconexionTopicPublisher, DesconexionTopicSubscriber, miTracker));
		RedundantController.start();
//		RedundantController.conectarJMS();
		System.out.println("CONECTADO");
		
		
		
		Thread thread = new Thread(){
		    public void run(){
		      System.out.println("Thread Running");
				int loop=0;
				
				while(loop<60) {
					System.out.println("TrackersRedundantes="+TrackersRedundantes);
					model.setRowCount(0);
					
					for(Tracker tracker:TrackersRedundantes) {
						model.addRow(new Object[] {tracker.getIP(),tracker.getPuerto(),tracker.isMaster()});
						System.out.println("Añadiendo="+tracker.getIP());
					}
					model.fireTableDataChanged();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					loop++;
				}
		    }
		  };

		  thread.start();
		
		

		
//		Tracker a = new Tracker(1,"192.168.1.2","65",true, System.currentTimeMillis());
//		Tracker b = new Tracker(2,"192.168.1.3","64",false,System.currentTimeMillis());
//		Tracker c = new Tracker(3,"192.168.1.4","63",false,System.currentTimeMillis());
//		Tracker d = new Tracker(4,"192.168.1.5","89",false,System.currentTimeMillis());
//		
//		model.addRow(new Object[] {a.getIP(),a.getPuerto(),a.isMaster()});
//		model.addRow(new Object[] {b.getIP(),b.getPuerto(),b.isMaster()});
//		model.addRow(new Object[] {c.getIP(),c.getPuerto(),c.isMaster()});
//		model.addRow(new Object[] {d.getIP(),d.getPuerto(),d.isMaster()});
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
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






}
