package bitTorrent.tracker.protocol.udp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

import bitTorrent.tracker.protocol.udp.BitTorrentUDPMessage.Action;

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
		
		StringBuffer stringBuff = new StringBuffer(getMessage());
		buffer.put(stringBuff.toString().getBytes());
		
		buffer.flip();
		return buffer.array();
	}
	
	public static Error parse(byte[] byteArray) {
		
		try
		{
		ByteBuffer buffer = ByteBuffer.wrap(byteArray);
	    buffer.order(ByteOrder.BIG_ENDIAN);
	    
	    Error msg = new Error();
	    
	    msg.setAction(Action.valueOf(buffer.getInt(0)));
	    msg.setTransactionId(buffer.getInt(4));
	    if(buffer.hasRemaining())
	    msg.setMessage(StandardCharsets.UTF_8.decode(buffer).toString());
	    
		return msg;
		
		}
		catch (Exception ex)
		{
			System.out.println("# Error parsing ScrapeResponse message: " + ex.getMessage());
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}