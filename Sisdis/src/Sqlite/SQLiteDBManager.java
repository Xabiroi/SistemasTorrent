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

	private static Connection con;
	
	public SQLiteDBManager(String dbname) {
		con = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + dbname);
			con.setAutoCommit(false);
			
//			//System.out.println(" - Db connection was opened :)");
		} catch (Exception ex) {
			//System.err.println(" # Unable to create SQLiteDBManager: " + ex.getMessage());
		}
	}
	
	public void closeConnection() {
		try {
			con.close();
//			//System.out.println("\n - Db connection was closed :)");
		} catch (Exception ex) {
			//System.err.println("\n # Error closing db connection: " + ex.getMessage());
		}
	}
//	#######################################################
	public static void insertPeer(String ip, String port) {	
		if (ip != null && !ip.isEmpty() &&
				port != null && !port.isEmpty()) {
		
			String sqlString = "INSERT INTO PEER ('IP', 'PUERTO') VALUES (?,?)";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, ip);
				stmt.setString(2, port);

				if (stmt.executeUpdate() == 1) {
					//System.out.println("\n - A new peer was inserted. :)");
					con.commit();
				} else {
					//System.err.println("\n - A new peer wasn't inserted. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				//System.err.println("\n # Error storing data in the db: " + ex.getMessage());
			}
		} else {
			//System.err.println("\n # Error inserting a new peer: some parameters are 'null' or 'empty'.");
		}
	}
	
	public static void insertPeer(String ip, String port, long connectionIdPrincipal, long connectionIdSecundario,long transaction,long tiempo) {	
		if (ip != null && !ip.isEmpty() &&
				port != null && !port.isEmpty()) {
		
			String sqlString = "INSERT INTO PEER ('IP', 'PUERTO','IDPRINCIPAL','IDSECUNDARIO','TRANSACTIONID','TIEMPO') VALUES (?,?,?,?,?,?)";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, ip);
				stmt.setString(2, port);
				stmt.setLong(3, connectionIdPrincipal);
				stmt.setLong(4, connectionIdSecundario);
				stmt.setLong(5, transaction);
				stmt.setLong(6, tiempo);

				if (stmt.executeUpdate() == 1) {
					//System.out.println("\n - A new peer was inserted. :)");
					con.commit();
				} else {
					//System.err.println("\n - A new peer wasn't inserted. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				//System.err.println("\n # Error storing data in the db: " + ex.getMessage());
			}
		} else {
			//System.err.println("\n # Error inserting a new peer: some parameters are 'null' or 'empty'.");
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
					//System.out.println("\n - Peer's data was updated. :)");
					con.commit();
				} else {
					//System.err.println("\n - Peer's data wasn't updated. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				//System.err.println("\n # Error updating data in the db: " + ex.getMessage());
			}
		} else {
			//System.err.println("\n # Error updating Peer's data: some parameters are 'null' or 'empty'.");
		}
	}
	
	public static void updateIdAndTr(String ip, long connectionIdPrincipal, long connectionIdSecundario,long transaction, long tiempo) {	
		if (ip != null && !ip.isEmpty()) {
		//'IDPRINCIPAL','IDSECUNDARIO','TRANSACTIONID'
			String sqlString = "UPDATE PEER SET IDPRINCIPAL = ?, IDSECUNDARIO = ?, TRANSACTIONID = ?, TIEMPO = ? WHERE IP = ?";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setLong(1, connectionIdPrincipal);
				stmt.setLong(2, connectionIdSecundario);
				stmt.setLong(3, transaction);
				stmt.setLong(4, tiempo);
				stmt.setString(5, ip);
				
				if (stmt.executeUpdate() != 0) {
					//System.out.println("\n - Peer's data was updated. :)");
					con.commit();
				} else {
					//System.err.println("\n - Peer's data wasn't updated. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				//System.err.println("\n # Error updating data in the db: " + ex.getMessage());
			}
		} else {
			//System.err.println("\n # Error updating Peer's data: some parameters are 'null' or 'empty'.");
		}
	}
	
	public static void updateTr(String ip, long connectionIdPrincipal,long transaction) {	
		if (ip != null && !ip.isEmpty()) {
		//'IDPRINCIPAL','IDSECUNDARIO','TRANSACTIONID'
			String sqlString = "UPDATE PEER SET TRANSACTIONID = ? WHERE IP = ? AND IDPRINCIPAL = ?";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setLong(1, transaction);
				stmt.setString(2, ip);
				stmt.setLong(3, connectionIdPrincipal);
				if (stmt.executeUpdate() != 0) {
					//System.out.println("\n - Peer's data was updated. :)");
					con.commit();
				} else {
					//System.err.println("\n - Peer's data wasn't updated. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				//System.err.println("\n # Error updating data in the db: " + ex.getMessage());
			}
		} else {
			//System.err.println("\n # Error updating Peer's data: some parameters are 'null' or 'empty'.");
		}
	}
	
	
	public static void updateTiempo(String ip, long connectionIdPrincipal,long tiempo) {	
		if (ip != null && !ip.isEmpty()) {
		//'IDPRINCIPAL','IDSECUNDARIO','TRANSACTIONID'
			String sqlString = "UPDATE PEER SET TIEMPO = ? WHERE IP = ? AND IDPRINCIPAL = ?";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setLong(1, tiempo);
				stmt.setString(2, ip);
				stmt.setLong(3, connectionIdPrincipal);
				
				if (stmt.executeUpdate() != 0) {
					//System.out.println("\n - Peer's data was updated. :)");
					con.commit();
				} else {
					//System.err.println("\n - Peer's data wasn't updated. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				//System.err.println("\n # Error updating data in the db: " + ex.getMessage());
			}
		} else {
			//System.err.println("\n # Error updating Peer's data: some parameters are 'null' or 'empty'.");
		}
	}
	
	
	public static ArrayList<Peer> loadPeers() {	
		String sqlString = "SELECT * FROM PEER";
		ArrayList<Peer> arPeer = new ArrayList<Peer>();
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {			
			ResultSet rs = stmt.executeQuery();
			
			//System.out.println("\n - Loading peers from the db:");
			
			
			while(rs.next()) {
				//System.out.println("    " + rs.getString("IP") + ".- " +rs.getString("PUERTO")+ ".- " +rs.getString("IDPEER"));
		         String ip = rs.getString("IP");
		         String puerto = rs.getString("PUERTO");
		         arPeer.add(new Peer(ip,Integer.parseInt(puerto)));
			}				
		} catch (Exception ex) {
			//System.err.println("\n # Error loading data in the db: " + ex.getMessage());
		}
		return arPeer;
	}
	
	public static ArrayList<Peer> loadPeers2() {	
		String sqlString = "SELECT * FROM PEER";
		ArrayList<Peer> arPeer = new ArrayList<Peer>();
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {			
			ResultSet rs = stmt.executeQuery();
			
			//System.out.println("\n - Loading peers from the db:");
			
			
			while(rs.next()) {
				//System.out.println("    " + rs.getString("IP") + ".- " +rs.getString("PUERTO")+ ".- " +rs.getString("IDPEER"));
		         String ip = rs.getString("IP");
		         String puerto = rs.getString("PUERTO");
		         long idPrincipal = rs.getLong("IDPRINCIPAL");
		         long idSecundario = rs.getLong("IDSECUNDARIO");
		         int idTransaccion = rs.getInt("TRANSACTIONID");
		         long tiempo = rs.getLong("TIEMPO");
		         arPeer.add(new Peer(ip,Integer.parseInt(puerto),null,idTransaccion,idPrincipal,idSecundario,tiempo));
			}				
		} catch (Exception ex) {
			//System.err.println("\n # Error loading data in the db: " + ex.getMessage());
		}
		return arPeer;
	}
	
	public static void deletePeers() {	
		String sqlString = "DELETE FROM PEER";
		
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {						
			int deleted = stmt.executeUpdate();
			
			if (deleted > 0) {
				//System.out.println("\n - '" + deleted + "' peers were deleted.");
				con.commit();
			} else {
				//System.out.println("\n - None peer was deleted.");
				con.rollback();
			}				
		} catch (Exception ex) {
			//System.err.println("\n # Error cleaning the db: " + ex.getMessage());
		}
	}
