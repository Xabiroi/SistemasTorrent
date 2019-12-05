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
	private ArrayList<Boolean> desconexion=new ArrayList<Boolean>(1);
	ArrayList<Tracker> trackers;
	Tracker miTracker;
	
	String connectionFactoryName = "TopicConnectionFactory";
	String topicJNDIName = "jndi.ssdd.keepalive";		//This name is defined in jndi.properties file
	
	TopicConnection topicConnection = null;
	TopicSession topicSession = null;
	TopicPublisher topicPublisher = null;	
	
	public KeepaliveTopicPublisher(ArrayList<Tracker> trackers, Tracker miTracker,ArrayList<Boolean> desconexion) {
		super();
		this.trackers = trackers;
		this.miTracker = miTracker;
		this.desconexion = desconexion;
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

			//cambio a un numero limitado para comprobar que desconecta al usuario
			while(desconexion.get(0)) {
				//System.out.println("Espera de 1 segundo antes de enviar el keepalive");
				//Object Message
				ObjectMessage objectMessage = topicSession.createObjectMessage();
				
				//FIXME La creacion de keepalives con asignacion de id
				//PRUEBA
				objectMessage.setObject(new Keepalive(miTracker.getId(), System.currentTimeMillis(), miTracker.isMaster()));
				//ORIGINAL
				//objectMessage.setObject(new Keepalive(miTracker.getId(), System.currentTimeMillis(), true));
				
				objectMessage.setJMSType("ObjectMessage");
				objectMessage.setJMSMessageID("ID-1");
				objectMessage.setJMSPriority(1);		
				
				topicPublisher.publish(objectMessage);
				//Publish the Message
				//System.out.println("- KeepAlive Object published in the Topic!");
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}	
			}
			

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
	

	public ArrayList<Boolean> getDesconexion() {
		return desconexion;
	}

	public void setDesconexion(ArrayList<Boolean> desconexion) {
		this.desconexion = desconexion;
	}
}
