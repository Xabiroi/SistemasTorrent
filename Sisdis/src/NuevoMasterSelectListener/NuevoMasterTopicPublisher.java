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

import Controllers.DataController.EstadosBaseDeDatos;
import Controllers.DataController.EstadosEleccionMaster;
import Mensajes.NuevoMaster;
import Objetos.Tracker;

public class NuevoMasterTopicPublisher extends Thread{
	private ArrayList<Tracker> trackers;
	private Tracker miTracker;
	private ArrayList<EstadosEleccionMaster> estadoActual;
	private ArrayList<Boolean> cambio;
	
	String connectionFactoryName = "TopicConnectionFactory";
	String topicJNDIName = "jndi.ssdd.nuevomaster"; 			//This name is defined in jndi.properties file		
	
	TopicConnection topicConnection = null;
	TopicSession topicSession = null;
	TopicPublisher topicPublisher = null;	
	
	public NuevoMasterTopicPublisher(ArrayList<Tracker> trackers, Tracker miTracker, ArrayList<EstadosEleccionMaster> estadoActual, ArrayList<Boolean> cambio) {
		super();
		this.trackers = trackers;
		this.miTracker = miTracker;
		this.estadoActual = estadoActual;
		this.cambio = cambio;
	}
	
	public void elegirNuevoMaster() {	
		
		try {
			//JNDI Initial Context
			Context ctx = new InitialContext();
		
			//Connection Factory
			TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ctx.lookup(connectionFactoryName);
			
			//Message Destination
			Topic myTopic = (Topic) ctx.lookup(topicJNDIName);
			
			//Connection			
			topicConnection = topicConnectionFactory.createTopicConnection();
//			System.out.println("- NuevoMaster Publisher Topic Connection created!");
			
			//Session
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
//			System.out.println("- NuevoMaster Publisher Topic Session created!");

			//Message Publisher
			topicPublisher = topicSession.createPublisher(myTopic);
			System.out.println("ESTAMOS DECIDIENDO!!\n\n\n");
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
			
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}	
			
		} catch (Exception e) {
			System.err.println("# NuevoMaster Publisher TopicPublisherTest Error: " + e.getMessage());
		} finally {
			try {
				//Close resources
				topicPublisher.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- NuevoMaster Publisher Topic resources closed!");				
			} catch (Exception ex) {
				System.err.println("# NuevoMaster Publisher TopicPublisherTest Error: " + ex.getMessage());
			}			
		}
	}
	
	
	
	public ArrayList<EstadosEleccionMaster> getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(ArrayList<EstadosEleccionMaster> estadosEleccionMasters) {
		this.estadoActual = estadosEleccionMasters;
	}

	public static void main(String[] args) {
		System.out.println("Main de topic publisher");
	}
}
