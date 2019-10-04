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
import javax.swing.JTextField;
import javax.swing.JTree;
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
		// éste es el primer panel
		// que se añade como pestaña al panelDePestanas
		PanelTracker PanelTracker = new PanelTracker();
		panelDePestanas.addTab("Trackers", null, PanelTracker, null);
		// al panel le pongo distribución nula para
		// posicionar los elementos en las coordenadas que
		// quiera
		PanelTracker.setLayout(null);
		
//		JList list = new JList();
//		list.setBounds(0, 155, 398, -151);
//		PanelTracker.add(list);
		
//		//TODO una etiqueta en el panel de la pestaña 1
//		JLabel temp = new JLabel("Info de trackers");
//		temp.setBounds(10, 11, 348, 14);
//		PanelTracker.add(temp);
		
		// otro panel de igual forma
		PanelPeer PanelPeer = new PanelPeer();
		panelDePestanas.addTab("Peers", null, PanelPeer, null);
		PanelPeer.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		PanelPeer.add(scrollPane);
		
		JTree tree = new JTree();
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
