package bitTorrent.tracker.protocol.udp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 
 * Offset  Size            	Name            	Value
 * 0       32-bit integer  	action          	3 // error
 * 4       32-bit integer  	transaction_id
 * 8       string  message
 * 
 */

public class Error extends BitTorrentUDPMessage {

	private String message;

	public Error() {
		super(Action.ERROR);
	}
	
	@Override
	public byte[] getBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(16);
		buffer.order(ByteOrder.BIG_ENDIAN);
		
		buffer.putInt(0, super.getAction().value());
		buffer.putInt(4, super.getTransactionId());
		
		
				
		buffer.flip();
			
		return buffer.array();
	}
	
	public static Error parse(byte[] byteArray) {
		//TODO: COMPLETE THIS METHOD
		
		return null;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}