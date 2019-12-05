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
	
	private ArrayList<Integer> ContadorVersionBD;
	private ArrayList<EstadosBaseDeDatos> estadoActual;
	private ArrayList<Tracker> TrackersRedundantes;
	private ArrayList<Boolean> desconexion=new ArrayList<Boolean>(1);

	


	public BDTopicSubscriber(ArrayList<Integer> contadorVersionBD, ArrayList<EstadosBaseDeDatos> estadoActual,
			ArrayList<Tracker> trackersRedundantes,ArrayList<Boolean> desconexion) {
		super();
		ContadorVersionBD = contadorVersionBD;
		this.estadoActual = estadoActual;
		TrackersRedundantes = trackersRedundantes;
		this.setDesconexion(desconexion);
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
//			topicConnection.setClientID("SSDDTopic.BDUpdate");
			
			System.out.println("- BD Subscriber Topic Connection created!");
			
			//Sessions
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("-BD Subscriber Topic Session created!");

			//Define a non-durable connection using a filter (the filter is optional)
			topicNONDurableSubscriber = topicSession.createSubscriber(myTopic);
			
			//Topic Listener
			
			BDTopicListener topicListener = new BDTopicListener(ContadorVersionBD,estadoActual,TrackersRedundantes);
			
			//Set the same message listener for the non-durable subscriber
			topicNONDurableSubscriber.setMessageListener(topicListener);
			
			//Begin message delivery
			topicConnection.start();
			
			//Bucle infinito

			while(desconexion.get(0)) {
				//Comprobacion de que funciona, habria que habilitar handlers de excepciones para detenerlo como cambio de master y otros
					//System.out.println("- Waiting 0.5 seconds for updates...");
					try {
						Thread.sleep(500);
					} catch (Exception e) {

						e.printStackTrace();
					}	

			}
			
		
		} catch (Exception e) {
			System.err.println("# BD Subscriber TopicTest Error: " + e.getMessage());			
		} finally {

			try {
				//Close resources
				topicNONDurableSubscriber.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- BD Subscriber Topic resources closed!");				
			} catch (Exception ex) {
				System.err.println("# BD Subscriber TopicSubscriberTest Error: " + ex.getMessage());
			}
		}

	}
	
	
	public static void main(String[] args) {

	}


	public ArrayList<Boolean> getDesconexion() {
		return desconexion;
	}


	public void setDesconexion(ArrayList<Boolean> desconexion) {
		this.desconexion = desconexion;
	}


}