//##########################################################################
	
	public void insertSwarm(String idSwarm, int tamanyo) {	
		if (idSwarm != null && !idSwarm.isEmpty()) {
		
			String sqlString = "INSERT INTO SWARM ('IDSWARM','SIZE') VALUES (?,?)";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, idSwarm);
				stmt.setInt(2, tamanyo);

				
				if (stmt.executeUpdate() == 1) {
					//System.out.println("\n - A new swarm was inserted. :)");
					con.commit();
				} else {
					//System.err.println("\n - A new swarm wasn't inserted. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				//System.err.println("\n # Error storing data in the db: " + ex.getMessage());
			}
		} else {
			//System.err.println("\n # Error inserting a new swarm: some parameters are 'null' or 'empty'.");
		}
	}
	//?
//	public static void updateSwarmId(String idSwarm) {	
//		if (idSwarm != null && !idSwarm.isEmpty()) {
//		
//			String sqlString = "UPDATE SWARM SET IDSWARM = ? WHERE IDSWARM = ?";
//			
//			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
//				stmt.setString(1, idSwarm);
//				
//				if (stmt.executeUpdate() != 0) {
//					//System.out.println("\n - Swarm's data was updated. :)");
//					con.commit();
//				} else {
//					//System.err.println("\n - Swarm's data wasn't updated. :(");
//					con.rollback();
//				}	
//			} catch (Exception ex) {
//				//System.err.println("\n # Error updating data in the db: " + ex.getMessage());
//			}
//		} else {
//			//System.err.println("\n # Error updating Swarm's data: some parameters are 'null' or 'empty'.");
//		}
//	}
	
	public ArrayList<Swarm> loadSwarms() {	
		String sqlString = "SELECT * FROM SWARM";
		ArrayList<Swarm> arSwarm = new ArrayList<Swarm>();
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {			
			ResultSet rs = stmt.executeQuery();
			
			//System.out.println("\n - Loading swarms from the db:");
			
			while(rs.next()) {
				//System.out.println("    " + rs.getInt("IDSWARM"));
				
		         String idSwarm = rs.getString("IDSWARM");
		         int size = rs.getInt("SIZE");
		         //TODO load peers con las ip de swarm_peer   una join
		         
		         
		         
		         arSwarm.add(new Swarm(new ArrayList<Peer>(),idSwarm));
			}				
		} catch (Exception ex) {
			//System.err.println("\n # Error loading data in the db: " + ex.getMessage());
		}
		return arSwarm;
	}
	
	public static ArrayList<Swarm> loadSwarms(String infohash) {	
		String sqlString = "SELECT * FROM SWARM WHERE IDSWARM = ?";
		ArrayList<Swarm> arSwarm = new ArrayList<Swarm>();
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {	
			
			stmt.setString(1, infohash);
			
			ResultSet rs = stmt.executeQuery();
			
			//System.out.println("\n - Loading swarms from the db:");
			
			while(rs.next()) {
				//System.out.println("    " + rs.getInt("IDSWARM"));
				
		         String idSwarm = rs.getString("IDSWARM");
		         int size = rs.getInt("SIZE");
		         //TODO load peers con las ip de swarm_peer   una join
		         
		         
		         
		         arSwarm.add(new Swarm(new ArrayList<Peer>(),idSwarm,size));
			}				
		} catch (Exception ex) {
			//System.err.println("\n # Error loading data in the db: " + ex.getMessage());
		}
		return arSwarm;
	}
	
	public static void deleteSwarms() {	
		String sqlString = "DELETE FROM SWARM";
		
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {						
			int deleted = stmt.executeUpdate();
			
			if (deleted > 0) {
				//System.out.println("\n - '" + deleted + "' swarms were deleted.");
				con.commit();
			} else {
				//System.out.println("\n - None swarm was deleted.");
				con.rollback();
			}				
		} catch (Exception ex) {
			//System.err.println("\n # Error cleaning the db: " + ex.getMessage());
		}
	}
	
	//##################################################################################
	public void insertSwarmPeer(String idSwarm, String ip,float descargado) {	
		if (idSwarm != null && !idSwarm.isEmpty() &&
				ip != null && !ip.isEmpty()) {
		
			String sqlString = "INSERT INTO SWARM_PEER ('IDSWARM', 'IP','DESCARGADO') VALUES (?,?,?)";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, idSwarm);
				stmt.setString(2, ip);
				stmt.setFloat(3, descargado);

				
				if (stmt.executeUpdate() == 1) {
					//System.out.println("\n - A new Swarm_peer was inserted. :)");
					con.commit();
				} else {
					//System.err.println("\n - A new Swarm_peer wasn't inserted. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				//System.err.println("\n # Error storing data in the db: " + ex.getMessage());
			}
		} else {
			//System.err.println("\n # Error inserting a new Swarm_peer: some parameters are 'null' or 'empty'.");
		}
	}
	
	public static void updateSwarmPeer(String idSwarm, String ip, float descargado) {	
		if (idSwarm != null && !idSwarm.isEmpty() &&
				ip != null && !ip.isEmpty()) {
		
			String sqlString = "UPDATE SWARM_PEER SET DESCARGADO = ? WHERE IDSWARM = ? AND IP = ?";
			
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setFloat(1, descargado);
				stmt.setString(2, idSwarm);
				stmt.setString(3, ip);
				
				if (stmt.executeUpdate() != 0) {
					//System.out.println("\n - SwarmPeer's data was updated. :)");
					con.commit();
				} else {
					//System.err.println("\n - SwarmPeer's data wasn't updated. :(");
					con.rollback();
				}	
			} catch (Exception ex) {
				//System.err.println("\n # Error updating data in the db: " + ex.getMessage());
			}
		} else {
			//System.err.println("\n # Error updating SwarmPeer's data: some parameters are 'null' or 'empty'.");
		}
	}
	//FIXME arreglar que devuelva una lista
	public static void loadSwarmPeers() {	
		String sqlString = "SELECT * FROM SWARM_PEER";
		
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {		
			ResultSet rs = stmt.executeQuery();
					//System.out.println("\n - Loading peers from the db:");
							
			
			
			//System.out.println("\n - Loading peers from the db:");
			
			while(rs.next()) {
				//System.out.println("    " + rs.getString("IDSWARM") + ".- " + rs.getString("IDPEER")+ ".- "+rs.getFloat("DESCARGADO"));
			}				
		} catch (Exception ex) {
			//System.err.println("\n # Error loading data in the db: " + ex.getMessage());
		}
	}
	
	public static ArrayList<Swarm> loadSwarmPeers(String infohash) {	
		
		String sqlString = "SELECT * FROM SWARM_PEER WHERE IDSWARM = ?";
		ArrayList<Swarm> arSwarm = new ArrayList<Swarm>();
		ArrayList<Peer> arPeer = SQLiteDBManager.loadPeers();
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
			stmt.setString(1, infohash);
			
			ResultSet rs = stmt.executeQuery();
			
			
			while(rs.next()) {
				ArrayList<Peer> ap = new ArrayList<Peer>();
				
		        String idSwarm = rs.getString("IDSWARM");
		        String ip = rs.getString("IP");
		        long descarga = rs.getLong("DESCARGADO");
		        //TODO load peers con las ip de swarm_peer   una join
		         
		        for(Peer p:arPeer) {
		        	if(ip.equals(p.getIP())) {
		        		ap.add(new Peer(p.getIP(),p.getPuerto(),descarga));
		        	}
		        }
		         
		        arSwarm.add(new Swarm(ap,idSwarm));
		        //System.out.println("    " + rs.getString("IDSWARM") + ".- " + rs.getString("IDPEER")+ ".- "+rs.getFloat("DESCARGADO"));
			}		
			
			
		} catch (Exception ex) {
			//System.err.println("\n # Error loading data in the db: " + ex.getMessage());
		}
		
		return arSwarm;
	}
	
