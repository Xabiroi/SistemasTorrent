package KeepAliveTopicListeners;

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

import Mensajes.Keepalive;
import Objetos.Tracker;

public class KeepaliveTopicPublisher extends Thread{
	ArrayList<Tracker> trackers;
	Tracker miTracker;
	
	String connectionFactoryName = "TopicConnectionFactory";
	String topicJNDIName = "jndi.ssdd.keepalive";		//This name is defined in jndi.properties file
	
	TopicConnection topicConnection = null;
	TopicSession topicSession = null;
	TopicPublisher topicPublisher = null;	
	
	public KeepaliveTopicPublisher(ArrayList<Tracker> trackers, Tracker miTracker) {
		super();
		this.trackers = trackers;
		this.miTracker = miTracker;
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
			System.out.println("- KeepAlive Topic Connection created!");
			
			//Session
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- KeepAlive Topic Session created!");

			//Message Publisher
			topicPublisher = topicSession.createPublisher(myTopic);
			System.out.println("- KeepAlive TopicPublisher created!");
			
			
			//Bucle infinito
			boolean loop=true;
			int loop1=1;
			//cambio a un numero limitado para comprobar que desconecta al usuario
			while(loop1<10) {
				System.out.println("Espera de 1 segundo antes de enviar el keepalive");
				//Object Message
				ObjectMessage objectMessage = topicSession.createObjectMessage();
				
				//La creacion de keepalives con asignacion de id, no 1 (getid del tracker)
				objectMessage.setObject(new Keepalive(miTracker.getId(), System.currentTimeMillis(), true));
				
				objectMessage.setJMSType("ObjectMessage");
				objectMessage.setJMSMessageID("ID-1");
				objectMessage.setJMSPriority(1);		
				
				topicPublisher.publish(objectMessage);
				//Publish the Message
				System.out.println("- KeepAlive Object published in the Topic!");
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						loop=false;
						e.printStackTrace();
					}	
					
				loop1++;
			}
			
//			//FIXME Prueba de keepalive fuera de tiempo
//			////////////////////////////////////////////////////
//			Thread.sleep(1600);
//			ObjectMessage objectMessage = topicSession.createObjectMessage();
//			
//			//La creacion de keepalives con asignacion de id, no 1 (getid del tracker)
//			objectMessage.setObject(new Keepalive(1, System.currentTimeMillis(), true));
//			
//			objectMessage.setJMSType("ObjectMessage");
//			objectMessage.setJMSMessageID("ID-1");
//			objectMessage.setJMSPriority(1);		
//			
//			topicPublisher.publish(objectMessage);
//			////////////////////////////////////////////////////

		} catch (Exception e) {
			System.err.println("# KeepAlive TopicPublisherTest Error: " + e.getMessage());
		} finally {
			try {
				//Close resources
				topicPublisher.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- KeepAlive Topic resources closed!");				
			} catch (Exception ex) {
				System.err.println("# KeepAlive TopicPublisherTest Error: " + ex.getMessage());
			}			
		}
	}
	
	
	
	public static void main(String[] args) {
		System.out.println("Main de topic publisher");
	}
}
