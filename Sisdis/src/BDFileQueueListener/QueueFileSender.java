package BDFileQueueListener;

import javax.jms.BytesMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

public class QueueFileSender extends Thread{
	
	private static String SRC_FILE = "./bd/test.db";
	
	public void run() {		
		String connectionFactoryName = "QueueConnectionFactory";
		String queueJNDIName = "jndi.ssdd.BDfileQueue";		
		
		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		QueueSender queueSender = null;			
		
		try{
			//JNDI Initial Context
			Context ctx = new InitialContext();
		
			//Connection Factory
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ctx.lookup(connectionFactoryName);
			
			//Message Destination
			Queue myQueue = (Queue) ctx.lookup(queueJNDIName);			
	
			//Connection	
			queueConnection = queueConnectionFactory.createQueueConnection();
			System.out.println("- File Queue Connection created!");
			
			//Session
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- File Queue Session created!");
	
			//Message Producer
			queueSender = queueSession.createSender(myQueue);			
			System.out.println("- File QueueSender created!");
			
			//Bytes Message
			BytesMessage bytesMessage = queueSession.createBytesMessage();
			
			//Message Body obtain the a byte array from a file
			bytesMessage.writeBytes(FileAsByteArrayManager.getInstance().readFileAsBytes(QueueFileSender.SRC_FILE));
			
			//Send the Message
			queueSender.send(bytesMessage);
			
			System.out.println("- ByteMessage sent to the Queue!");
		} catch (Exception e) {
			System.err.println("# QueueSenderTest Error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				queueSender.close();
				queueSession.close();
				queueConnection.close();
				System.out.println("- Queue resources closed!");				
			} catch (Exception ex) {
				System.err.println("# QueueSenderTest Error: " + ex.getMessage());
			}
		}
	}
}