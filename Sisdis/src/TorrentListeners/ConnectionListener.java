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
import Sqlite.SQLiteDBManager;
import bitTorrent.tracker.protocol.udp.ConnectRequest;
import bitTorrent.tracker.protocol.udp.ConnectResponse;
import bitTorrent.tracker.protocol.udp.BitTorrentUDPMessage.Action;

public class ConnectionListener {
	

	private ArrayList<Peer> listaPeers;
	private String IP;
	private ArrayList<Integer> puerto;
	
	
	

	public ConnectionListener(ArrayList<Peer> peersTransactionId, String iP, ArrayList<Integer> puerto) {
		super();
		this.listaPeers = peersTransactionId;
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
		
		
		listaPeers=SQLiteDBManager.loadPeers2();
		
		
		for(Peer p:listaPeers) {
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println("p.getConIdPr=="+p.getConnectionIdPrincipal());
			System.out.println("p.getConIdSc=="+p.getConnectionIdSecundario());
			System.out.println("Addr=="+p.getIP());
		}
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(reply.getData());
		byteBuffer.order(ByteOrder.BIG_ENDIAN);

			//mirar si el connection id es el default u otro, sino adjuntarle otro
			if(cr.getConnectionId()==4497486125440L) {
				System.out.println("Inicio connectID");
				
				Random r=new Random();
				long newConnectionId = r.nextLong();
				int transactionid = cr.getTransactionId();
				
				System.out.println("newConnectionId=="+newConnectionId);
				
				boolean encontrado = false;
				for(Peer p:listaPeers) {
					if(p.getIP().equals(reply.getAddress().toString().substring(1))) {System.out.println("Misma ip intenta conectarse");encontrado=true;}	
				}
				
				if(!encontrado) {
					System.out.println("¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬");
					System.out.println("¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬");
					System.out.println("reply.getAddress().toString().substring(1)=="+reply.getAddress().toString().substring(1));
					System.out.println("Integer.toString(reply.getPort())=="+Integer.toString(reply.getPort()));
					System.out.println("newConnectionId=="+newConnectionId);
					System.out.println("¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬");
					System.out.println("¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬");
					
					SQLiteDBManager.insertPeer(reply.getAddress().toString().substring(1),Integer.toString(reply.getPort()),newConnectionId,cr.getConnectionId(),transactionid,System.currentTimeMillis());
//					listaPeers.add(new Peer(reply.getAddress().toString(),reply.getPort(),"",transactionid,newConnectionId,cr.getConnectionId()));
					System.out.println("NO ENCONTRADO");
					}

//				
//				for(Peer p:listaPeers) {
//					System.out.println("/////////////////////////");
//					System.out.println("p.getConIdPr=="+p.getConnectionIdPrincipal());
//					System.out.println("p.getConIdSc=="+p.getConnectionIdSecundario());
//					System.out.println("Addr=="+p.getIP());
//				}
				//Mensaje de vuelta FIXME
				//###########################
//				String serverIP = this.getIP();
//				int serverPort = this.getPuerto().get(0);
				
//				String serverIP = "192.168.0.11";
				String serverIP = reply.getAddress().toString().substring(1); 
				int serverPort = 7001;
				if(encontrado) {}
				else {
				try (DatagramSocket udpSocket = new DatagramSocket()) {
					InetAddress serverHost = InetAddress.getByName(serverIP);	
		
					ConnectResponse connectResponse = new ConnectResponse();
					
					connectResponse.setTransactionId(transactionid);
					connectResponse.setConnectionId(newConnectionId); //antes con newconnectionId
					
					//#############################
					byte[] requestBytes = connectResponse.getBytes();			
					DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length, serverHost, serverPort);
					
					udpSocket.send(packet);
					System.out.println("IP Address:- " + InetAddress.getLocalHost());
					System.out.println(" - Sent from server response to '" + connectResponse.getConnectionId() + "::::::::::" + packet.getPort() + "' -> " + new String(packet.getData()) + " [" + packet.getLength() + " byte(s)]");

					//###########################


					
					
					} catch (SocketException | UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}else {
				
				
				System.out.println("Entra en el else");
				for(Peer p:listaPeers) {
				    String address="/";
				    address=address+p.getIP();
				    InetAddress add=reply.getAddress();
				    String aadd=add.toString();
					System.out.println("Adress=="+address);
					System.out.println("Aadd=="+aadd);
				    if(address.equals(aadd)){
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
	
							p.setConnectionIdPrincipal(newConnectionId);
							System.out.println(p.getConnectionIdPrincipal());
							
							System.out.println("UPDATEANDO");
							SQLiteDBManager.updateIdAndTr(reply.getAddress().toString().substring(1),p.getConnectionIdPrincipal(),p.getConnectionIdSecundario(),p.getTransactionId(),System.currentTimeMillis());
	
							
							//Mensaje de vuelta
							//###########################
//								String serverIP = this.getIP();
//								int serverPort = this.getPuerto().get(0);
							
							String serverIP = reply.getAddress().toString().substring(1); 
							int serverPort = 7001;
	
							
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
								
							
	
							
							
							} catch (IOException e) {
								e.printStackTrace();
							}
							
							//break;
						}else {
							//TODO enviar error
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
