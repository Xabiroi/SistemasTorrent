package bitTorrent.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class ByteUtils {
	
	private static final String ALLOWED_CHARS = "$-_!";
	private static char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static final String HEX_DIGITS = "0123456789ABCDEF";
	
	
	public static String intToIpAddress(int number) {
		long ip = number > 0 ? (long) number : (((long) Integer.MAX_VALUE)+1) * 2 + number;

		StringBuffer ipBuffer = new StringBuffer();
		
		ipBuffer.append(((ip >> 24 ) & 0xFF));
		ipBuffer.append(".");
		ipBuffer.append(((ip >> 16 ) & 0xFF));
		ipBuffer.append(".");
		ipBuffer.append(((ip >> 8 ) & 0xFF));
		ipBuffer.append(".");
		ipBuffer.append(ip & 0xFF);
		
		return  ipBuffer.toString();
	}
	
	public static int ipAddressToInt(String ipAddress) {			
		long result = 0;
				
		String[] ipAddressInArray = ipAddress.split("\\.");

		for (int i = 3; i >= 0; i--) {
			result |= Integer.parseInt(ipAddressInArray[3 - i]) << (i * 8);
		}
		
		return (int) result;
	}
	
    //Generates SHA1 Hash from an array of bytes
	public static byte[] generateSHA1Hash(byte[] bytes) {
		try {
			byte[] hash = new byte[20];
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			hash = sha.digest(bytes);
			
			return hash;		
		} catch (NoSuchAlgorithmException e) {
			System.err.println("'SHA-1' is not a valid algorithm name.");
		}
		
		return null;
	}
	
	public static String generatePeerId() {
		StringBuffer bufferID = new StringBuffer();
		bufferID.append("-");
		bufferID.append("SSDD01");
		bufferID.append("-");
		Random random = new Random();
		
		for(int i=0; i<12; i++) {
			bufferID.append(random.nextInt(9));
		}
				
		return bufferID.toString();
	}
	 
	/**
	 * Transforms an array of bytes to a binary Hexadecimal String.
	 * @param
	 * 			bytes byte[] the source array of bytes.
	 * @return
	 * 			the resulting String. 
	 */
	public static String toHexString(byte[] bytes) {
		return DatatypeConverter.printHexBinary(bytes);
	}

    //Extract a subarray of bytes from a source array with a certanin length
    public static byte[] subArray(byte[] b, int offset, int length){
        byte[] sub = new byte[length];
        
        for(int i = offset; i < offset + length; i++) {
            sub[i-offset] = b[i];
        }
        
        return sub;
    }

	/**
	 * Converts a byte[] into a BitSet.
	 * 
	 * @author Chris Lauderdale
	 * @param bytes
	 *            A <code>byte[]</code> representing the bits in
	 *            <code>bytes</code>.
	 * @return A BitSet object representing the input byte[].
	 */
	public static BitSet bytesToBitSet(byte[] bytes) {
		final BitSet rv = new BitSet();
		
		bytesToBitSet(bytes, rv);
		
		return rv;
	}

	/**
	 * Converts a byte[] into a BitSet.
	 * 
	 * @author Chris Lauderdale
	 * @param bytes
	 *            A <code>byte[]</code> representing the bits in
	 *            <code>bytes</code>.
	 * @param bits
	 *            The {@link BitSet} to fill.
	 */
	public static void bytesToBitSet(byte[] bytes, final BitSet bits) {
		int i, j, k;
		
		bits.clear();
		
		for (i = k = 0; i < bytes.length; i++) {
			for (j = 0x80; j != 0; j >>>= 1, k++) {
				if ((bytes[i] & j) != 0) {
					bits.set(k);
				}
			}
		}
	}

	/**
	 * Converts a <code>BitSet</code> into a <code>byte[]</code>.
	 * 
	 * @author Chris Lauderdale
	 * @param bits
	 *            A {@link BitSet}.
	 * @param bytes
	 *            The <code>byte</code> array to fill.
	 */
	public static void bitSetToBytes(BitSet bits, byte[] bytes) {
		int i;
		
		java.util.Arrays.fill(bytes, (byte) 0);
		
		for (i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {
			bytes[i >>> 3] |= 0x80 >>> (i & 7);
		}
	}

	/**
	 * Converts a <code>BitSet</code> into a <code>byte[]</code>.
	 * 
	 * @author Chris Lauderdale
	 * @param bits
	 *            A <code>BitSet</code>.
	 * @return A <code>byte[]</code> containing the same bits stored in
	 *         <code>bits</code>.
	 */
	public static byte[] bitSetToBytes(BitSet bits) {
		final byte[] bytes = new byte[(bits.size() + 7) >>> 3];
		
		bitSetToBytes(bits, bytes);
		
		return bytes;
	}

	/**
	 * Transforms an array of bytes to an URL encoded String.
	 * @param
	 * 			bytes byte[] the source array of bytes.
	 * @return
	 * 			the resulting String. 
	 */
	public static String toURLEncodedString(byte[] bytes ) {
        StringBuffer result = new StringBuffer(bytes.length*2);
        char c;
        
        for( int i = 0; i < bytes.length; i++ ) {
            c = (char) bytes[i];

            switch( c ) {
            	case '.':	
            	case '-':  
            	case '_':
            	case '~':
            		result.append(c);
            		break;
              default:
                if( (c >= 'a' && c <= 'z') || 
                	(c >= 'A' && c <= 'Z') || 
                	(c >= '0' && c <= '9') ) {
                    result.append(c);
                } else {
                    result.append('%');
                    result.append(HEX_DIGITS.charAt((c & 0xF0) >> 4 ));
                    result.append(HEX_DIGITS.charAt(c & 0x0F));
                }
            }
        }

        return result.toString();
    }
	
	static String makeHTTPEscaped(String str) {
		final int l = str.length();
		char c;
		
		for (int i = 0; i < l; i++) {
			c = str.charAt(i);
			
			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
				(c >= '0' && c <= '9') || ALLOWED_CHARS.indexOf(c) >= 0) {
				continue;
			}

			final StringBuffer rv = new StringBuffer(str.substring(0, i));
			
			c &= 0xFF;
			rv.append('%').append(HEX_CHARS[c >>> 4]).append(HEX_CHARS[c & 15]);
			
			for (++i; i < l; i++) {
				c = str.charAt(i);
			
				if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || 
					(c >= '0' && c <= '9') || ALLOWED_CHARS.indexOf(c) >= 0) {
					rv.append(c);
				} else {
					c &= 0xFF;
					rv.append('%').append(HEX_CHARS[c >>> 4])
							.append(HEX_CHARS[c & 15]);
				}
			}
			
			return rv.toString();
		}
		
		return str;
	}
	
	public static byte[] fileToByteArray(String filename) {
		byte[] result = null;
		
		if (filename != null) {
			try (FileInputStream fis = new FileInputStream(filename);
				 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {		        
				byte[] buf = new byte[1024];
		        
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum);
	            }

				result = bos.toByteArray();
			} catch (IOException e) {
				System.err.println("# fileToByteArray(): " + e.getMessage());
			}
		}
		
		return result;
	}

	public static byte[] toByteArray(String s) {
	    return DatatypeConverter.parseHexBinary(s);
	}
	
	public static byte[] concatenateByteArrays(byte[]... arrays) {
		
		int newArrayLength = 0;
		
		for (byte[] a : arrays) {
			newArrayLength += a.length;
		}
				
		byte newArray[] = new byte[newArrayLength];
		
		int currentIndex = 0;
		
		for (int i=0; i<arrays.length; i++) {	
			System.arraycopy(arrays[i], 0, newArray, currentIndex, arrays[i].length);
			currentIndex += arrays[i].length;
		}
		
		return newArray;
	}

	
	//Converts byte array to int
	public static int arrayToInt(byte[] byteArray){
	    ByteBuffer buffer = ByteBuffer.wrap(byteArray);
	    
	    return buffer.getInt();
	}
	
	//Converts byte array to int
	public static int arrayToShort(byte[] byteArray){
	    ByteBuffer buffer = ByteBuffer.wrap(byteArray);
	    
	    return buffer.getShort();
	}
	
	//Converts byte array to long
	public static long arrayToLong(byte[] byteArray){
	    ByteBuffer buffer = ByteBuffer.wrap(byteArray);
	    
	    return buffer.getLong();
	}

	//Converts a byte array to an int
    public static int byteArrayToInt(byte[] b) {
        if (b.length == 4) {
            return b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff);
        } else if (b.length == 2) {
            return 0x00 << 24 | 0x00 << 16 | (b[0] & 0xff) << 8 | (b[1] & 0xff);
        } else {
        	return 0;
        }
    }	
	
	//Converts int value to a Big Endian byte array
	public static byte[] toBigEndian(int intValue){
	    ByteBuffer buffer = ByteBuffer.allocate(4);
	    buffer.order(ByteOrder.BIG_ENDIAN);
	    buffer.putInt(intValue);
	    buffer.flip();
	    
	    return buffer.array();
	}
	
	//Converts int value to a Big Endian byte array
	public static byte[] toLittleEndian(int intValue){
	    ByteBuffer buffer = ByteBuffer.allocate(4);
	    buffer.order(ByteOrder.LITTLE_ENDIAN);
	    buffer.putInt(intValue);
	    buffer.flip();
	    
	    return buffer.array();
	}
	
	//Converts long value to a Big Endian byte array
	public static byte[] toBigEndian(long longValue){
	    ByteBuffer buffer = ByteBuffer.allocate(8);
	    buffer.order(ByteOrder.BIG_ENDIAN);
	    buffer.putLong(longValue);
	    buffer.flip();
	    
	    return buffer.array();
	}
	
	//Converts long value to a Little Endian byte array
	public static byte[] toLittleEndian(long longValue){
	    ByteBuffer buffer = ByteBuffer.allocate(8);
	    buffer.order(ByteOrder.LITTLE_ENDIAN);
	    buffer.putLong(longValue);
	    buffer.flip();
	    
	    return buffer.array();
	}
	
	//Converts Big Endian byte array to int
	public static int bigEndianToInt(byte[] byteArray){
	    ByteBuffer buffer = ByteBuffer.wrap(byteArray);
	    buffer.order(ByteOrder.BIG_ENDIAN);
	    
	    return buffer.getInt();
	}
	
	//Converts Little Endian byte array to int
	public static int littleEndianToInt(byte[] byteArray){
	    ByteBuffer buffer = ByteBuffer.wrap(byteArray);
	    buffer.order(ByteOrder.LITTLE_ENDIAN);
	    
	    return buffer.getInt();
	}
	
	//Converts Big Endian byte array to long
	public static long bigEndianToLong(byte[] byteArray){
	    ByteBuffer buffer = ByteBuffer.wrap(byteArray);
	    buffer.order(ByteOrder.BIG_ENDIAN);
	    
	    return buffer.getLong();
	}
	
	//Converts Little Endian byte array to long
	public static long littleEndianToLong(byte[] byteArray){
	    ByteBuffer buffer = ByteBuffer.wrap(byteArray);
	    buffer.order(ByteOrder.LITTLE_ENDIAN);
	    
	    return buffer.getLong();
	}
		
	//Converts an int to an array of bytes
	public static byte[] intToBigEndianBytes(int int_value, byte[] bytes, int offset) {
		bytes[3 + offset] = (byte) int_value;
		int_value >>>= 8;
		bytes[2 + offset] = (byte) int_value;
		int_value >>>= 8;
		bytes[1 + offset] = (byte) int_value;
		int_value >>>= 8;
		bytes[0 + offset] = (byte) int_value;
		
		return bytes;
	}

	//Extracts the int value of an array of bytes. The bytes representing the int value start at a certain index.
	public static int bigEndianBytesToInt(byte[] bytes, int i) {
		return ((bytes[0 + i] & 0xFF) << 24) + 
			   ((bytes[1 + i] & 0xFF) << 16) + 
			   ((bytes[2 + i] & 0xFF) << 8) + 
			   (bytes[3 + i] & 0xFF);
	}
	
	//Converts a byte to an unsigned int
    public static int byteToUnsignedInt(byte b){
        return 0x00 << 24 | b & 0xff;
    }
    	
	public static byte[] int32ToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	public static byte[] shortToByteArray(short value) {
	    return new byte[] {
	            (byte)0,
	            (byte)0,
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	public static byte[] int16ToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 8),
	            (byte)value};
	}
}