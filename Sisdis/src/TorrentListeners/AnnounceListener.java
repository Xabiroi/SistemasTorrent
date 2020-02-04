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
import bitTorrent.util.ByteUtils;
import bitTorrent.tracker.protocol.udp.AnnounceRequest;
import bitTorrent.tracker.protocol.udp.AnnounceResponse;

public class AnnounceListener {
	
	private ArrayList<Peer> listaPeers;
	private String IP;
	private ArrayList<Integer> puerto;
	private final static int interval=10000;	
	private LinkedList<Peer> PeersEnCola;
	
	
	public AnnounceListener(ArrayList<Peer> listaPeers, String iP, ArrayList<Integer> puerto,
			LinkedList<Peer> peersEnCola) {
		super();
		this.listaPeers = listaPeers;
		this.IP = iP;
		this.puerto = puerto;
		this.PeersEnCola = peersEnCola;
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
		
		SQLiteDBManager.updateTr(reply.getAddress().toString().substring(1),ar.getConnectionId(),ar.getTransactionId());
		//TODO Tambien updatear tiempo cada vez que llega 1??
		listaPeers=SQLiteDBManager.loadPeers2();
		
		System.out.println("ListaPeers="+listaPeers);
		
		
		
		
		for(Peer p:listaPeers) {

		    String address="/";
		    address=address+p.getIP();
		    InetAddress add=reply.getAddress();
		    String aadd=add.toString();

		    System.out.println("address="+address);
		    System.out.println("aadd="+aadd);
		    
		    

		    
			if(address.equals(aadd)){
				System.out.println("Address equal");
				if(ar.getConnectionId()==p.getConnectionIdPrincipal() || ar.getConnectionId()==p.getConnectionIdSecundario()) {
					System.out.println("ConnectionId comprobado");
//					System.out.println("ar.getTransactionId()=="+ar.getTransactionId());
//					System.out.println("p.getTransactionId()=="+p.getTransactionId());
					
					if(ar.getConnectionId()==p.getTransactionId()) {System.err.println("ANNListener 0");}
					else if(ar.getTransactionId()==p.getConnectionIdPrincipal() || ar.getTransactionId()==p.getConnectionIdSecundario()) {System.out.println("ANNListener 1");}
					else if(ar.getTransactionId()==p.getTransactionId()) {
						
//						System.out.println("p.getTiempo()+interval-50000=="+(p.getTiempo()+interval-50000));
//						System.out.println("p.getTiempo()+interval+50000=="+(p.getTiempo()+interval+50000));
//						System.out.println("System.currentTimeMillis()=="+System.currentTimeMillis());
						//TODO como comprobar o gestioanr en la bd
//						if((p.getTiempo()+interval-50000)<System.currentTimeMillis() && (p.getTiempo()+interval+50000)>System.currentTimeMillis()) {

							
							System.out.println("LA IP REPLY=="+reply.getAddress().toString());//ar.getPeerInfo().getStringIpAddress()
							System.out.println("LA IP AR=="+ar.getPeerInfo().getStringIpAddress());
							SQLiteDBManager.updateTiempo(reply.getAddress().toString(),ar.getConnectionId(),System.currentTimeMillis());
							
//							System.out.println("ACTUALIZA EL TIEMPO");

							
							System.out.println("INFOHASH=="+ar.getHexInfoHash());
							ArrayList<Swarm> swarmRaw = SQLiteDBManager.loadSwarmPeers(ar.getHexInfoHash());
							ArrayList<Peer> peersRaw = new ArrayList<Peer>();
							if(!swarmRaw.isEmpty()) {						
							
//								System.out.println("ANTES DEL GET");
							
								peersRaw = swarmRaw.get(0).getListaPeers();
								
//								System.out.println("EXplota porque no hay posicion 0 porque no se a anyadido el peer en todo el proceso");
								
								boolean encontrado=false;
								for(Peer peer:swarmRaw.get(0).getListaPeers()) {
									System.out.println("Comprobacion peer ip="+peer.getIP());
									System.out.println("Comprobacion peer announce="+reply.getAddress().toString().substring(1));
									if(peer.getIP().equals(reply.getAddress().toString().substring(1))) {
										encontrado=true;
									}
									
								}
								if(encontrado==false) {
									synchronized(PeersEnCola) {
										PeersEnCola.add(new Peer(reply.getAddress().toString().substring(1),ar.getPeerInfo().getPort(),ar.getHexInfoHash(),ar.getDownloaded(),ar.getLeft()));
		//								PeersEnCola.add(new Peer(ar.getPeerInfo().getStringIpAddress(),ar.getPeerInfo().getPort(),ar.getHexInfoHash(),ar.getDownloaded(),ar.getLeft()));
										System.out.println("HAY ALGO EN LA BASE DE DATOS");
									}
								}
							}
							else {
								synchronized(PeersEnCola) {
									PeersEnCola.add(new Peer(reply.getAddress().toString().substring(1),ar.getPeerInfo().getPort(),ar.getHexInfoHash(),ar.getDownloaded(),ar.getLeft()));
	//								PeersEnCola.add(new Peer(ar.getPeerInfo().getStringIpAddress(),ar.getPeerInfo().getPort(),ar.getHexInfoHash(),ar.getDownloaded(),ar.getLeft()));
									System.out.println("NO HAY NADA EN LA BD, ANYADIENDO");
								}
							
							}
							
							//Si no anyade, estara vacio, no se hace bucle
							while(!PeersEnCola.isEmpty()) {
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							}
							
//							System.out.println("HA SALIDO DEL BUCLE (ACTUALIZACION)");
							//Mensaje de vuelta FIXME
							//###########################
						
							
//							String serverIP = "192.168.0.11"; //Elegir las ips bien
							String serverIP = reply.getAddress().toString().substring(1); 
							int serverPort = 7001;
						
							try (DatagramSocket udpSocket = new DatagramSocket()) {
	
								
								InetAddress serverHost = InetAddress.getByName(serverIP);	
					
								AnnounceResponse announceResponse = new AnnounceResponse();
								
								announceResponse.setTransactionId(ar.getTransactionId());
	
								
								//TODO selects a la base de datos y obtener los tiempos y numeros
								
								List<PeerInfo> peers = new ArrayList<PeerInfo>();
								//obtener datos
								//ArrayList<Peer> peersRaw = SQLiteDBManager.loadPeers("123ABC");//FIXME seria el infohash aqui pero para pruebas
								

								/*FIXME comprobar si se ha anyadido con un wait en todo el proceso(?)
								 * evitar usar el get(0) si isEmpty (?)
								 * */
								
								ArrayList<Swarm> swarmRaw2 = SQLiteDBManager.loadSwarmPeersAnnounce(ar.getHexInfoHash());
								ArrayList<Peer> peersRaw2 = swarmRaw2.get(0).getListaPeers();
								System.out.println("swarmRaw2=="+swarmRaw2);
								System.out.println("Peersraw2=="+peersRaw2.get(0).getIP());
								
//								System.err.println("LLEGA HASTA AQUI");
								//for de peers para obtener los peerinfo y meterlos a la lista
								for(Peer pe:peersRaw2) {
									System.out.println("Dentro del bucle");
									System.out.println("pe.getIdentificadorSwarm()=="+pe.getIdentificadorSwarm());
									System.out.println("ar.getHexInfoHash()=="+ar.getHexInfoHash());
									if(pe.getIdentificadorSwarm().equals(ar.getHexInfoHash())) {
//										System.out.println("Dentro del if");
										
										
//										String dir = pe.getIP().replaceAll("[^0-9]","");
//										int ip = Integer.parseInt(dir);
										int ip=ByteUtils.ipAddressToInt(pe.getIP());
										PeerInfo pinf= new PeerInfo();
										pinf.setIpAddress(ip);
//										System.out.println("EL PUERTO=="+pe.getPuerto());
//										System.out.println("EL PUERTO (short)=="+(short)pe.getPuerto());
										pinf.setPort(pe.getPuerto());
										peers.add(pinf);
									}
								}
								
//								System.out.println("POST FOR");
								
								int seeders=0;
								int leechers=0;
								for(Peer peer:peersRaw2) {
									if(peer.getDescargado()<swarmRaw2.get(0).getSize()) {
										leechers++;
									}
									seeders++;
								}
								
//								System.out.println("POST SEGUNDO FOR");
	
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
								
//								System.out.println("CREACION DEL RESPONSE");
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
//						}
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
