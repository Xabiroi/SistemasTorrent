package Controllers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import Objetos.Peer;
import TorrentListeners.AnnounceListener;
import TorrentListeners.ConnectionListener;
import TorrentListeners.ScrapeListener;

public class TorrentController extends Thread{

	
	
//	private static final String DEFAULT_IP = "127.0.0.1"; //Aqui iria la IP multicast a la que nos conectamos
//	private static final int DEFAULT_PORT = 7000;

	
	private String IP;
	private ArrayList<Integer> puerto;
	private ArrayList<Boolean> bucle;
	
	//Los arrays de peers funcionales
	private ArrayList<Peer> peersTransactionId;
	
	private ConnectionListener connectionListener;
	private AnnounceListener announceListener;
	private ScrapeListener scrapeListener;


	
	//Funcionalidad UDP del torrent
	public TorrentController(String iP, ArrayList<Integer> puerto, ArrayList<Boolean> bucle,
			ArrayList<Peer> peersTransactionId, ConnectionListener connectionListener,
			AnnounceListener announceListener, ScrapeListener scrapeListener) {
		super();
		IP = iP;
		this.puerto = puerto;
		this.bucle = bucle;
		this.peersTransactionId = peersTransactionId;
		this.connectionListener = connectionListener;
		this.announceListener = announceListener;
		this.scrapeListener = scrapeListener;
	}

	
//	private static final String DEFAULT_IP = "228.5.6.7"; //La multicast a la que nos tenemos que conectar tiene esta pinta
//	private static final int DEFAULT_PORT = 9000;
//	private static final String DEFAULT_MESSAGE = "Hello World!";
	
	public void run() {
		
		//Argumentos, de momento innecesarios ya que usamos la aplicacion por default
				String serverIP = this.getIP();
				int serverPort = this.getPuerto().get(0);

				
//				String groupIP = DEFAULT_IP;
//				int port = DEFAULT_PORT;
				
//				String message = DEFAULT_MESSAGE;
				System.out.println("(((((((((((((((((((((((((((((((((");
				System.out.println("(((((((((((((((((((((((((((((((((");
				System.out.println("Conectando a multicast");
				System.out.println("(((((((((((((((((((((((((((((((((");
				System.out.println("(((((((((((((((((((((((((((((((((");
				try (MulticastSocket socket = new MulticastSocket(serverPort)) {
					System.out.println("ServerIP="+serverIP);
					InetAddress group = InetAddress.getByName(serverIP);
					socket.setNetworkInterface(getActiveInterface());
					socket.joinGroup(group);	
				
//					try (DatagramSocket udpSocket = new DatagramSocket(serverPort, InetAddress.getByName(serverIP))) {
					System.out.println("=======================================");
					System.out.println("=======================================");
					System.out.println(this.getBucle().get(0));
					System.out.println("=======================================");
					System.out.println("=======================================");
					while(this.getBucle().get(0)) {
						//TODO Ajustar aqui al size de los mensajes recibidos
						byte[] buffer = new byte[16];
						System.out.println("¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿");
						System.out.println("ESPERANDO AL PAQUETE");
						System.out.println("¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿");
						DatagramPacket reply = new DatagramPacket(buffer, buffer.length, group, serverPort);

						socket.receive(reply);		

						ByteBuffer byteBuffer = ByteBuffer.wrap(reply.getData());
						byteBuffer.order(ByteOrder.BIG_ENDIAN);
						
						//Con el primer int obtenemos la id del response para clasificarla
					    int a= byteBuffer.getInt(8); //FIXME era 8 en los request(?)
					    
					    System.out.println("server a="+a);
					    
	//					    byte[] buffer = new byte[16];
	//						DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
	//						udpSocket.receive(reply);		
	//
	//						ByteBuffer byteBuffer = ByteBuffer.wrap(reply.getData());
	//						byteBuffer.order(ByteOrder.BIG_ENDIAN);
	//						
	//						//Con el primer int obtenemos la id del response para clasificarla
	//					    int a= byteBuffer.getInt(8); //FIXME era 8 en los request(?)
	//					    
	//					    System.out.println("server a="+a);
						
					    if(a == 0) {
							//CONNECT_RESPONSE
					    	System.out.println("Connect response");
							getConnectionListener().receive(reply);
						}
						else if (a == 1){
							//ANOUNCE_RESPONSE
							getAnnounceListener().receive(reply);
						}
						else if (a == 2){
							//SCRAPE_RESPONSE
	
							getScrapeListener().receive(reply);
						}
						else if (a == 3){
							//ERROR
							//TODO gestionar mensaje error (simple)
						}

					}
					socket.leaveGroup(group);
					
				} catch (SocketException e) {
					System.err.println("# UDPClient Socket error: " + e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("# UDPClient IO error: " + e.getMessage());
				}
				
		
		}
	

	public static NetworkInterface getActiveInterface() {
		
		try {		
			//iterate over the network interfaces known to java
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		
			for (NetworkInterface networkInterface : Collections.list(interfaces)) {
				// we shouldn't care about loopback addresses
				if (networkInterface.isLoopback())
					continue;

				// if you don't expect the interface to be up you can skip this
				// though it would question the usability of the rest of the code
				if (!networkInterface.isUp())
					continue;

				// iterate over the addresses associated with the interface
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
		  
				for (InetAddress address : Collections.list(addresses)) {
					// look only for ipv4 addresses
					if (address instanceof Inet6Address)
						continue;

					// use a timeout big enough for your needs
					if (!address.isReachable(1000))
						continue;

					return networkInterface;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
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
		return peersTransactionId;
	}

	public void setPeersTransactionId(ArrayList<Peer> peersTransactionId) {
		this.peersTransactionId = peersTransactionId;
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

	
}
