package BDUpdateTopicListener;

import java.util.ArrayList;

import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;

import Controllers.DataController.EstadosBaseDeDatos;
import Objetos.Tracker;

public class BDTopicSubscriber extends Thread{	
	
	private int ContadorVersionBD;
	private ArrayList<EstadosBaseDeDatos> estadoActual;
	private ArrayList<Tracker> TrackersRedundantes;


	


	public BDTopicSubscriber(int contadorVersionBD, ArrayList<EstadosBaseDeDatos> estadoActual,
			ArrayList<Tracker> trackersRedundantes) {
		super();
		ContadorVersionBD = contadorVersionBD;
		this.estadoActual = estadoActual;
		TrackersRedundantes = trackersRedundantes;
	}



	String connectionFactoryName = "TopicConnectionFactory";
	//This name is defined in jndi.properties file
	String topicJNDIName = "jndi.ssdd.bdupdate";//FIXME cambiar topics
	TopicConnection topicConnection = null;
	TopicSession topicSession = null;
	TopicSubscriber topicNONDurableSubscriber = null;

	public void run() {

				
		try {
			Context ctx = new InitialContext();
		
			//Connection Factories
			TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ctx.lookup(connectionFactoryName);
			
			//Message Destinations
			Topic myTopic = (Topic) ctx.lookup(topicJNDIName);		
			
			//Connections			
			topicConnection = topicConnectionFactory.createTopicConnection();
			
			//Set an ID to create a durable connection (optional)
			topicConnection.setClientID("SSDDTopic.BDUpdate");
			
			System.out.println("- Topic Connection created!");
			
			//Sessions
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("-BD Topic Session created!");

			//Define a non-durable connection using a filter (the filter is optional)
			topicNONDurableSubscriber = topicSession.createSubscriber(myTopic);
			
			//Topic Listener
			
			BDTopicListener topicListener = new BDTopicListener(ContadorVersionBD,estadoActual,TrackersRedundantes);
			
			//Set the same message listener for the non-durable subscriber
			topicNONDurableSubscriber.setMessageListener(topicListener);
			
			//Begin message delivery
			topicConnection.start();
			
			//Bucle infinito
			boolean loop=true;
			//FIXME Comprobacion evitando el bucle infinito
			int loop1=1;
			while(loop1<100) {
				//Comprobacion de que funciona, habria que habilitar handlers de excepciones para detenerlo como cambio de master y otros
					System.out.println("- Waiting 0.5 seconds for updates...");
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						loop=false;
						e.printStackTrace();
					}	
					loop1++;//FIXME
			}
			
		
		} catch (Exception e) {
			System.err.println("# BD TopicSubscriberTest Error: " + e.getMessage());			
		} finally {

			try {
				//Close resources
				topicNONDurableSubscriber.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- BD Topic resources closed!");				
			} catch (Exception ex) {
				System.err.println("# BD TopicSubscriberTest Error: " + ex.getMessage());
			}
		}

	}
	
	
	public static void main(String[] args) {

	}


}

