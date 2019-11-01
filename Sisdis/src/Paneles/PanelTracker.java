package Paneles;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Objetos.Tracker;

public class PanelTracker extends JPanel {


	/**
	 * 
	 */
	private static final long serialVersionUID = -412970576837431423L;

	/**
	 * Create the panel.
	 */
	public PanelTracker() {
		this.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		this.add(scrollPane_1);
		
		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);

		//Valores de prueba, crear JTable personalizada con diferentes metodos
		model.addColumn("Ip");
		model.addColumn("Puerto");
		model.addColumn("Master");
		
		
		Tracker a = new Tracker(1,"192.168.1.2","65",true, System.currentTimeMillis());
		Tracker b = new Tracker(2,"192.168.1.3","64",false,System.currentTimeMillis());
		Tracker c = new Tracker(3,"192.168.1.4","63",false,System.currentTimeMillis());
		Tracker d = new Tracker(4,"192.168.1.5","89",false,System.currentTimeMillis());
		
		model.addRow(new Object[] {a.getIP(),a.getPuerto(),a.isMaster()});
		model.addRow(new Object[] {b.getIP(),b.getPuerto(),b.isMaster()});
		model.addRow(new Object[] {c.getIP(),c.getPuerto(),c.isMaster()});
		model.addRow(new Object[] {d.getIP(),d.getPuerto(),d.isMaster()});
		

		
		scrollPane_1.setViewportView(table);
	}

}
