package Controllers;

import java.io.IOException;
import java.net.DatagramPacket;
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
import java.util.LinkedList;

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

	private LinkedList<Peer> PeersEnCola;
	
	
	


	public TorrentController(String iP, ArrayList<Integer> puerto, ArrayList<Boolean> bucle,
			ArrayList<Peer> peersTransactionId, ConnectionListener connectionListener,
			AnnounceListener announceListener, ScrapeListener scrapeListener, LinkedList<Peer> peersEnCola) {
		super();
		this.IP = iP;
		this.puerto = puerto;
		this.bucle = bucle;
		this.peersTransactionId = peersTransactionId;
		this.connectionListener = connectionListener;
		this.announceListener = announceListener;
		this.scrapeListener = scrapeListener;
		this.PeersEnCola = peersEnCola;
	}


	
	public void run() {
		
		//Argumentos, de momento innecesarios ya que usamos la aplicacion por default
				String serverIP = this.getIP();
				int serverPort = this.getPuerto().get(0);

				try (MulticastSocket socket = new MulticastSocket(serverPort)) {
					System.out.println("ServerIP="+serverIP);
					InetAddress group = InetAddress.getByName(serverIP);
//					socket.setNetworkInterface(getActiveInterface());
					socket.joinGroup(group);	
				
					while(this.getBucle().get(0)) {
						//TODO Ajustar aqui al size de los mensajes recibidos
						byte[] buffer = new byte[1024];

						DatagramPacket reply = new DatagramPacket(buffer, buffer.length, group, serverPort);

						socket.receive(reply);		
						//FIXME????? Se puede obtener el puerto por el que se ha enviado y hacer a la inversa con variables
//						System.out.println("reply.getPort()=="+reply.getPort());
//						System.out.println("reply.getSocketAddress()=="+reply.getSocketAddress());
//						System.out.println("reply.getAddress().getHostAddress()=="+reply.getAddress().getHostAddress());

						ByteBuffer byteBuffer = ByteBuffer.wrap(reply.getData());
						byteBuffer.order(ByteOrder.BIG_ENDIAN);
						
						//Con el primer int obtenemos la id del response para clasificarla
//					    int a= byteBuffer.getInt(0); //FIXME era 8 en los request(?)
//					    int b= byteBuffer.getInt(4);
					    int c= byteBuffer.getInt(8);
//					    int d= byteBuffer.getInt(12);

//					    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//					    System.out.println("Action server a="+a);
//					    System.out.println("Transaction server b="+b);
//					    System.out.println("server c="+c);
//					    System.out.println("server d="+d);
					    
//					    long aa= byteBuffer.getLong(0); //FIXME era 8 en los request(?)
//					    long ab= byteBuffer.getLong(8);

  
//					    System.out.println("server aa="+aa);
//					    System.out.println("Conn server ab="+ab);
//					    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");


					    if(c == 0) {
							//CONNECT_RESPONSE
					    	System.out.println("Connect response");
							this.getConnectionListener().receive(reply);
						}
						else if (c == 1){
							//ANOUNCE_RESPONSE
							this.getAnnounceListener().receive(reply);
						}
						else if (c == 2){
							//SCRAPE_RESPONSE
	
							this.getScrapeListener().receive(reply);
						}
						else if (c == 3){
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


	public LinkedList<Peer> getPeersEnCola() {
		return PeersEnCola;
	}


	public void setPeersEnCola(LinkedList<Peer> peersEnCola) {
		PeersEnCola = peersEnCola;
	}


	
}
