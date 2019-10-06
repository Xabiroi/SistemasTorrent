import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class VentanaPrincipal {

	private JFrame frame;
	private PanelPeer PanelPeer;
private PanelTracker PanelTracker;
	private PanelConfiguracion PanelConfiguracion;

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

		frame.setBounds(100, 100, 419, 234);
	
		JTabbedPane panelDePestanas = new JTabbedPane(JTabbedPane.TOP);
		panelDePestanas.setBounds(10, 11, 383, 174);

		frame.getContentPane().add(panelDePestanas);

		PanelTracker = new PanelTracker();
		panelDePestanas.addTab("Trackers", null, PanelTracker, null);


		PanelPeer = new PanelPeer();
		panelDePestanas.addTab("Peers", null, PanelPeer, null);
		
		PanelConfiguracion = new PanelConfiguracion();

		panelDePestanas.addTab("Configuracion", null, PanelConfiguracion, null);

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
