package NuevoMasterSelectListener;

import java.util.ArrayList;

import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;

import Controllers.DataController.EstadosEleccionMaster;
import Objetos.Tracker;

public class NuevoMasterTopicSubscriber extends Thread{	
	private ArrayList<Tracker> trackers=new ArrayList<Tracker>();
	private Tracker miTracker;
	private ArrayList<EstadosEleccionMaster> estadoActual;
	private ArrayList<Boolean> desconexion=new ArrayList<Boolean>(1);
	

	public NuevoMasterTopicSubscriber(ArrayList<Tracker> trackers, Tracker miTracker, ArrayList<EstadosEleccionMaster> estadoActual,ArrayList<Boolean> desconexion) {
		super();
		this.trackers = trackers;
		this.miTracker = miTracker;
		this.estadoActual = estadoActual;
		this.setDesconexion(desconexion);
	}


	public void run() {
		String connectionFactoryName = "TopicConnectionFactory";
		//This name is defined in jndi.properties file
		String topicJNDIName = "jndi.ssdd.nuevomaster";		
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
//			topicConnection.setClientID("SSDDTopic.NuevoMaster");
			System.out.println("- NuevoMaster Subscriber Topic Connection created!");
			
			//Sessions
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- NuevoMaster Subscriber Topic Session created!");

			//Define a non-durable connection using a filter (the filter is optional)
			topicNONDurableSubscriber = topicSession.createSubscriber(myTopic);
			
			//Topic Listener
			NuevoMasterListener topicListener = new NuevoMasterListener(trackers, miTracker, estadoActual);
			
			//Set the same message listener for the non-durable subscriber
			topicNONDurableSubscriber.setMessageListener(topicListener);
			
			//Begin message delivery
			topicConnection.start();
			
			//Bucle infinito
			while(desconexion.get(0)) {
				//Comprobacion de que funciona, habria que habilitar handlers de excepciones para detenerlo como cambio de master y otros
					System.out.println("- Waiting 0.5 seconds for messages...");
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
			
		
		} catch (Exception e) {
			System.err.println("# NuevoMaster Subscriber TopicSubscriberTest Error: " + e.getMessage());			
		} finally {

			try {
				//Close resources
				topicNONDurableSubscriber.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- NuevoMaster Subscriber Topic resources closed!");				
			} catch (Exception ex) {
				System.err.println("# NuevoMaster Subscriber TopicSubscriberTest Error: " + ex.getMessage());
			}
		}

	
		
		
	}
	
	
	public static void main(String[] args) {
		//Los try catch no funcionan bien hasta implementar gestion de excepciones con las funciones
		
		System.out.println("EL MAIN DE NUEVO_MASTER_TOPIC_SUBSCRIBER");
//	//Wait 10 seconds for messages. After that period the program stops.
//		KeepaliveTopicSubscriber topicSubscriberTest=new KeepaliveTopicSubscriber();
//		try {	
//			topicSubscriberTest.start();
//		}finally {
//			try {
//				topicSubscriberTest.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			topicSubscriberTest.stop();
//
//		}
//		

		
	}


	public ArrayList<Boolean> getDesconexion() {
		return desconexion;
	}


	public void setDesconexion(ArrayList<Boolean> desconexion) {
		this.desconexion = desconexion;
	}
}

