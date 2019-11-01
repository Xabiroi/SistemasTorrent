package BDFileListener;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;

public class FileAsByteArrayManager {

	private static FileAsByteArrayManager instance = null;
	
	private FileAsByteArrayManager() {
	}
	
	public static FileAsByteArrayManager getInstance() {
		if (instance == null) {
			instance = new FileAsByteArrayManager();
		}
		
		return instance;
	}
	
    public byte[] readFileAsBytes(String fileName) throws IOException {    
    	try (RandomAccessFile file = new RandomAccessFile(fileName, "r")) {
            byte[] bytes = new byte[(int) file.length()];
            file.readFully(bytes);
            
            return bytes;
        }
    }

    public void writeFile(byte[] bytes, String fileName) throws IOException {
    	try(RandomAccessFile file = new RandomAccessFile(fileName, "rw")) {
    		file.write(bytes);
    	}
    }
    
    public static void main(String[] args) {
    	String src = "./file_in/Test.xlsx";
    	String dst = "./file_out/offline-Test_" + Calendar.getInstance().getTimeInMillis() + ".xlsx";
    	
    	try {
    		byte[] bytes = FileAsByteArrayManager.getInstance().readFileAsBytes(src);
    		FileAsByteArrayManager.getInstance().writeFile(bytes, dst);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
}