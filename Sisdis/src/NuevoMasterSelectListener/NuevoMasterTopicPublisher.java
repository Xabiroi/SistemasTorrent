package NuevoMasterSelectListener;

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

import Mensajes.NuevoMaster;
import Objetos.Tracker;

public class NuevoMasterTopicPublisher extends Thread{
	private ArrayList<Tracker> trackers;
	private Tracker miTracker;
	
	String connectionFactoryName = "TopicConnectionFactory";
	//This name is defined in jndi.properties file
	String topicJNDIName = "jndi.ssdd.NuevoMaster";		
	
	TopicConnection topicConnection = null;
	TopicSession topicSession = null;
	TopicPublisher topicPublisher = null;	
	
	public NuevoMasterTopicPublisher(ArrayList<Tracker> trackers, Tracker miTracker) {
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
			System.out.println("- Topic Connection created!");
			
			//Session
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- Topic Session created!");

			//Message Publisher
			topicPublisher = topicSession.createPublisher(myTopic);
			System.out.println("- TopicPublisher created!");
			
			
			//TODO Comprobar ID más bajo en la lista "trackers" y enviarlo en un mensaje
			int idMasBajo = miTracker.getId();
			for(Tracker tracker : trackers) {
				if(tracker.getId() < idMasBajo)
					idMasBajo = tracker.getId();
			}
			
			ObjectMessage objectMessage = topicSession.createObjectMessage();
			
			objectMessage.setObject(new NuevoMaster(idMasBajo));
			
			objectMessage.setJMSType("ObjectMessage");
			objectMessage.setJMSMessageID("ID-1");
			objectMessage.setJMSPriority(1);		
			
			topicPublisher.publish(objectMessage);
			

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
		System.out.println("Main de topic publisher");
	}
}
