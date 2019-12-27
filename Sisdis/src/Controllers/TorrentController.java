package Controllers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Random;

import Objetos.Peer;
import TorrentListeners.AnnounceListener;
import TorrentListeners.ConnectionListener;
import TorrentListeners.ScrapeListener;
import bitTorrent.tracker.protocol.udp.ConnectRequest;
import bitTorrent.tracker.protocol.udp.ConnectResponse;

public class TorrentController extends Thread{

	
	
//	private static final String DEFAULT_IP = "127.0.0.1"; //Aqui iria la IP multicast a la que nos conectamos
//	private static final int DEFAULT_PORT = 7000;

	
	private String IP;
	private ArrayList<Integer> puerto;
	private ArrayList<Boolean> bucle;
	
	//TODO los arrays de peers funcionales
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




	public void run() {
		
		//Argumentos, de momento innecesarios ya que usamos la aplicacion por default
				String serverIP = this.getIP();
				int serverPort = this.getPuerto().get(0);


				while(this.getBucle().get(0)) {
					try (DatagramSocket udpSocket = new DatagramSocket(serverPort, InetAddress.getByName(serverIP))) {

						byte[] buffer = new byte[16];
						DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
						udpSocket.receive(reply);		

						ByteBuffer byteBuffer = ByteBuffer.wrap(reply.getData());
						byteBuffer.order(ByteOrder.BIG_ENDIAN);
						
						//Con el primer int obtenemos la id del response para clasificarla
					    int a= byteBuffer.getInt(8); //FIXME era 8 en los request(?)
					    
					    System.out.println("server a="+a);
						

					    
					    if(a == 0) {
							//CONNECT_RESPONSE
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


					} catch (SocketException e) {
						System.err.println("# UDPClient Socket error: " + e.getMessage());
						e.printStackTrace();
					} catch (IOException e) {
						System.err.println("# UDPClient IO error: " + e.getMessage());
					}
				}
		
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
