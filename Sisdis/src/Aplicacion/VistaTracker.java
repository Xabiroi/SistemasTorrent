package Aplicacion;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import Paneles.PanelConfiguracion;
import Paneles.PanelPeer;
import Paneles.PanelTracker;
import javax.swing.JButton;
import java.awt.BorderLayout;
import Objetos.Peer;
import Objetos.Tracker;

import javax.swing.JPanel;

public class VistaTracker {

	private JFrame frame;
	private PanelPeer PanelPeer;
	private PanelTracker PanelTracker;
	private PanelConfiguracion PanelConfiguracion;
	private JPanel panel;
	private ArrayList<Boolean> desconexion=new ArrayList<Boolean>(1);
	private static ArrayList<Tracker> TrackersRedundantes=new ArrayList<Tracker>();
	private LinkedList<Peer> PeersEnCola = new LinkedList<Peer>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VistaTracker window = new VistaTracker();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VistaTracker() {
		initialize();
	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		desconexion.add(0, true);
		frame = new JFrame();
		frame.setBounds(100, 100, 482, 531);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setBounds(100, 100, 419, 234);
	
		JTabbedPane panelDePestanas = new JTabbedPane(JTabbedPane.TOP);
		panelDePestanas.setBounds(10, 11, 383, 174);

		frame.getContentPane().add(panelDePestanas);

		PanelTracker = new PanelTracker(desconexion,TrackersRedundantes);
		panelDePestanas.addTab("Trackers", null, PanelTracker, null);


		PanelPeer = new PanelPeer(desconexion,TrackersRedundantes,PeersEnCola);
		panelDePestanas.addTab("Peers", null, PanelPeer, null);
		
		PanelConfiguracion = new PanelConfiguracion(PeersEnCola);

		panelDePestanas.addTab("Configuracion", null, PanelConfiguracion, null);
		
		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton button = new JButton("Desconectar");
	      button.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent ae) {
	        	  desconexion.set(0, false);
	        	  try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	             System.exit(0);
	          }
	       });
		panel.add(button);
		
		

		frame.pack();

		
	}

	public PanelPeer getPanelPeer() {
		return PanelPeer;
	}

	public void setPanelPeer(PanelPeer panelPeer) {
		PanelPeer = panelPeer;
	}

	public PanelTracker getPanelTracker() {
		return PanelTracker;
	}

	public void setPanelTracker(PanelTracker panelTracker) {
		PanelTracker = panelTracker;
	}

	public PanelConfiguracion getPanelConfiguracion() {
		return PanelConfiguracion;
	}

	public void setPanelConfiguracion(PanelConfiguracion panelConfiguracion) {
		PanelConfiguracion = panelConfiguracion;
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
