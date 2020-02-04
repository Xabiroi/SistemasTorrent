package Paneles;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controllers.TorrentController;
import Objetos.Peer;
import TorrentListeners.AnnounceListener;
import TorrentListeners.ConnectionListener;
import TorrentListeners.ScrapeListener;

public class PanelConfiguracion extends JPanel {
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField txtIdNoEditable;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String IP;
	private ArrayList<Integer> puerto;
	private ArrayList<Boolean> bucle;
	
	//TODO los arrays de peers funcionales
	private ArrayList<Peer> listaPeers;
	
	private ConnectionListener connectionListener;
	private AnnounceListener announceListener;
	private ScrapeListener scrapeListener;
	
	private TorrentController torrentController;
	private LinkedList<Peer> PeersEnCola;

	/**
	 * Create the panel.
	 */
	public PanelConfiguracion(LinkedList<Peer> peersEnCola) {
		//Creacion de los atributos
		//############################
		IP="228.5.6.7"; //FIXME hardcodeado
		puerto=new ArrayList<Integer>(1);
		puerto.add(7000); //FIXME hardcodeado
		
		listaPeers = new ArrayList<Peer>(10);//FIXME hardcodeado
//		listaPeers.add(new Peer(IP2, puerto.get(0),"ABCD", 1, Long.decode("0x41727101980"),456));
		
		bucle= new ArrayList<Boolean>(1);
		bucle.add(true);
		
		connectionListener = new ConnectionListener(listaPeers, IP, puerto);
		announceListener = new AnnounceListener(listaPeers, IP, puerto,peersEnCola);
		scrapeListener = new ScrapeListener(listaPeers, IP, puerto);
		
		torrentController = new TorrentController(IP, puerto, bucle, listaPeers, connectionListener, announceListener, scrapeListener,peersEnCola);
		torrentController.start();
		
		
		
		
		//############################
		this.setLayout(new GridLayout(0, 3, 0, 0));
		
		JLabel lblJms = new JLabel("JMS");
		this.add(lblJms);
		
		JLabel label = new JLabel("");
		this.add(label);
		
		JLabel label_1 = new JLabel("");
		this.add(label_1);
		
		JLabel lblip = new JLabel("-IP:");
		this.add(lblip);
		
		textField_4 = new JTextField();
		this.add(textField_4);
		textField_4.setColumns(10);
		
		JLabel label_2 = new JLabel("");
		this.add(label_2);
		
		JLabel lblpuerto = new JLabel("-Puerto:");
		this.add(lblpuerto);
		
		textField_5 = new JTextField();
		this.add(textField_5);
		textField_5.setColumns(10);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
		});
		this.add(btnConectar);
		
		JLabel label_4 = new JLabel("");
		this.add(label_4);
		
		JLabel label_5 = new JLabel("");
		this.add(label_5);
		
		JLabel label_6 = new JLabel("");
		this.add(label_6);
		
		JLabel lblIpMulticast_1 = new JLabel("IP Multicast:");
		this.add(lblIpMulticast_1);
		
		textField_6 = new JTextField();
		this.add(textField_6);
		textField_6.setColumns(10);
		
		JLabel label_9 = new JLabel("");
		this.add(label_9);
		
		JLabel lblMascara = new JLabel("Mascara: ");
		this.add(lblMascara);
		
		textField_7 = new JTextField();
		this.add(textField_7);
		textField_7.setColumns(10);
		
		JButton btnConectar_1 = new JButton("Conectar");
		btnConectar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		this.add(btnConectar_1);
		
		JLabel lblId = new JLabel("ID");
		this.add(lblId);
		
		txtIdNoEditable = new JTextField();
		txtIdNoEditable.setEditable(false);
		txtIdNoEditable.setText("id no editable");
		this.add(txtIdNoEditable);
		txtIdNoEditable.setColumns(10);
	}
	
    private static final String zeroTo255 = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";

	private static final String IP_REGEXP = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
	
	private static final Pattern IP_PATTERN = Pattern.compile(IP_REGEXP);
	
	// Return true when *address* is IP Address
	private boolean isValid(String address) {
		return IP_PATTERN.matcher(address).matches();
	}
		
	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public ArrayList<Integer> getPuerto() {
		return puerto;
	}

	public void setPuerto(ArrayList<Integer> puerto) {
		this.puerto = puerto;
	}

	public ArrayList<Boolean> getBucle() {
		return bucle;
	}

	public void setBucle(ArrayList<Boolean> bucle) {
		this.bucle = bucle;
	}

	public ArrayList<Peer> getPeersTransactionId() {
		return listaPeers;
	}

	public void setPeersTransactionId(ArrayList<Peer> peersTransactionId) {
		this.listaPeers = peersTransactionId;
	}

	public ConnectionListener getConnectionListener() {
		return connectionListener;
	}

	public void setConnectionListener(ConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}

	public AnnounceListener getAnnounceListener() {
		return announceListener;
	}

	public void setAnnounceListener(AnnounceListener announceListener) {
		this.announceListener = announceListener;
	}

	public ScrapeListener getScrapeListener() {
		return scrapeListener;
	}

	public void setScrapeListener(ScrapeListener scrapeListener) {
		this.scrapeListener = scrapeListener;
	}

	public TorrentController getTorrentController() {
		return torrentController;
	}

	public void setTorrentController(TorrentController torrentController) {
		this.torrentController = torrentController;
	}

	public LinkedList<Peer> getPeersEnCola() {
		return PeersEnCola;
	}

	public void setPeersEnCola(LinkedList<Peer> peersEnCola) {
		PeersEnCola = peersEnCola;
	}


	
}
