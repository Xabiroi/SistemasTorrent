import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.BorderLayout;

public class VentanaPrincipal {

	private JFrame frame;
	private PanelPeer PanelPeer;
	private PanelTracker PanelTracker;
	private PanelConfiguracion PanelConfiguracion;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField txtIdNoEditable;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal window = new VentanaPrincipal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VentanaPrincipal() {
		initialize();
	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 613, 401);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//MIO FIXME
		frame.setBounds(100, 100, 419, 234);
	
		JTabbedPane panelDePestanas = new JTabbedPane(JTabbedPane.TOP);
		panelDePestanas.setBounds(10, 11, 383, 174);

		frame.getContentPane().add(panelDePestanas);

		PanelTracker PanelTracker = new PanelTracker();
		panelDePestanas.addTab("Trackers", null, PanelTracker, null);
		PanelTracker.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		PanelTracker.add(scrollPane_1);
		
		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);

		//Valores de prueba, crear JTable personalizada con diferentes metodos
		model.addColumn("Ip");
		model.addColumn("Puerto");
		model.addColumn("Master");
		
		
		Tracker a = new Tracker("192.168.1.2","65",true);
		Tracker b = new Tracker("192.168.1.3","64",true);
		Tracker c = new Tracker("192.168.1.4","63",true);
		Tracker d = new Tracker("192.168.1.5","89",true);
		
		model.addRow(new Object[] {a.getIP(),a.getPuerto(),a.isMaster()});
		model.addRow(new Object[] {b.getIP(),b.getPuerto(),b.isMaster()});
		model.addRow(new Object[] {c.getIP(),c.getPuerto(),c.isMaster()});
		model.addRow(new Object[] {d.getIP(),d.getPuerto(),d.isMaster()});
		

		
		scrollPane_1.setViewportView(table);
		
		// otro panel de igual forma
		PanelPeer PanelPeer = new PanelPeer();
		panelDePestanas.addTab("Peers", null, PanelPeer, null);
		PanelPeer.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		PanelPeer.add(scrollPane);
		
		//Ejemplo de como tendria que ser
		DefaultMutableTreeNode titulo = new DefaultMutableTreeNode("Swarms");
		DefaultMutableTreeNode subtitulo1 = new DefaultMutableTreeNode("Swarm#ABCD1234");
		DefaultMutableTreeNode subtitulo2 = new DefaultMutableTreeNode("Swarm#EFGH1234");
		DefaultMutableTreeNode subtitulo3 = new DefaultMutableTreeNode("Swarm#IJKL1234");
		
		Peer p1=new Peer("192.168.10.2","42");
		Peer p2=new Peer("192.168.10.3","41");
		Peer p3=new Peer("192.168.10.4","42");
		Peer p4=new Peer("192.168.10.5","46");
		Peer p5=new Peer("192.168.10.6","42");
		Peer p6=new Peer("192.168.10.7","67");
				
		titulo.add(subtitulo1);
		titulo.add(subtitulo2);
		titulo.add(subtitulo3);
		
		subtitulo1.add(new DefaultMutableTreeNode("Peer		"+p1.getIP()+":"+p1.getPuerto()));
		subtitulo2.add(new DefaultMutableTreeNode("Peer		"+p2.getIP()+":"+p2.getPuerto()));
		subtitulo2.add(new DefaultMutableTreeNode("Peer		"+p3.getIP()+":"+p3.getPuerto()));
		subtitulo2.add(new DefaultMutableTreeNode("Peer		"+p4.getIP()+":"+p4.getPuerto()));
		subtitulo3.add(new DefaultMutableTreeNode("Peer		"+p5.getIP()+":"+p5.getPuerto()));
		subtitulo3.add(new DefaultMutableTreeNode("Peer		"+p6.getIP()+":"+p6.getPuerto()));
		subtitulo3.add(new DefaultMutableTreeNode("Peer		"+p1.getIP()+":"+p1.getPuerto()));
		
		JTree tree = new JTree(titulo);


		
		scrollPane.setViewportView(tree);

		
//		JLabel temp2 = new JLabel("Info de peers");
//		temp2.setBounds(10, 11, 290, 14);
//		PanelPeer.add(temp2);
				
		//otro
		PanelConfiguracion PanelConfiguracion = new PanelConfiguracion();
		PanelConfiguracion.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
			}
		});
		panelDePestanas.addTab("Configuracion", null, PanelConfiguracion, null);
		PanelConfiguracion.setLayout(new GridLayout(0, 3, 0, 0));
		
		JLabel lblJms = new JLabel("JMS");
		PanelConfiguracion.add(lblJms);
		
		JLabel label = new JLabel("");
		PanelConfiguracion.add(label);
		
		JLabel label_1 = new JLabel("");
		PanelConfiguracion.add(label_1);
		
		JLabel lblip = new JLabel("-IP:");
		PanelConfiguracion.add(lblip);
		
		textField_4 = new JTextField();
		PanelConfiguracion.add(textField_4);
		textField_4.setColumns(10);
		
		JLabel label_2 = new JLabel("");
		PanelConfiguracion.add(label_2);
		
		JLabel lblpuerto = new JLabel("-Puerto:");
		PanelConfiguracion.add(lblpuerto);
		
		textField_5 = new JTextField();
		PanelConfiguracion.add(textField_5);
		textField_5.setColumns(10);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
		});
		PanelConfiguracion.add(btnConectar);
		
		JLabel label_4 = new JLabel("");
		PanelConfiguracion.add(label_4);
		
		JLabel label_5 = new JLabel("");
		PanelConfiguracion.add(label_5);
		
		JLabel label_6 = new JLabel("");
		PanelConfiguracion.add(label_6);
		
		JLabel lblIpMulticast_1 = new JLabel("IP Multicast:");
		PanelConfiguracion.add(lblIpMulticast_1);
		
		textField_6 = new JTextField();
		PanelConfiguracion.add(textField_6);
		textField_6.setColumns(10);
		
		JLabel label_9 = new JLabel("");
		PanelConfiguracion.add(label_9);
		
		JLabel lblMascara = new JLabel("Mascara: ");
		PanelConfiguracion.add(lblMascara);
		
		textField_7 = new JTextField();
		PanelConfiguracion.add(textField_7);
		textField_7.setColumns(10);
		
		JButton btnConectar_1 = new JButton("Conectar");
		btnConectar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		PanelConfiguracion.add(btnConectar_1);
		
		JLabel lblId = new JLabel("ID");
		PanelConfiguracion.add(lblId);
		
		txtIdNoEditable = new JTextField();
		txtIdNoEditable.setEditable(false);
		txtIdNoEditable.setText("id no editable");
		PanelConfiguracion.add(txtIdNoEditable);
		txtIdNoEditable.setColumns(10);
		frame.pack();

		
	}

	public PanelPeer getPanelPeer() {
		return PanelPeer;
	}

	public void setPanelPeer(PanelPeer panelPeer) {
		PanelPeer = panelPeer;
	}

	public PanelTracker getPanelTracker() {
		return PanelTracker;
	}

	public void setPanelTracker(PanelTracker panelTracker) {
		PanelTracker = panelTracker;
	}

	public PanelConfiguracion getPanelConfiguracion() {
		return PanelConfiguracion;
	}

	public void setPanelConfiguracion(PanelConfiguracion panelConfiguracion) {
		PanelConfiguracion = panelConfiguracion;
	}
}
