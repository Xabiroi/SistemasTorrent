package TorrentListeners;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

import Objetos.Peer;
import bitTorrent.tracker.protocol.udp.ConnectRequest;
import bitTorrent.tracker.protocol.udp.ConnectResponse;

public class ConnectionListener {
	

	private ArrayList<Peer> peersTransactionId;
	private String IP;
	private ArrayList<Integer> puerto;
	
	
	

	public ConnectionListener(ArrayList<Peer> peersTransactionId, String iP, ArrayList<Integer> puerto) {
		super();
		this.peersTransactionId = peersTransactionId;
		IP = iP;
		this.puerto = puerto;
	}



	//TODO mandar error en caso de que n se cumpla ninguna de las condiciones de que el peer no tenga conexion id valida
	public void receive(DatagramPacket reply) {
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
	    ConnectRequest cr = ConnectRequest.parse(reply.getData());
	    System.out.println("CR="+cr.getAction()); //CONNECT=0
	    System.out.println("CR="+cr.getTransactionId()); 
		System.out.println("CR="+cr.getConnectionId()); 
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		
		//VALIDATE
		if(reply.getLength()==16) {
			//for de buscar transaction id con el peer
			for(Peer p:peersTransactionId) {
				//FIXME si la transactionId es buena

			    String address="/";
			    address=address+p.getIP();
//			    System.out.println(address);
			    InetAddress add=reply.getAddress();
			    String aadd=add.toString();
//			    System.out.println(address.equals(aadd));
				
				if(address.equals(aadd)) {
					
					
					
					//mirar si el connection id es el default u otro, sino adjuntarle otro
					if(cr.getConnectionId()==Long.decode("0x41727101980")) {
						//FIXME con set o con igual como los arraylist
						p.setTransactionId(cr.getTransactionId());
						System.out.println("ConnectRequest primero valido");
						Random r=new Random();
						int newConnectionId = r.nextInt(Integer.MAX_VALUE);

						p.setConnectionIdPrincipal(newConnectionId);

						//Mensaje de vuelta
						//###########################
						String serverIP = this.getIP();
						int serverPort = this.getPuerto().get(0);

						
						try (DatagramSocket udpSocket = new DatagramSocket()) {
							//if con si se nos ha asignado id o es la primera vez
							udpSocket.setSoTimeout(15000);
							
							InetAddress serverHost = InetAddress.getByName(serverIP);	
				
							ConnectResponse connectResponse = new ConnectResponse();
							
							connectResponse.setTransactionId(p.getTransactionId());
							connectResponse.setConnectionId(newConnectionId);

							
							//#############################
							byte[] requestBytes = connectResponse.getBytes();			
							DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length, serverHost, serverPort);
							udpSocket.send(packet);
				
							System.out.println(" - Sent from server response to '" + serverHost.getHostAddress() + ":" + packet.getPort() + "' -> " + new String(packet.getData()) + " [" + packet.getLength() + " byte(s)]");
							
							
							//###########################

						break;
						
						
						} catch (SocketException | UnknownHostException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else {
						//comprobar que el connectionId es bueno (anterior y posterior)
						
						if(p.getConnectionIdPrincipal()==cr.getConnectionId() || p.getConnectionIdSecundario()==cr.getConnectionId()) {
						
							p.setTransactionId(cr.getTransactionId());
							p.setConnectionIdSecundario(p.getConnectionIdPrincipal());
							
							Random r=new Random();
							int newConnectionId = r.nextInt(Integer.MAX_VALUE);
	
							p.setConnectionIdPrincipal(newConnectionId);
	
							
							//Mensaje de vuelta
							//###########################
							String serverIP = this.getIP();
							int serverPort = this.getPuerto().get(0);
	
							
							try (DatagramSocket udpSocket = new DatagramSocket()) {
								//if con si se nos ha asignado id o es la primera vez
								udpSocket.setSoTimeout(15000);
								
								InetAddress serverHost = InetAddress.getByName(serverIP);	
					
								ConnectResponse connectResponse = new ConnectResponse();
								
								connectResponse.setTransactionId(p.getTransactionId());
								connectResponse.setConnectionId(newConnectionId);
	
								//#############################
								byte[] requestBytes = connectResponse.getBytes();			
								DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length, serverHost, serverPort);
								udpSocket.send(packet);
					
								System.out.println(" - Sent from server response to '" + serverHost.getHostAddress() + ":" + packet.getPort() + "' -> " + new String(packet.getData()) + " [" + packet.getLength() + " byte(s)]");
								
							
	
							break;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}else {
							
							//TODO enviar error
						}
					}
				}
			}
		}
	
	
	
	}
		



		public ArrayList<Peer> getPeersTransactionId() {
			return peersTransactionId;
		}




		public void setPeersTransactionId(ArrayList<Peer> peersTransactionId) {
			this.peersTransactionId = peersTransactionId;
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
