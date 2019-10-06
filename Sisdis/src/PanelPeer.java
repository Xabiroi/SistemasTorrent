import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class PanelPeer extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public PanelPeer() {
		this.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		this.add(scrollPane);
		
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

	}

}
