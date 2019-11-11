package BDFileQueueListener;

import java.io.File;
import java.util.Calendar;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQBytesMessage;

public class QueueFileMessageListener implements MessageListener  {

	private static String DEST_FILE = "./bd/test1";
	
	@Override
	public void onMessage(Message message) {
		if (message != null) {
			try {
				System.out.println("   - FileQueueListener: " + message.getClass().getSimpleName() + " received!");
								
				if (message instanceof ActiveMQBytesMessage) {
					String fileName = QueueFileMessageListener.DEST_FILE + ".db";
					
					//Read message content as an Array of bytes
					FileAsByteArrayManager.getInstance().writeFile(((ActiveMQBytesMessage)message).getContent().getData(), fileName);
					
					//Print received file details
					File file = new File(fileName);					
					System.out.println("     - Received BD file:  '" + file.getName() + "' (" + file.length() + " bytes)");
				}
			
			} catch (Exception ex) {
				System.err.println("# QueueFileMessageListener error: " + ex.getMessage());
			}
		}
	}
}
