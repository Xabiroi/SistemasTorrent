package DesconexionTopicListeners;

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

import Controllers.DataController;
import Controllers.DataController.EstadosEleccionMaster;
import Controllers.RedundantController;
import Mensajes.Desconexion;
import NuevoMasterSelectListener.NuevoMasterTopicPublisher;
import Objetos.Tracker;

public class DesconexionTopicPublisher extends Thread{
	NuevoMasterTopicPublisher nuevoMasterTopicPublisher; 
	String connectionFactoryName = "TopicConnectionFactory";
	//This name is defined in jndi.properties file
	String topicJNDIName = "jndi.ssdd.desconexion";		
	
	TopicConnection topicConnection = null;
	TopicSession topicSession = null;
	TopicPublisher topicPublisher = null;	
	
	private EstadosEleccionMaster estadoActual;
	private Tracker miTracker;
	

	public DesconexionTopicPublisher(EstadosEleccionMaster estadoActual, Tracker miTracker, NuevoMasterTopicPublisher nuevoMasterTopicPublisher) {
		super();
		this.estadoActual = estadoActual;
		this.miTracker = miTracker;
		this.nuevoMasterTopicPublisher = nuevoMasterTopicPublisher;
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
			System.out.println("- Desconexion Topic Connection created!");
			
			//Session
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- Desconexion Topic Session created!");

			//Message Publisher
			topicPublisher = topicSession.createPublisher(myTopic);
			System.out.println("- Desconexion TopicPublisher created!");
			
			ObjectMessage objectMessage = topicSession.createObjectMessage();
		
			objectMessage.setObject(new Desconexion(miTracker.getId()));
			objectMessage.setJMSType("ObjectMessage");
			objectMessage.setJMSMessageID("ID-1");
			objectMessage.setJMSPriority(1);		
			
			topicPublisher.publish(objectMessage);
			System.out.println("Desconexión enviada");
			Thread.sleep(3000);
			ArrayList<EstadosEleccionMaster> nuevoEstado = new ArrayList<EstadosEleccionMaster>();
			nuevoEstado.add(EstadosEleccionMaster.Decidiendo);
			
			nuevoMasterTopicPublisher.setEstadoActual(nuevoEstado);
			System.out.println("Cambio de estado de Elección : " + nuevoMasterTopicPublisher.getEstadoActual());
			RedundantController.desconexion();
			System.out.println("DESCONECTADO");

		} catch (Exception e) {
			System.out.println("Se ha desonectado el tracker");
//			System.err.println("# Desconexion TopicPublisherTest Error 1: " + e.getMessage());
			
		} finally {
			try {
				//Close resources
				topicPublisher.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- Desconexion Topic resources closed!");				
			} catch (Exception ex) {
				System.err.println("# Desconexion TopicPublisherTest Error 2: " + ex.getMessage());
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
