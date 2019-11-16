package KeepAliveTopicListeners;

import java.util.ArrayList;

import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;

import BDFileQueueListener.QueueFileReceiver;
import BDFileQueueListener.QueueFileSender;
import Objetos.Tracker;

public class KeepaliveTopicSubscriber extends Thread{	
	private ArrayList<Tracker> trackers=new ArrayList<Tracker>();
	private Tracker miTracker;
	private QueueFileSender enviadorBD;
	private QueueFileReceiver recibidorBD;
	



	public KeepaliveTopicSubscriber(ArrayList<Tracker> trackers, Tracker miTracker, QueueFileSender enviadorBD,
			QueueFileReceiver recibidorBD) {
		super();
		this.trackers = trackers;
		this.miTracker = miTracker;
		this.enviadorBD = enviadorBD;
		this.recibidorBD = recibidorBD;
	}


	public void run() {
		String connectionFactoryName = "TopicConnectionFactory";
		//This name is defined in jndi.properties file
		String topicJNDIName = "jndi.ssdd.keepalive";		
		TopicConnection topicConnection = null;
		TopicSession topicSession = null;
		TopicSubscriber topicNONDurableSubscriber = null;
				
		try {
			Context ctx = new InitialContext();
		
			//Connection Factories
			TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ctx.lookup(connectionFactoryName);
			
			//Message Destinations
			Topic myTopic = (Topic) ctx.lookup(topicJNDIName);		
			
			//Connections			
			topicConnection = topicConnectionFactory.createTopicConnection();
			
			//Set an ID to create a durable connection (optional)
//			topicConnection.setClientID("SSDDTopic.Keepalive");
			System.out.println("- KeepAlive Topic Connection created!");
			
			//Sessions
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- KeepAlive Topic Session created!");

			//Define a non-durable connection using a filter (the filter is optional)
			topicNONDurableSubscriber = topicSession.createSubscriber(myTopic);
			
			//Topic Listener
			KeepaliveListener topicListener = new KeepaliveListener(trackers, miTracker,enviadorBD,recibidorBD);
			
			//Set the same message listener for the non-durable subscriber
			topicNONDurableSubscriber.setMessageListener(topicListener);
			
			//Begin message delivery
			topicConnection.start();
			
			//Bucle infinito
			boolean loop=true;
			//FIXME Comprobacion evitando el bucle infinito
			int loop1=1;
			while(loop1<60) {
				//Comprobacion de que funciona, habria que habilitar handlers de excepciones para detenerlo como cambio de master y otros
					//System.out.println("- Waiting 0.5 seconds for messages...");
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						loop=false;
						e.printStackTrace();
					}	
					loop1++;
			}
			
		
		} catch (Exception e) {
			System.err.println("# KeepAlive TopicSubscriberTest Error: " + e.getMessage());	
			e.printStackTrace();
		} finally {

			try {
				//Close resources
				topicNONDurableSubscriber.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- KeepAlive Topic resources closed!");				
			} catch (Exception ex) {
				System.err.println("# KeepAlive TopicSubscriberTest Error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}

	
		
		
	}
	
	
	public static void main(String[] args) {
		//Los try catch no funcionan bien hasta implementar gestion de excepciones con las funciones
		
		System.out.println("EL MAIN DE KEEPALIVE_TOPIC_SUBSCRIBER");
//	//Wait 10 seconds for messages. After that period the program stops.
//		KeepaliveTopicSubscriber topicSubscriberTest=new KeepaliveTopicSubscriber();
//		try {	
//			topicSubscriberTest.start();
//		}finally {
//			try {
//				topicSubscriberTest.join();
//			} catch (InterruptedException e) {
//				
//				e.printStackTrace();
//			}
//			topicSubscriberTest.stop();
//
//		}
//		

		
	}


	public QueueFileSender getEnviadorBD() {
		return enviadorBD;
	}


	public void setEnviadorBD(QueueFileSender enviadorBD) {
		this.enviadorBD = enviadorBD;
	}


	public QueueFileReceiver getRecibidorBD() {
		return recibidorBD;
	}


	public void setRecibidorBD(QueueFileReceiver recibidorBD) {
		this.recibidorBD = recibidorBD;
	}
}

