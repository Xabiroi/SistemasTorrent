package bitTorrent.tracker.protocol.udp;

import java.io.ObjectInputStream.GetField;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Offset          Size            	Name            	Value
 * 0               64-bit integer  	connection_id
 * 8               32-bit integer  	action          	2 // scrape
 * 12              32-bit integer  	transaction_id
 * 16 + 20 * n     20-byte string  	info_hash
 * 16 + 20 * N
 *
 */

public class ScrapeRequest extends BitTorrentUDPRequestMessage {

	private static List<String> infoHashes;
	
	public ScrapeRequest() {
		super(Action.SCRAPE);
		this.infoHashes = new ArrayList<>();
	}
	
	@Override 
	public byte[] getBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(16+20*infoHashes.size());
		buffer.order(ByteOrder.BIG_ENDIAN);
		
		buffer.putLong(0, super.getConnectionId());
		buffer.putInt(8, super.getAction().value());
		buffer.putInt(12, super.getTransactionId());
		
		CharBuffer charBuffer = buffer.asCharBuffer();
		
		for(int i = 0; i < getInfoHashes().size(); i++)
		{
		    charBuffer.put(infoHashes.get(i), 16+20*i, 16+19+20*i);
		}
		
		buffer.flip();
		return buffer.array();
	}
	
	public static ScrapeRequest parse(byte[] byteArray) {
		ByteBuffer buffer = ByteBuffer.wrap(byteArray);
		buffer.order(ByteOrder.BIG_ENDIAN);
		
		ScrapeRequest msg = new ScrapeRequest();
		
		msg.setConnectionId(buffer.getLong(0));
		msg.setAction(Action.valueOf(buffer.getInt(8)));
		msg.setTransactionId(buffer.getInt(12));
		
		for(int i = 0; i < infoHashes.size(); i += 20)
		{
			msg.addInfoHash(infoHashes.get(i));
		}
		
		return msg;
	}
	
	public List<String> getInfoHashes() {
		return infoHashes;
	}

	public void addInfoHash(String infoHash) {
		if (infoHash != null && !infoHash.trim().isEmpty() && !this.infoHashes.contains(infoHash)) {
			this.infoHashes.add(infoHash);
		}
	}
}
