package es.deusto.ingenieria.ssdd.jms.topic;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import Mensajes.Keepalive;

public class TopicListener implements MessageListener {

	@Override
	//This method is call when a new Message arrives to the topic
	public void onMessage(Message message) {		
		if (message != null) {
			try {
				System.out.println("   - TopicListener: " + message.getClass().getSimpleName() + " received!");
				
//				ObjectMessage objectMessage = (ObjectMessage) message;
//				System.out.println("ABCD"+objectMessage.getObject().getClass());
				//Depending on the type of the message the process is different
				if (message != null) {
					System.out.println("     - TopicListener: ObjectMessage id = '" + message.getClass().getCanonicalName());
					
					ObjectMessage objectMessage = (ObjectMessage) message;					
					Keepalive keepAlive = (Keepalive) objectMessage.getObject();
					
					System.out.println("     - Keep Alive ID: " + keepAlive.getI());
					
					//System.out.println("     - TopicListener: ObjectMessage id = '" + ((Keepalive) ((ObjectMessage) message).getObject()).getIdOrigen());
				}
			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}		
	}
}