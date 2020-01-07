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
import java.util.LinkedList;
import java.util.List;
import Objetos.Peer;
import Objetos.Swarm;
import Sqlite.SQLiteDBManager;
import bitTorrent.tracker.protocol.udp.PeerInfo;
import bitTorrent.tracker.protocol.udp.AnnounceRequest;
import bitTorrent.tracker.protocol.udp.AnnounceResponse;

public class AnnounceListener {
	
	private ArrayList<Peer> listaPeers;
	private String IP;
	private ArrayList<Integer> puerto;
	private final static int interval=10000;	
	private LinkedList<Peer> PeersEnCola = new LinkedList<Peer>();
	
	
	public AnnounceListener(ArrayList<Peer> listaPeers, String iP, ArrayList<Integer> puerto,
			LinkedList<Peer> peersEnCola) {
		super();
		this.listaPeers = listaPeers;
		IP = iP;
		this.puerto = puerto;
		PeersEnCola = peersEnCola;
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
		
		SQLiteDBManager.updateTr(reply.getAddress().toString(),ar.getConnectionId(),ar.getTransactionId());
		listaPeers=SQLiteDBManager.loadPeers2();
		
		System.out.println("ListaPeers="+listaPeers);
		
		
		for(Peer p:listaPeers) {

		    String address="";
		    address=address+p.getIP();
		    InetAddress add=reply.getAddress();
		    String aadd=add.toString();

		    System.out.println("address="+address);
		    System.out.println("aadd="+aadd);
		    
		    
		    //FIXME comprobar que si la transaccion en la actual que no la pase (mensajes repetidos)
		    
			if(address.equals(aadd)){
//				System.out.println("ADDRESS");
				//mirar si el connection id es valido
//				System.out.println("ar.getConnectionId()=="+ar.getConnectionId());
//				System.out.println("p.getConnectionIdPrincipal()=="+p.getConnectionIdPrincipal());
//				System.out.println("p.getConnectionIdSecundario()=="+p.getConnectionIdSecundario());
				
				if(ar.getConnectionId()==p.getConnectionIdPrincipal() || ar.getConnectionId()==p.getConnectionIdSecundario()) {
//					System.out.println("ConnectionId comprobado");
//					System.out.println("ar.getTransactionId()=="+ar.getTransactionId());
//					System.out.println("p.getTransactionId()=="+p.getTransactionId());
					
					if(ar.getConnectionId()==p.getTransactionId()) {System.err.println("ANNListener 0");}
					else if(ar.getTransactionId()==p.getConnectionIdPrincipal() || ar.getTransactionId()==p.getConnectionIdSecundario()) {System.out.println("ANNListener 1");}
					else if(ar.getTransactionId()==p.getTransactionId()) {
						
						System.out.println("p.getTiempo()+interval-3000=="+(p.getTiempo()+interval-3000));
						System.out.println("p.getTiempo()+interval+3000=="+(p.getTiempo()+interval+3000));
						System.out.println("System.currentTimeMillis()=="+System.currentTimeMillis());
						//TODO como comprobar o gestioanr en la bd
						if((p.getTiempo()+interval-3000)<System.currentTimeMillis() && (p.getTiempo()+interval+3000)>System.currentTimeMillis()) {
							System.out.println("ACTUALIZA EL TIEMPO");
//							p.setTiempo(System.currentTimeMillis());
							SQLiteDBManager.updateTiempo(reply.getAddress().toString(),ar.getConnectionId(),System.currentTimeMillis());
							
							
//							Peer(String iP, int puerto, String identificadorSwarm, long descargado, long left, String infoHash)
							
							PeersEnCola.add(new Peer(ar.getPeerInfo().getStringIpAddress(),ar.getPeerInfo().getPort(),ar.getHexInfoHash(),ar.getDownloaded(),ar.getLeft()));
							
							System.out.println("ANYADE PEERSENCOLA");
							//Mensaje de vuelta FIXME
							//###########################
						
							String serverIP = "192.168.0.11"; //Elegir las ips bien
							int serverPort = 8000;
						
							try (DatagramSocket udpSocket = new DatagramSocket()) {
	
								
								InetAddress serverHost = InetAddress.getByName(serverIP);	
					
								AnnounceResponse announceResponse = new AnnounceResponse();
								
								announceResponse.setTransactionId(ar.getTransactionId());
	
								
								//TODO selects a la base de datos y obtener los tiempos y numeros
								
								List<PeerInfo> peers = new ArrayList<PeerInfo>();
								//obtener datos
								//ArrayList<Peer> peersRaw = SQLiteDBManager.loadPeers("123ABC");//FIXME seria el infohash aqui pero para pruebas
								ArrayList<Swarm> swarmRaw = SQLiteDBManager.loadSwarmPeers(ar.getHexInfoHash());
								ArrayList<Peer> peersRaw = swarmRaw.get(0).getListaPeers();
								
								System.out.println("EXplota porque no hay posicion 0 porque no se a anyadido el peer en todo el proceso");
								/*FIXME comprobar si se ha anyadido con un wait en todo el proceso(?)
								 * evitar usar el get(0) si isEmpty (?)
								 * */
								
								
								//for de peers para obtener los peerinfo y meterlos a la lista
								for(Peer pe:peersRaw) {
									if(pe.getIdentificadorSwarm().equals(ar.getHexInfoHash())) {
										String dir = pe.getIP().replaceAll("[^0-9]","");
										int ip = Integer.parseInt(dir);
										PeerInfo pinf= new PeerInfo();
										pinf.setIpAddress(ip);
										pinf.setPort((short)pe.getPuerto());
										peers.add(pinf);
									}
								}
								
								int seeders=0;
								int leechers=0;
								for(Peer peer:peersRaw) {
									if(peer.getDescargado()<swarmRaw.get(0).getSize()) {
										leechers++;
									}
									seeders++;
								}
								
	
								//TODO contar seeders y leechers
								//hacer set de la lista y enviar
								
								announceResponse.setInterval(11000);//Cada 11 segundos que se envie el announceRequest?
								
								//FIXME vaciar al principio la base de datos, y al ejecutar el connectRequest anyadir ahi el peer a memoria, y en el announce ahi a la bd
								
								/* Buscar en una lista los archivos disponibles que haya - base de datos Swarm = Infohash + tamanyo
								 * Comprobar u obtener el tamanyo maximo
								 * Hacer select de la tabla peer de cuanto tienen descargado
								 * Separar los que tengan menos del maximo como leecher
								 * Establecer numeros
								 * */
								announceResponse.setLeechers(leechers);//los que sean leechers,seeders y la lista de peers de la base de datos
								announceResponse.setSeeders(seeders);
								announceResponse.setPeers(peers);
								
	
								//#############################
								byte[] requestBytes = announceResponse.getBytes();			
								DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length, serverHost, serverPort);
								udpSocket.send(packet);
	
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


	public LinkedList<Peer> getPeersEnCola() {
		return PeersEnCola;
	}


	public void setPeersEnCola(LinkedList<Peer> peersEnCola) {
		PeersEnCola = peersEnCola;
	}
}
