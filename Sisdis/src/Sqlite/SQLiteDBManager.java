package Sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Objetos.Peer;
import Objetos.Swarm;
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
//	#######################################################
	public void insertPeer(String ip, String port,String idPeer) {	
		if (ip != null && !ip.isEmpty() &&
				port != null && !port.isEmpty()) {
		
			String sqlString = "INSERT INTO PEER ('IP', 'PUERTO','IDPEER') VALUES (?,?,?)";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, ip);
				stmt.setString(2, port);
				stmt.setString(3, idPeer);

				
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
		
			String sqlString = "UPDATE PEER SET PUERTO = ? WHERE IP = ?";
			
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
	
	public ArrayList<Peer> loadPeers() {	
		String sqlString = "SELECT * FROM PEER";
		ArrayList<Peer> arPeer = new ArrayList<Peer>();
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {			
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("\n - Loading peers from the db:");
			
			
			while(rs.next()) {
				System.out.println("    " + rs.getString("IP") + ".- " +rs.getString("PUERTO")+ ".- " +rs.getString("IDPEER"));
		         String ip = rs.getString("IP");
		         String puerto = rs.getString("PUERTO");
		         String idpeer = rs.getString("IDPEER");
		         arPeer.add(new Peer(ip,puerto,idpeer));
			}				
		} catch (Exception ex) {
			System.err.println("\n # Error loading data in the db: " + ex.getMessage());
		}
		return arPeer;
	}
	
	public void deletePeers() {	
		String sqlString = "DELETE FROM PEER";
		
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
//##########################################################################
	
	public void insertSwarm(String idSwarm) {	
		if (idSwarm != null && !idSwarm.isEmpty()) {
		
			String sqlString = "INSERT INTO SWARM ('IDSWARM') VALUES (?)";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, idSwarm);


				
				if (stmt.executeUpdate() == 1) {
					System.out.println("\n - A new swarm was inserted. :)");
					con.commit();
				} else {
					System.err.println("\n - A new swarm wasn't inserted. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				System.err.println("\n # Error storing data in the db: " + ex.getMessage());
			}
		} else {
			System.err.println("\n # Error inserting a new swarm: some parameters are 'null' or 'empty'.");
		}
	}
	
	public void updateSwarmId(String idSwarm) {	
		if (idSwarm != null && !idSwarm.isEmpty()) {
		
			String sqlString = "UPDATE SWARM SET IDSWARM = ? WHERE IDSWARM = ?";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, idSwarm);
				
				if (stmt.executeUpdate() != 0) {
					System.out.println("\n - Swarm's data was updated. :)");
					con.commit();
				} else {
					System.err.println("\n - Swarm's data wasn't updated. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				System.err.println("\n # Error updating data in the db: " + ex.getMessage());
			}
		} else {
			System.err.println("\n # Error updating Swarm's data: some parameters are 'null' or 'empty'.");
		}
	}
	
	public ArrayList<Swarm> loadSwarms() {	
		String sqlString = "SELECT * FROM SWARM";
		ArrayList<Swarm> arSwarm = new ArrayList<Swarm>();
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {			
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("\n - Loading swarms from the db:");
			
			while(rs.next()) {
				System.out.println("    " + rs.getInt("IDSWARM"));
				
		         String idSwarm = rs.getString("IDSWARM");

		         arSwarm.add(new Swarm(new ArrayList<Peer>(),idSwarm));
			}				
		} catch (Exception ex) {
			System.err.println("\n # Error loading data in the db: " + ex.getMessage());
		}
		return arSwarm;
	}
	
	public void deleteSwarms() {	
		String sqlString = "DELETE FROM SWARM";
		
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {						
			int deleted = stmt.executeUpdate();
			
			if (deleted > 0) {
				System.out.println("\n - '" + deleted + "' swarms were deleted.");
				con.commit();
			} else {
				System.out.println("\n - None swarm was deleted.");
				con.rollback();
			}				
		} catch (Exception ex) {
			System.err.println("\n # Error cleaning the db: " + ex.getMessage());
		}
	}
	
	//##################################################################################
	public void insertSwarmPeer(String idSwarm, String idPeer,float descargado) {	
		if (idSwarm != null && !idSwarm.isEmpty() &&
				idPeer != null && !idPeer.isEmpty()) {
		
			String sqlString = "INSERT INTO SWARM_PEER ('IDSWARM', 'IDPEER','DESCARGADO') VALUES (?,?,?)";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, idSwarm);
				stmt.setString(2, idPeer);
				stmt.setFloat(3, descargado);

				
				if (stmt.executeUpdate() == 1) {
					System.out.println("\n - A new Swarm_peer was inserted. :)");
					con.commit();
				} else {
					System.err.println("\n - A new Swarm_peer wasn't inserted. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				System.err.println("\n # Error storing data in the db: " + ex.getMessage());
			}
		} else {
			System.err.println("\n # Error inserting a new Swarm_peer: some parameters are 'null' or 'empty'.");
		}
	}
	
	public void updateSwarmPeer(String idSwarm, String idPeer, float descargado) {	
		if (idSwarm != null && !idSwarm.isEmpty() &&
				idPeer != null && !idPeer.isEmpty()) {
		
			String sqlString = "UPDATE SWARM_PEER SET DESCARGADO = ? WHERE IDSWARM = ? AND IDPEER = ?";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setFloat(1, descargado);
				stmt.setString(2, idSwarm);
				stmt.setString(3, idPeer);
				
				if (stmt.executeUpdate() != 0) {
					System.out.println("\n - SwarmPeer's data was updated. :)");
					con.commit();
				} else {
					System.err.println("\n - SwarmPeer's data wasn't updated. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				System.err.println("\n # Error updating data in the db: " + ex.getMessage());
			}
		} else {
			System.err.println("\n # Error updating SwarmPeer's data: some parameters are 'null' or 'empty'.");
		}
	}
	//FIXME arreglar que devuelva una lista
	public void loadSwarmPeers() {	
		String sqlString = "SELECT * FROM SWARM_PEER";
		
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {			
			ResultSet rs = stmt.executeQuery();
			
			System.out.println("\n - Loading peers from the db:");
			
			while(rs.next()) {
				System.out.println("    " + rs.getString("IDSWARM") + ".- " + rs.getString("IDPEER")+ ".- "+rs.getFloat("DESCARGADO"));
			}				
		} catch (Exception ex) {
			System.err.println("\n # Error loading data in the db: " + ex.getMessage());
		}
	}
	
	public void deleteSwarmPeers() {	
		String sqlString = "DELETE FROM SWARM_PEER";
		
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
	
	//##################################################################################
	public static void main(String[] args) {
		SQLiteDBManager manager = new SQLiteDBManager("bd/test.db");
	
		manager.insertPeer("192.168.1.53", "23","6");
		manager.loadPeers();
		manager.updatePeerPort("192.168.1.53", "45");
		manager.loadPeers();
//		manager.deletePeers();
		manager.closeConnection();
	}
}
