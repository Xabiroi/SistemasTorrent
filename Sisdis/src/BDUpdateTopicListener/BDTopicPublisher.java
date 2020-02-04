package BDUpdateTopicListener;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;

import Controllers.DataController.EstadosBaseDeDatos;
import Mensajes.ActualizacionBD;
import Mensajes.PreparacionActualizacion;
import Mensajes.SugerenciaActualizacion;
import Objetos.Peer;
import Objetos.Swarm;

public class BDTopicPublisher extends Thread{	
	String connectionFactoryName = "TopicConnectionFactory";
	//This name is defined in jndi.properties file
	String topicJNDIName = "jndi.ssdd.bdupdate";		
	
	TopicConnection topicConnection = null;
	TopicSession topicSession = null;
	TopicPublisher topicPublisher = null;	
	
	private ArrayList<Integer> ContadorVersionBD;
	private ArrayList<EstadosBaseDeDatos> estadoActual;
	private ArrayList<Swarm> swarms;
	private ArrayList<Boolean> cambio;
	private LinkedList<Peer> PeersEnCola;
	private ArrayList<Boolean> desconexion=new ArrayList<Boolean>(1);
	
	public BDTopicPublisher(ArrayList<Integer> contadorVersionBD, ArrayList<EstadosBaseDeDatos> estadoActual, ArrayList<Swarm> swarms,
			ArrayList<Boolean> cambio, LinkedList<Peer> peersEnCola,ArrayList<Boolean> desconexion) {
		super();
		ContadorVersionBD = contadorVersionBD;
		this.estadoActual = estadoActual;
		this.swarms = swarms;
		this.cambio = cambio;
		PeersEnCola = peersEnCola;
		this.setDesconexion(desconexion);
	}
	
	public void run() {
		
		try {
			
			//JNDI Initial Context
			Context ctx = new InitialContext();
		
			//Connection Factory
			TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ctx.lookup(connectionFactoryName);
			
			//Message Destination
			Topic myTopic = (Topic) ctx.lookup(topicJNDIName);
			
			//Connection			
			topicConnection = topicConnectionFactory.createTopicConnection();
//			System.out.println("- BD Publisher Topic Connection created!");
			
			//Session
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
//			System.out.println("- BD Publisher Topic Session created!");

			//Message Publisher
			topicPublisher = topicSession.createPublisher(myTopic);
//			System.out.println("- BD Publisher TopicPublisher created!");
			

			//cambio a un numero limitado para comprobar que desconecta al usuario
			
			while(desconexion.get(0)) {
				if(cambio.get(0).booleanValue()==false) {}
				else {
				switch(estadoActual.get(0)) {
				  case Esperando:
	//				  System.out.println("Bucle cambio esperando=="+cambio.get(0).booleanValue());
					  while(!cambio.get(0).booleanValue()) {					  
							try {
								Thread.sleep(100);
							} catch (Exception e) {
								e.printStackTrace();
							}	
					  }
					  estadoActual.set(0,EstadosBaseDeDatos.Sugerencia);
					break;
				  case Sugerencia:
					  	System.out.println("¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡");
					  	System.out.println("Bucle cambio Sugerencia=="+cambio.get(0).booleanValue());
					  	System.out.println("¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡");
					  	if(cambio.get(0).booleanValue()==false) {}
					  	else {
							ObjectMessage objectMessage = topicSession.createObjectMessage();
							
							Peer peer = PeersEnCola.getFirst();
							objectMessage.setObject(new SugerenciaActualizacion(peer.getIP()));
							
							objectMessage.setJMSType("ObjectMessage");
							objectMessage.setJMSMessageID("ID-1");
							objectMessage.setJMSPriority(1);		
							
							topicPublisher.publish(objectMessage);
							//Publish the Message
		//					System.out.println("- Sugerencia published in the Topic!");
							
							try {
								Thread.sleep(1000);
							} catch (Exception e) {
								e.printStackTrace();
							}	
					  	}
				    break;
				  case Preparacion:
					  	System.out.println("¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡");
					  	System.out.println("Caso Preparacion");
					  	System.out.println("¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡");
						ObjectMessage objectMessage2 = topicSession.createObjectMessage();
						objectMessage2.setObject(new PreparacionActualizacion());
						
						objectMessage2.setJMSType("ObjectMessage");
						objectMessage2.setJMSMessageID("ID-1");
						objectMessage2.setJMSPriority(1);		
						
						topicPublisher.publish(objectMessage2);
	
	//					System.out.println("- Preparacion published in the Topic!");
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}	
					  
				    break;
				  case Actualizacion:
						  System.out.println("¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡");
						  System.out.println("ACTUALIZACION");
						  System.out.println("¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡");
						ObjectMessage objectMessage3 = topicSession.createObjectMessage();
						
						//obtener id de la version de bd
						objectMessage3.setObject(new ActualizacionBD(ContadorVersionBD.get(0)));
						
						objectMessage3.setJMSType("ObjectMessage");
						objectMessage3.setJMSMessageID("ID-1");
						objectMessage3.setJMSPriority(1);		
						
						topicPublisher.publish(objectMessage3);
						//Publish the Message
	//					System.out.println("- Actualizacion published in the Topic!");
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}	
					  
	
					  break;
				  default:
				    // code block
			}}
			} Thread.sleep(1000);
		}catch (Exception e) {
			System.err.println("# BD Publisher TopicTest Error: " + e.getMessage());
			}
			
			

//		} catch (Exception e) {
//			System.err.println("# BD Publisher TopicTest Error: " + e.getMessage());
//		}
//		finally {
//			try {
//				//Close resources
//				topicPublisher.close();
//				topicSession.close();
//				topicConnection.close();
//				System.out.println("- BD Publisher Topic resources closed!");				
//			} catch (Exception ex) {
//				System.err.println("# BD Publisher TopicTest Error: " + ex.getMessage());
//			}			
//		}
		
		
	}
	
	




	public static void main(String[] args) {
	}




	public ArrayList<EstadosBaseDeDatos> getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(ArrayList<EstadosBaseDeDatos> estadoActual) {
		this.estadoActual = estadoActual;
	}

	public ArrayList<Boolean> getCambio() {
		return cambio;
	}

	public void setCambio(ArrayList<Boolean> cambio) {
		this.cambio = cambio;
	}


	public ArrayList<Integer> getContadorVersionBD() {
		return ContadorVersionBD;
	}

	public void setContadorVersionBD(ArrayList<Integer> contadorVersionBD) {
		ContadorVersionBD = contadorVersionBD;
	}

	public ArrayList<Swarm> getSwarms() {
		return swarms;
	}



	public void setSwarms(ArrayList<Swarm> swarms) {
		this.swarms = swarms;
	}

	public LinkedList<Peer> getPeersEnCola() {
		return PeersEnCola;
	}

	public void setPeersEnCola(LinkedList<Peer> peersEnCola) {
		PeersEnCola = peersEnCola;
	}

	public ArrayList<Boolean> getDesconexion() {
		return desconexion;
	}

	public void setDesconexion(ArrayList<Boolean> desconexion) {
		this.desconexion = desconexion;
	}



}
