package BDUpdateTopicListener;

import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;

import Mensajes.ActualizacionBD;
import Mensajes.PreparacionActualizacion;
import Mensajes.SugerenciaActualizacion;

public class BDTopicPublisher extends Thread{	
	String connectionFactoryName = "TopicConnectionFactory";
	//This name is defined in jndi.properties file
	String topicJNDIName = "jndi.ssdd.bdupdate";		
	
	TopicConnection topicConnection = null;
	TopicSession topicSession = null;
	TopicPublisher topicPublisher = null;	
	
	private String estadoActual;
	
	public BDTopicPublisher(String estadoActual) {
		super();
		this.estadoActual = estadoActual;
	}


	public enum estado{
		Sugerencia,
		Preparacion,
		Actualizacion
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
			  case "Sugerencia":
					ObjectMessage objectMessage = topicSession.createObjectMessage();
					
					//La creacion de keepalives con asignacion de id, no 1 (getid del tracker)
					objectMessage.setObject(new SugerenciaActualizacion("192.168.1.56"));
					
					objectMessage.setJMSType("ObjectMessage");
					objectMessage.setJMSMessageID("ID-1");
					objectMessage.setJMSPriority(1);		
					
					topicPublisher.publish(objectMessage);
					//Publish the Message
					System.out.println("- Object published in the Topic!");
				  
			    // code block
			    break;
			  case "Preparacion":
				  // code block
				  
					ObjectMessage objectMessage2 = topicSession.createObjectMessage();
					

					objectMessage2.setObject(new PreparacionActualizacion());
					
					objectMessage2.setJMSType("ObjectMessage");
					objectMessage2.setJMSMessageID("ID-1");
					objectMessage2.setJMSPriority(1);		
					
					topicPublisher.publish(objectMessage2);
					//Publish the Message
					System.out.println("- Object published in the Topic!");
				  
			    break;
			  case "Actualizacion":
				// code block
				  
					ObjectMessage objectMessage3 = topicSession.createObjectMessage();
					
					//obtener id de la version de bd
					objectMessage3.setObject(new ActualizacionBD());
					
					objectMessage3.setJMSType("ObjectMessage");
					objectMessage3.setJMSMessageID("ID-1");
					objectMessage3.setJMSPriority(1);		
					
					topicPublisher.publish(objectMessage3);
					//Publish the Message
					System.out.println("- Object published in the Topic!");
				  

				  break;
			  default:
			    // code block
			}
			
			//FIXME Cambiar esto
			//Bucle infinito
			boolean loop=true;
			int loop1=1;
			//cambio a un numero limitado para comprobar que desconecta al usuario
			while(loop1<10) {
				System.out.println("Espera de 1 segundo antes de enviar el keepalive");
				//Object Message
				ObjectMessage objectMessage = topicSession.createObjectMessage();
				
				//La creacion de keepalives con asignacion de id, no 1 (getid del tracker)
				
				objectMessage.setJMSType("ObjectMessage");
				objectMessage.setJMSMessageID("ID-1");
				objectMessage.setJMSPriority(1);		
				
				topicPublisher.publish(objectMessage);
				//Publish the Message
				System.out.println("- Object published in the Topic!");
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						loop=false;
						e.printStackTrace();
					}	
					
				loop1++;
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







	public String getEstadoActual() {
		return estadoActual;
	}







	public void setEstadoActual(String estadoActual) {
		this.estadoActual = estadoActual;
	}


}
