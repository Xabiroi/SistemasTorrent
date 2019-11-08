package BDFileQueueListener;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

public class QueueFileReceiver extends Thread{	
	
	public void run() {
		String connectionFactoryName = "QueueConnectionFactory";
		String queueJNDIName = "jndi.ssdd.BDfileQueue";
		
		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		QueueReceiver queueReceiver = null;			
		
		try{
			//JNDI Initial Context
			Context ctx = new InitialContext();
		
			//Connection Factory
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ctx.lookup(connectionFactoryName);			
			
			//Message Destination
			Queue myQueue = (Queue) ctx.lookup(queueJNDIName);			
	
			//Connection	
			queueConnection = queueConnectionFactory.createQueueConnection();
			System.out.println("- FileQueue Connection created!");
			
			//Session
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);			
			System.out.println("- FileQueue Session created!");
			
			//Message Receiver
			queueReceiver = queueSession.createReceiver(myQueue);
			System.out.println("- FileQueueReceiver created!");
						
			QueueFileMessageListener listener = new QueueFileMessageListener();
			
			queueReceiver.setMessageListener(listener);
			
			//Start receiving messages
			queueConnection.start();
			
			Thread.sleep(10000);
		} catch (Exception e) {
			System.err.println("# QueueReceiverTest Error: " + e.getMessage());
		} finally {
			try {
				queueReceiver.close();
				queueSession.close();
				queueConnection.close();
				System.out.println("- Queue resources closed!");				
			} catch (Exception ex) {
				System.err.println("# QueueReceiverTest Error: " + ex.getMessage());
			}
		}
	}
}
