package es.deusto.ingenieria.ssdd.jms.topic;

import java.util.Calendar;

import javax.jms.MapMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;

public class TopicPublisherTest {
	
	public static void main(String[] args) {	
		String connectionFactoryName = "TopicConnectionFactory";
		//This name is defined in jndi.properties file
		String topicJNDIName = "jndi.ssdd.topic";		
		
		TopicConnection topicConnection = null;
		TopicSession topicSession = null;
		TopicPublisher topicPublisher = null;		
		
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
			
			//Text Message
			TextMessage textMessage = topicSession.createTextMessage();
			//Message Headers (optional - defined by JMS)
			textMessage.setJMSType("TextMessage");
			textMessage.setJMSMessageID("ID-1");
			textMessage.setJMSPriority(1);
			//Message Properties (optional - defined by the application)
			textMessage.setStringProperty("Filter", "1");			
			//Message Body
			textMessage.setText("Hello World!!");

			//Publish the Message
			topicPublisher.publish(textMessage);
			System.out.println("- TextMessage published in the Topic!");
			
			//Map Message
			MapMessage mapMessage = topicSession.createMapMessage();
			//Message Headers (optional - defined by JMS)
			mapMessage.setJMSType("MapMessage");
			mapMessage.setJMSMessageID("ID-1");
			mapMessage.setJMSPriority(2);
			//Message Properties (optional - defined by the application)
			mapMessage.setStringProperty("Filter", "2");			
			//Message Body
			mapMessage.setString("Text", "Hello World!");
			mapMessage.setLong("Timestamp", Calendar.getInstance().getTimeInMillis());
			mapMessage.setBoolean("ACK_required", true);
			
			//Publish the Message
			topicPublisher.publish(mapMessage);
			System.out.println("- MapMessage sent to the Topic!");
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
}
