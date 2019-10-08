package Paneles;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelConfiguracion extends JPanel {
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField txtIdNoEditable;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public PanelConfiguracion() {
		this.setLayout(new GridLayout(0, 3, 0, 0));
		
		JLabel lblJms = new JLabel("JMS");
		this.add(lblJms);
		
		JLabel label = new JLabel("");
		this.add(label);
		
		JLabel label_1 = new JLabel("");
		this.add(label_1);
		
		JLabel lblip = new JLabel("-IP:");
		this.add(lblip);
		
		textField_4 = new JTextField();
		this.add(textField_4);
		textField_4.setColumns(10);
		
		JLabel label_2 = new JLabel("");
		this.add(label_2);
		
		JLabel lblpuerto = new JLabel("-Puerto:");
		this.add(lblpuerto);
		
		textField_5 = new JTextField();
		this.add(textField_5);
		textField_5.setColumns(10);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
		});
		this.add(btnConectar);
		
		JLabel label_4 = new JLabel("");
		this.add(label_4);
		
		JLabel label_5 = new JLabel("");
		this.add(label_5);
		
		JLabel label_6 = new JLabel("");
		this.add(label_6);
		
		JLabel lblIpMulticast_1 = new JLabel("IP Multicast:");
		this.add(lblIpMulticast_1);
		
		textField_6 = new JTextField();
		this.add(textField_6);
		textField_6.setColumns(10);
		
		JLabel label_9 = new JLabel("");
		this.add(label_9);
		
		JLabel lblMascara = new JLabel("Mascara: ");
		this.add(lblMascara);
		
		textField_7 = new JTextField();
		this.add(textField_7);
		textField_7.setColumns(10);
		
		JButton btnConectar_1 = new JButton("Conectar");
		btnConectar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		this.add(btnConectar_1);
		
		JLabel lblId = new JLabel("ID");
		this.add(lblId);
		
		txtIdNoEditable = new JTextField();
		txtIdNoEditable.setEditable(false);
		txtIdNoEditable.setText("id no editable");
		this.add(txtIdNoEditable);
		txtIdNoEditable.setColumns(10);
	}
	
    private static final String zeroTo255 = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";

	private static final String IP_REGEXP = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
	
	private static final Pattern IP_PATTERN = Pattern.compile(IP_REGEXP);
	
	// Return true when *address* is IP Address
	private boolean isValid(String address) {
		return IP_PATTERN.matcher(address).matches();
	}
	
	public static void main(String[] args) {
		PanelConfiguracion a= new PanelConfiguracion();
		System.out.println(a.isValid("192.168.1.2"));
	}
	
}
