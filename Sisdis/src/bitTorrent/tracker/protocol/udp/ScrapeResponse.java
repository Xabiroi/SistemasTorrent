package bitTorrent.tracker.protocol.udp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import bitTorrent.tracker.protocol.udp.BitTorrentUDPMessage.Action;

/**
 *
 * Offset      	Size            	Name            Value
 * 0           	32-bit integer  	action          2 // scrape
 * 4           	32-bit integer  	transaction_id
 * 8 + 12 * n  	32-bit integer  	seeders
 * 12 + 12 * n 	32-bit integer  	completed
 * 16 + 12 * n 	32-bit integer  	leechers
 * 8 + 12 * N
 * 
 */

public class ScrapeResponse extends BitTorrentUDPMessage {
	
	private static List<ScrapeInfo> scrapeInfos;

	public ScrapeResponse() {
		super(Action.SCRAPE);		
		this.scrapeInfos = new ArrayList<>();
	}
	
	@Override
	public byte[] getBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(16);
		buffer.order(ByteOrder.BIG_ENDIAN);
		
		buffer.putInt(0, super.getAction().value());
		buffer.putInt(4, super.getTransactionId());
		
		if(scrapeInfos.size() > 0 )
		{
			for(int i = 0; i < scrapeInfos.size(); i++)
			{
				buffer.putInt(8+12*i, scrapeInfos.get(i).getSeeders());
				buffer.putInt(12+12*i, scrapeInfos.get(i).getCompleted());
				buffer.putInt(16+12*i, scrapeInfos.get(i).getLeechers());
			}
		}
				
		buffer.flip();
			
		return buffer.array();
	}
	
	public static ScrapeResponse parse(byte[] byteArray) {
		try {
    		ByteBuffer buffer = ByteBuffer.wrap(byteArray);
		    buffer.order(ByteOrder.BIG_ENDIAN);
		    
		    ScrapeResponse msg = new ScrapeResponse();
		    
		    msg.setAction(Action.valueOf(buffer.getInt(0)));
		    msg.setTransactionId(buffer.getInt(4));
		    
		    ScrapeInfo si = null;
		    
		    for(int i = 0; i < scrapeInfos.size(); i++) {
			    si = new ScrapeInfo();
			    
			    si.setSeeders(buffer.getInt(8+12*i));
			    si.setCompleted(buffer.getInt(12+12*i));
			    si.setLeechers(buffer.getInt(16+12*i));
			    
			    msg.addScrapeInfo(si);
		    }		    
			
			return msg;
	} catch (Exception ex) {
		System.out.println("# Error parsing ScrapeResponse message: " + ex.getMessage());
		ex.printStackTrace();
	}
    
    return null;
	}
	
	public List<ScrapeInfo> getScrapeInfos() {
		return scrapeInfos;
	}

	public void addScrapeInfo(ScrapeInfo scrapeInfo) {
		if (scrapeInfo != null && !this.scrapeInfos.contains(scrapeInfo)) {
			this.scrapeInfos.add(scrapeInfo);
		}
	}
}