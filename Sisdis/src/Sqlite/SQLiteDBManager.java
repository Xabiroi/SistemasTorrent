package Sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//FIXME meter los metodos en datacontroller
public class SQLiteDBManager {

	private Connection con;
	
	public SQLiteDBManager(String dbname) {
		con = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + dbname);
			con.setAutoCommit(false);
			
			System.out.println(" - Db connection was opened :)");
		} catch (Exception ex) {
			System.err.println(" # Unable to create SQLiteDBManager: " + ex.getMessage());
		}
	}
	
	public void closeConnection() {
		try {
			con.close();
			System.out.println("\n - Db connection was closed :)");
		} catch (Exception ex) {
			System.err.println("\n # Error closing db connection: " + ex.getMessage());
		}
	}
	
	public void insertPeer(String ip, String port) {	
		if (ip != null && !ip.isEmpty() &&
				port != null && !port.isEmpty()) {
		
			String sqlString = "INSERT INTO PEERS ('IP', 'PORT') VALUES (?,?)";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, ip);
				stmt.setString(2, port);

				
				if (stmt.executeUpdate() == 1) {
					System.out.println("\n - A new peer was inserted. :)");
					con.commit();
				} else {
					System.err.println("\n - A new peer wasn't inserted. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				System.err.println("\n # Error storing data in the db: " + ex.getMessage());
			}
		} else {
			System.err.println("\n # Error inserting a new peer: some parameters are 'null' or 'empty'.");
		}
	}
	
	public void updatePeerPort(String ip, String port) {	
		if (ip != null && !ip.isEmpty() &&
				port != null && !port.isEmpty()) {
		
			String sqlString = "UPDATE PEERS SET PORT = ? WHERE IP = ?";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, port);
				stmt.setString(2, ip);
				
				if (stmt.executeUpdate() != 0) {
					System.out.println("\n - Peer's data was updated. :)");
					con.commit();
				} else {
					System.err.println("\n - Peer's data wasn't updated. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				System.err.println("\n # Error updating data in the db: " + ex.getMessage());
			}
		} else {
			System.err.println("\n # Error updating Peer's data: some parameters are 'null' or 'empty'.");
		}
	}
	
	public void loadPeers() {	
		String sqlString = "SELECT * FROM PEERS";
		
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {			
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("\n - Loading peers from the db:");
			
			while(rs.next()) {
				System.out.println("    " + rs.getInt("IP") + ".- " + 
			                                rs.getString("PORT"));
			}				
		} catch (Exception ex) {
			System.err.println("\n # Error loading data in the db: " + ex.getMessage());
		}
	}
	
	public void deletePeers() {	
		String sqlString = "DELETE FROM PEERS";
		
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {						
			int deleted = stmt.executeUpdate();
			
			if (deleted > 0) {
				System.out.println("\n - '" + deleted + "' peers were deleted.");
				con.commit();
			} else {
				System.out.println("\n - None peer was deleted.");
				con.rollback();
			}				
		} catch (Exception ex) {
			System.err.println("\n # Error cleaning the db: " + ex.getMessage());
		}
	}
	
	public static void main(String[] args) {
		SQLiteDBManager manager = new SQLiteDBManager("db/test.db");
	
		manager.insertPeer("192.168.1.53", "23");
		manager.loadPeers();
		manager.updatePeerPort("192.168.1.53", "45");
		manager.loadPeers();
		manager.deletePeers();
		manager.closeConnection();
	}
}
