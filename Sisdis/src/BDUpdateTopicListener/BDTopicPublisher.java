package BDUpdateTopicListener;

import java.util.ArrayList;

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
import Objetos.Swarm;

public class BDTopicPublisher extends Thread{	
	String connectionFactoryName = "TopicConnectionFactory";
	//This name is defined in jndi.properties file
	String topicJNDIName = "jndi.ssdd.bdupdate";		
	
	TopicConnection topicConnection = null;
	TopicSession topicSession = null;
	TopicPublisher topicPublisher = null;	
	
	private int ContadorVersionBD;
	private EstadosBaseDeDatos estadoActual;
	private ArrayList<Swarm> swarms;
	private static boolean cambio;
	

	public BDTopicPublisher(int contadorVersionBD, EstadosBaseDeDatos estadoActual, ArrayList<Swarm> swarms, boolean cambio) {
		super();
		ContadorVersionBD = contadorVersionBD;
		this.estadoActual = estadoActual;
		this.swarms = swarms;
		BDTopicPublisher.cambio = cambio;
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
			System.out.println("- Topic Connection created!");
			
			//Session
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- Topic Session created!");

			//Message Publisher
			topicPublisher = topicSession.createPublisher(myTopic);
			System.out.println("- TopicPublisher created!");
			
			
			switch(estadoActual) {
			  case Esperando:
				  while(!cambio) {
					  System.err.println("Bucle cambio="+cambio);
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}	
				  }
					  setEstadoActual(EstadosBaseDeDatos.Sugerencia);
				break;
			  case Sugerencia:
				  
					ObjectMessage objectMessage = topicSession.createObjectMessage();
					
					//FIXME no hardcodear la ip, obtener de la cola donde se procesan los peers
					objectMessage.setObject(new SugerenciaActualizacion("192.168.1.56"));
					
					objectMessage.setJMSType("ObjectMessage");
					objectMessage.setJMSMessageID("ID-1");
					objectMessage.setJMSPriority(1);		
					
					topicPublisher.publish(objectMessage);
					//Publish the Message
					System.out.println("- Sugerencia published in the Topic!");
					
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
						e.printStackTrace();
					}	
				  
			    // code block
			    break;
			  case Preparacion:
				  // code block
				  
					ObjectMessage objectMessage2 = topicSession.createObjectMessage();
					objectMessage2.setObject(new PreparacionActualizacion());
					
					objectMessage2.setJMSType("ObjectMessage");
					objectMessage2.setJMSMessageID("ID-1");
					objectMessage2.setJMSPriority(1);		
					
					topicPublisher.publish(objectMessage2);
					//Publish the Message
					System.out.println("- Object published in the Topic!");
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
						e.printStackTrace();
					}	
				  
			    break;
			  case Actualizacion:
				// code block
				  
					ObjectMessage objectMessage3 = topicSession.createObjectMessage();
					
					//obtener id de la version de bd
					objectMessage3.setObject(new ActualizacionBD(ContadorVersionBD));
					
					objectMessage3.setJMSType("ObjectMessage");
					objectMessage3.setJMSMessageID("ID-1");
					objectMessage3.setJMSPriority(1);		
					
					topicPublisher.publish(objectMessage3);
					//Publish the Message
					System.out.println("- Object published in the Topic!");
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
						e.printStackTrace();
					}	
				  

				  break;
			  default:
			    // code block
			}
			


			

		} catch (Exception e) {
			System.err.println("# TopicPublisherTest Error: " + e.getMessage());
		} finally {
			try {
				//Close resources
				topicPublisher.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- Topic resources closed!");				
			} catch (Exception ex) {
				System.err.println("# TopicPublisherTest Error: " + ex.getMessage());
			}			
		}
		
		
	}
	
	

	public static void main(String[] args) {
	}



	public EstadosBaseDeDatos getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(EstadosBaseDeDatos estadoActual) {
		this.estadoActual = estadoActual;
	}



	public int getContadorVersionBD() {
		return ContadorVersionBD;
	}



	public void setContadorVersionBD(int contadorVersionBD) {
		ContadorVersionBD = contadorVersionBD;
	}



	public ArrayList<Swarm> getSwarms() {
		return swarms;
	}



	public void setSwarms(ArrayList<Swarm> swarms) {
		this.swarms = swarms;
	}



}
