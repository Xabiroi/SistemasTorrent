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
import java.util.Random;

import Objetos.Peer;
import bitTorrent.tracker.protocol.udp.ConnectRequest;
import bitTorrent.tracker.protocol.udp.ConnectResponse;
import bitTorrent.tracker.protocol.udp.BitTorrentUDPMessage.Action;

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
		System.out.println("RECEIVE DEL CONNECTION LISTENER");
		ConnectRequest cr = ConnectRequest.parse(reply.getData());
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
	    System.out.println("Action="+cr.getAction()); //CONNECT=0
	    System.out.println("Transaction="+cr.getTransactionId()); 
		System.out.println("Connection="+cr.getConnectionId()); 
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(reply.getData());
		byteBuffer.order(ByteOrder.BIG_ENDIAN);
		
	   

		if(reply.getLength()==16) {
			//for de buscar transaction id con el peer
			for(Peer p:peersTransactionId) {
				//FIXME si la transactionId es buena

			    String address="/";
			    address=address+p.getIP();
//			    System.out.println(address);
			    InetAddress add=reply.getAddress();
			    String aadd=add.toString();

			    
				if(address.equals(aadd)){
					//mirar si el connection id es el default u otro, sino adjuntarle otro
					if(cr.getConnectionId()==Long.decode("0x41727101980")) {
						System.out.println("Inicio connectID");
						if(cr.getConnectionId()==p.getTransactionId()) {System.err.println("ConListener 0");}
						else if(cr.getTransactionId()==p.getConnectionIdPrincipal() || cr.getTransactionId()==p.getConnectionIdSecundario()) {System.out.println("ConListener 1");}
						else if(cr.getTransactionId()==p.getTransactionId()) {System.out.println("ConListener 2");}
						else {
							for(Peer p1:peersTransactionId) {
								System.out.println("111111111111111111111111111111");
								System.out.println("Peer id principal=="+p1.getConnectionIdPrincipal());
								System.out.println("Peer id secundario=="+p1.getConnectionIdSecundario());
							}
						    System.out.println("Action="+cr.getAction()); //CONNECT=0
						    System.out.println("Transaction="+cr.getTransactionId()); 
							System.out.println("Connection="+cr.getConnectionId()); 
							System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
							p.setTransactionId(cr.getTransactionId());
							System.out.println("ConnectRequest primero valido");
							System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
							Random r=new Random();
							long newConnectionId = r.nextLong();
	
							p.setTransactionId(cr.getTransactionId());
							p.setConnectionIdSecundario(p.getConnectionIdPrincipal());
							p.setConnectionIdPrincipal(newConnectionId);
							System.out.println(p.getConnectionIdPrincipal());
	
							//Mensaje de vuelta FIXME
							//###########################
//							String serverIP = this.getIP();
//							int serverPort = this.getPuerto().get(0);
							
							String serverIP = "192.168.0.11";
							int serverPort = this.getPuerto().get(0);
							
							try (DatagramSocket udpSocket = new DatagramSocket()) {

								
								InetAddress serverHost = InetAddress.getByName(serverIP);	
					
								ConnectResponse connectResponse = new ConnectResponse();
								
//								connectResponse.setAction(Action.CONNECT);
								connectResponse.setTransactionId(p.getTransactionId());
								connectResponse.setConnectionId(newConnectionId);
	
								System.out.println("CONNECTION_LISTENER");
//								System.out.println("¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬");
								System.out.println("¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬");
								System.out.println("ACTION connectionresponse=="+connectResponse.getAction());
								System.out.println("TRANSACTION connectionresponse=="+connectResponse.getTransactionId());
								System.out.println("ConnectionId connectionresponse=="+connectResponse.getConnectionId());
								System.out.println("¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬");
//								System.out.println("¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬");
								
								
								//#############################
								byte[] requestBytes = connectResponse.getBytes();			
								DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length, serverHost, serverPort);
								udpSocket.send(packet);
					
								ByteBuffer byteBufferr = ByteBuffer.wrap(packet.getData());				
								long a = byteBufferr.getLong(8);
								int b = byteBufferr.getInt(0);
								int c = byteBufferr.getInt(4);
								System.out.println("A en el connection Listener="+a);
								System.out.println("B en el connection Listener="+b);
								System.out.println("C en el connection Listener="+c);
								
								System.out.println(" - Sent from server response to '" + serverHost.getHostAddress() + ":" + packet.getPort() + "' -> " + new String(packet.getData()) + " [" + packet.getLength() + " byte(s)]");
								
								
								//###########################
	
							break;
							
							
							} catch (SocketException | UnknownHostException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}else {
						if(cr.getConnectionId()==p.getTransactionId()) {System.out.println("Connection listener FIN 1");}
						else if(cr.getTransactionId()==p.getConnectionIdPrincipal() || cr.getTransactionId()==p.getConnectionIdSecundario()) {System.out.println("Connection listener FIN 2");}
						else if(cr.getTransactionId()==p.getTransactionId()) {System.out.println("Connection listener FIN 3");}
						else {
							//comprobar que el connectionId es bueno (anterior y posterior)
							System.out.println("Connection id comprobando");
							System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
							System.out.println("cr.getConnectionId()=="+cr.getConnectionId());
							System.out.println("p.getConnectionIdPrincipal()=="+p.getConnectionIdPrincipal());
							System.out.println("p.getConnectionIdSecundario()=="+p.getConnectionIdSecundario());
							if(p.getConnectionIdPrincipal()==cr.getConnectionId() || p.getConnectionIdSecundario()==cr.getConnectionId()) {
								
								System.out.println("COMPROBADO");
								System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
								p.setTransactionId(cr.getTransactionId());
								p.setConnectionIdSecundario(p.getConnectionIdPrincipal());
								
								Random r=new Random();
								long newConnectionId = r.nextLong();
		
								p.setTransactionId(cr.getTransactionId());
								p.setConnectionIdSecundario(p.getConnectionIdPrincipal());
								p.setConnectionIdPrincipal(newConnectionId);
								System.out.println(p.getConnectionIdPrincipal());
		
								
								//Mensaje de vuelta
								//###########################
								String serverIP = this.getIP();
								int serverPort = this.getPuerto().get(0);
		
								
								try (DatagramSocket udpSocket = new DatagramSocket()) {
									//if con si se nos ha asignado id o es la primera vez
//									udpSocket.setSoTimeout(15000);
									
									InetAddress serverHost = InetAddress.getByName(serverIP);	
						
									ConnectResponse connectResponse = new ConnectResponse();
									
									connectResponse.setAction(Action.CONNECT);
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
		
		for(Peer p:peersTransactionId) {
			System.out.println("22222222222222222222222222222222");
			System.out.println("Peer id principal=="+p.getConnectionIdPrincipal());
			System.out.println("Peer id secundario=="+p.getConnectionIdSecundario());
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
