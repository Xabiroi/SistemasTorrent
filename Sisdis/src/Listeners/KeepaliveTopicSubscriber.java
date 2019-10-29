package Listeners;

import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;

public class KeepaliveTopicSubscriber extends Thread{	
	

	public void run() {
		String connectionFactoryName = "TopicConnectionFactory";
		//This name is defined in jndi.properties file
		String topicJNDIName = "jndi.ssdd.topic";		
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
			topicConnection.setClientID("SSDD_TopicSubscriber");
			System.out.println("- Topic Connection created!");
			
			//Sessions
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- Topic Session created!");

			//Define a non-durable connection using a filter (the filter is optional)
			topicNONDurableSubscriber = topicSession.createSubscriber(myTopic);
			
			//Topic Listener
			KeepaliveListener topicListener = new KeepaliveListener();
			
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
					System.out.println("- Waiting 0.5 seconds for messages...");
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						loop=false;
						e.printStackTrace();
					}	
					loop1++;//FIXME
			}
			
		
		} catch (Exception e) {
			System.err.println("# TopicSubscriberTest Error: " + e.getMessage());			
		} finally {

			try {
				//Close resources
				topicNONDurableSubscriber.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- Topic resources closed!");				
			} catch (Exception ex) {
				System.err.println("# TopicSubscriberTest Error: " + ex.getMessage());
			}
		}

	
		
		
	}
	
	
	public static void main(String[] args) {
		//Los try catch no funcionan bien hasta implementar gestion de excepciones con las funciones
		
		
	//Wait 10 seconds for messages. After that period the program stops.
		KeepaliveTopicSubscriber topicSubscriberTest=new KeepaliveTopicSubscriber();
		try {	
			topicSubscriberTest.start();
		}finally {
			try {
				topicSubscriberTest.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			topicSubscriberTest.stop();

		}
		

		
	}
}

