import java.util.regex.Pattern;

import javax.swing.JPanel;

public class PanelConfiguracion extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public PanelConfiguracion() {

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