public static ArrayList<Swarm> loadSwarms(ArrayList<String> infoHashes) {	
		
		String sqlString = "SELECT * FROM SWARM_PEER WHERE IDSWARM = ?";
		
		ArrayList<Swarm> arSwarm = new ArrayList<Swarm>();
		ArrayList<Peer> arPeer = SQLiteDBManager.loadPeers();
		
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
			
			for(int i = 0; i < infoHashes.size(); i++)
			{
				stmt.setString(1, infoHashes.get(i));
				
				ResultSet rs = stmt.executeQuery();
				
								
				while(rs.next())
				{
					ArrayList<Peer> peersForHash = new ArrayList<Peer>();
					
					for(Peer p : arPeer)
					{
						if(rs.getString("IP").equalsIgnoreCase(p.getIP()))
						{
							peersForHash.add(p);
						}
					}
					arSwarm.add(new Swarm(peersForHash, infoHashes.get(i)));	
				}
			}

		} catch (Exception ex) {
			//System.err.println("\n # Error loading data in the db: " + ex.getMessage());
		}
		
		return arSwarm;
	}
	
	
	public static void deleteSwarmPeers() {	
		String sqlString = "DELETE FROM SWARM_PEER";
		
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {						
			int deleted = stmt.executeUpdate();
			
			if (deleted > 0) {
				//System.out.println("\n - '" + deleted + "' peers were deleted.");
				con.commit();
			} else {
				//System.out.println("\n - None peer was deleted.");
				con.rollback();
			}				
		} catch (Exception ex) {
			//System.err.println("\n # Error cleaning the db: " + ex.getMessage());
		}
	}
	
	//##################################################################################

}
