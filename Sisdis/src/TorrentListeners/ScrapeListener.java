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
import Objetos.Peer;
import Objetos.Swarm;
import Sqlite.SQLiteDBManager;
import bitTorrent.tracker.protocol.udp.ScrapeRequest;
import bitTorrent.tracker.protocol.udp.ScrapeResponse;
import bitTorrent.tracker.protocol.udp.ScrapeInfo;

public class ScrapeListener {
	
	private ArrayList<Peer> listaPeers;
	private String IP;
	private ArrayList<Integer> puerto;
	
	public ScrapeListener(ArrayList<Peer> peersTransactionId, String iP, ArrayList<Integer> puerto) {
		super();
		this.listaPeers = peersTransactionId;
		IP = iP;
		this.puerto = puerto;
	}

	//TODO mandar error en caso de que n se cumpla ninguna de las condiciones de que el peer no tenga conexion id valida
		public void receive(DatagramPacket reply) {
			System.out.println("RECEIVE DEL SCRAPE LISTENER");
			ScrapeRequest sRequest = ScrapeRequest.parse(reply.getData());
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		    System.out.println("Action= "+sRequest.getAction()); //SCRAPE=2
		    System.out.println("Transaction= "+sRequest.getTransactionId()); 
			System.out.println("Connection= "+sRequest.getConnectionId());
			System.out.println("Number of InfoHashes= "+sRequest.getInfoHashes().size());
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			
			ByteBuffer byteBuffer = ByteBuffer.wrap(reply.getData());
			byteBuffer.order(ByteOrder.BIG_ENDIAN);
						
			
			
			
			for(Peer p:listaPeers) {

			    String address="/";
			    address=address+p.getIP(); //dirección ip del peer
			    
			    InetAddress add=reply.getAddress();
			    String aadd=add.toString(); //direccion ip del mensaje

			    
				if(address.equals(aadd)) //encontramos el peer que envía
				{
					//mirar si el connection id es valido
					if(sRequest.getConnectionId()==p.getConnectionIdPrincipal() || sRequest.getConnectionId()==p.getConnectionIdSecundario()) {
						System.out.println("ConnectionId comprobado");
						if(sRequest.getConnectionId()==p.getTransactionId())
						{
							System.err.println("SCRListener 0");
						}
						else if(sRequest.getTransactionId()==p.getConnectionIdPrincipal() || sRequest.getTransactionId()==p.getConnectionIdSecundario())
						{
							System.out.println("SCRListener 1");
						}
						else if(sRequest.getTransactionId()==p.getTransactionId())
						{
								//Mensaje de vuelta FIXME
								//###########################
							
								String serverIP = "192.168.0.11"; //Elegir las ips bien
								int serverPort = 8000;
							
								try (DatagramSocket udpSocket = new DatagramSocket()) {
		
									
									InetAddress serverHost = InetAddress.getByName(serverIP);	
							
									ArrayList<Swarm> swarms = new ArrayList<Swarm>();
									ArrayList<String> infoHashes = (ArrayList<String>) sRequest.getInfoHashes();
						 
									swarms = SQLiteDBManager.loadSwarms(infoHashes);			
									
									ScrapeResponse sResp = new ScrapeResponse();
									
									for(Swarm swarm : swarms)
									{
										ArrayList<Peer> peersForHash = swarm.getListaPeers();
										ArrayList<Peer> seedersForHash = new ArrayList<Peer>();
										ArrayList<Peer> leechersForHash = new ArrayList<Peer>();
										
										for(Peer peer : peersForHash)
										{
											if(swarm.getSize() == peer.getDescargado())
												seedersForHash.add(peer);
											else
												leechersForHash.add(peer);
										}
										
										ScrapeInfo scrapeInfo = new ScrapeInfo();
										
										scrapeInfo.setLeechers(leechersForHash.size());
										scrapeInfo.setSeeders(seedersForHash.size());
										
										sResp.addScrapeInfo(scrapeInfo);
									}
									
									sResp.setTransactionId(sRequest.getTransactionId());
									sResp.setAction(sRequest.getAction());
									
									byte[] requestBytes = sResp.getBytes();			
									DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length, serverHost, serverPort);
									udpSocket.send(packet);
		
									System.out.println(" - Sent from server response to "+ packet.getAddress() +":" + packet.getPort() + "' -> " + new String(packet.getData()) + " [" + packet.getLength() + " byte(s)]");
		
									
								
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
