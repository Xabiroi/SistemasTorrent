package DesconexionTopicListeners;

import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;

import Controllers.DataController.EstadosEleccionMaster;
import Mensajes.Desconexion;
import Objetos.Tracker;

public class DesconexionTopicPublisher extends Thread{	
	String connectionFactoryName = "TopicConnectionFactory";
	//This name is defined in jndi.properties file
	String topicJNDIName = "jndi.ssdd.desconexion";		
	
	TopicConnection topicConnection = null;
	TopicSession topicSession = null;
	TopicPublisher topicPublisher = null;	
	
	private EstadosEleccionMaster estadoActual;
	private Tracker miTracker;
	

	public DesconexionTopicPublisher(EstadosEleccionMaster estadoActual, Tracker miTracker) {
		super();
		this.estadoActual = estadoActual;
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
			
			ObjectMessage objectMessage = topicSession.createObjectMessage();
		
			objectMessage.setObject(new Desconexion(miTracker.getId()));
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
	}

	public EstadosEleccionMaster getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(EstadosEleccionMaster estadoActual) {
		this.estadoActual = estadoActual;
	}






}
