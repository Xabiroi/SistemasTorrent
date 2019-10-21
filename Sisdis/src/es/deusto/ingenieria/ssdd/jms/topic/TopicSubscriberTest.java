package es.deusto.ingenieria.ssdd.jms.topic;

import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;

public class TopicSubscriberTest {	
	
	public static void main(String[] args) {
		String connectionFactoryName = "TopicConnectionFactory";
		//This name is defined in jndi.properties file
		String topicJNDIName = "jndi.ssdd.topic";
		//ID for a durable connection (optional)
		String subscriberID = "SubscriberID";
		
		TopicConnection topicConnection = null;
		TopicSession topicSession = null;
		TopicSubscriber topicDurableSubscriber = null;
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

			//Define a durable subscriber using a filter (the filter is optional)
			topicDurableSubscriber = topicSession.createDurableSubscriber(myTopic, subscriberID, "Filter = '1'", false);
			//Define a non-durable connection using a filter (the filter is optional)
			topicNONDurableSubscriber = topicSession.createSubscriber(myTopic, "Filter = '2'", false);
			
			//Topic Listener
			TopicListener topicListener = new TopicListener();
			
			//Set the message listener for the durable subscriber
			topicDurableSubscriber.setMessageListener(topicListener);
			//Set the same message listener for the non-durable subscriber
			topicNONDurableSubscriber.setMessageListener(topicListener);
			
			//Begin message delivery
			topicConnection.start();
			
			//Wait 10 seconds for messages. After that period the program stops.
			System.out.println("- Waiting 10 seconds for messages...");
			Thread.sleep(10000);			
		} catch (Exception e) {
			System.err.println("# TopicSubscriberTest Error: " + e.getMessage());			
		} finally {
			try {
				//Close resources
				topicDurableSubscriber.close();
				topicNONDurableSubscriber.close();
				//Unsubscribe the durable subscriber to release resources
				topicSession.unsubscribe(subscriberID);
				topicSession.close();
				topicConnection.close();
				System.out.println("- Topic resources closed!");				
			} catch (Exception ex) {
				System.err.println("# TopicSubscriberTest Error: " + ex.getMessage());
			}
		}
	}
}