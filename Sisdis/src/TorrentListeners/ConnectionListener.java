package TorrentListeners;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Random;

import Objetos.Peer;
import bitTorrent.tracker.protocol.udp.ConnectRequest;

public class ConnectionListener {
	

	private ArrayList<Peer> peersTransactionId;
	
	
	
	
	public ConnectionListener(ArrayList<Peer> peersTransactionId) {
		super();
		this.peersTransactionId = peersTransactionId;
	}




	public void receive(DatagramPacket reply) {

	    ConnectRequest cr = ConnectRequest.parse(reply.getData());
	    System.out.println("CR="+cr.getAction()); //CONNECT=0
	    System.out.println("CR="+cr.getTransactionId()); 
		System.out.println("CR="+cr.getConnectionId()); 
		
		//VALIDATE
		if(reply.getLength()==16) {
			//for de buscar transaction id con el peer
			for(Peer p:peersTransactionId) {
				//si la transactionId es buena
				if(p.getTransactionId()==cr.getTransactionId()) {
					
					//mirar si el connection id es el default u otro, sino adjuntarle otro
					if(cr.getConnectionId()==Long.decode("0x41727101980")) {
						System.out.println("ConnectRequest primero valido");
						Random r=new Random();
						int randomInteger = r.nextInt(Integer.MAX_VALUE);

						p.setConnectionIdPrincipal(randomInteger);

						//TODO enviar mensaje de vuelta
						

						break;
						
					}else {
						//TODO comprobar que el connectionId es bueno (anterior y posterior)
						p.setConnectionIdSecundario(p.getConnectionIdPrincipal());
						
						Random r=new Random();
						int randomInteger = r.nextInt(Integer.MAX_VALUE);

						p.setConnectionIdPrincipal(randomInteger);

						
						//TODO enviar mensaje de vuelta
						

						break;
					}
				}
			}
		}
	
	
	
	}
}
