package TorrentListeners;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import Objetos.Peer;
import bitTorrent.tracker.protocol.udp.PeerInfo;
import bitTorrent.tracker.protocol.udp.AnnounceRequest;
import bitTorrent.tracker.protocol.udp.AnnounceResponse;

public class AnnounceListener {
	
	private ArrayList<Peer> listaPeers;
	private String IP;
	private ArrayList<Integer> puerto;
	
	




	public AnnounceListener(ArrayList<Peer> peersTransactionId, String iP, ArrayList<Integer> puerto) {
		super();
		this.listaPeers = peersTransactionId;
		IP = iP;
		this.puerto = puerto;
	}






	//TODO mandar error en caso de que n se cumpla ninguna de las condiciones de que el peer no tenga conexion id valida
	public void receive(DatagramPacket reply) {
		System.out.println("RECEIVE DEL ANNOUNCE LISTENER");
		AnnounceRequest ar = AnnounceRequest.parse(reply.getData());
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
	    System.out.println("Action="+ar.getAction()); //CONNECT=0
	    System.out.println("Transaction="+ar.getTransactionId()); 
		System.out.println("Connection="+ar.getConnectionId()); 
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(reply.getData());
		byteBuffer.order(ByteOrder.BIG_ENDIAN);
		
	   

//		if(reply.getLength()==16) {
		for(Peer p:listaPeers) {

		    String address="/";
		    address=address+p.getIP();
		    InetAddress add=reply.getAddress();
		    String aadd=add.toString();

		    
			if(address.equals(aadd)){
				//mirar si el connection id es valido
				if(ar.getConnectionId()==p.getConnectionIdPrincipal() || ar.getConnectionId()==p.getConnectionIdSecundario()) {
					System.out.println("ConnectionId comprobado");
					if(ar.getConnectionId()==p.getTransactionId()) {System.err.println("ANNListener 0");}
					else if(ar.getTransactionId()==p.getConnectionIdPrincipal() || ar.getTransactionId()==p.getConnectionIdSecundario()) {System.out.println("ANNListener 1");}
					else if(ar.getTransactionId()==p.getTransactionId()) {

						
						//TODO comprobar el timestamp del mensaje (system.currentTimeMillis())

						//Mensaje de vuelta FIXME
						//###########################
//							String serverIP = this.getIP();
//							int serverPort = this.getPuerto().get(0);
						
						String serverIP = "192.168.0.11";
						int serverPort = 8000;
						
						try (DatagramSocket udpSocket = new DatagramSocket()) {

							
							InetAddress serverHost = InetAddress.getByName(serverIP);	
				
							AnnounceResponse announceResponse = new AnnounceResponse();
							
							announceResponse.setTransactionId(ar.getTransactionId());
							
							//TODO selects a la base de datos y obtener los tiempos y numeros
							
							List<PeerInfo> peers = new ArrayList<PeerInfo>();
							
							//obtener datos
							//for de peers para obtener los peerinfo y meterlos a la lista
							//contar seeders y leechers
							//hacer set de la lista y enviar
							
							announceResponse.setInterval(10000);//Cada 10 segundos que se envie el announceRequest
							announceResponse.setLeechers(5);//los que sean leechers,seeders y la lista de peers de la base de datos
							announceResponse.setSeeders(5);
							announceResponse.setPeers(peers);


							//#############################
							byte[] requestBytes = announceResponse.getBytes();			
							DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length, serverHost, serverPort);
							udpSocket.send(packet);
							System.out.println("IP Address:- " + InetAddress.getLocalHost());
							System.out.println(" - Sent from server response to "+ packet.getAddress() +":" + packet.getPort() + "' -> " + new String(packet.getData()) + " [" + packet.getLength() + " byte(s)]");

							//###########################

						break;
						
						
						} catch (SocketException | UnknownHostException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		
		for(Peer p:listaPeers) {
			System.out.println("22222222222222222222222222222222");
			System.out.println("Peer id principal=="+p.getConnectionIdPrincipal());
			System.out.println("Peer id secundario=="+p.getConnectionIdSecundario());
		}
	
	
	
	}


	public ArrayList<Peer> getPeersTransactionId() {
		return listaPeers;
	}



	public void setPeersTransactionId(ArrayList<Peer> peersTransactionId) {
		this.listaPeers = peersTransactionId;
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
}
