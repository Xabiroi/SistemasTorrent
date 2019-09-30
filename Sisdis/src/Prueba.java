import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;


public class Prueba extends JFrame {

 private JPanel contentPane;
 

 public static void main(String[] args) {
  EventQueue.invokeLater(new Runnable() {
   public void run() {
    try {
     Prueba frame = new Prueba();
     frame.setVisible(true);
    } catch (Exception e) {
     e.printStackTrace();
    }
   }
  });
 }


 public Prueba() {
  

  setTitle("Pestau00F1as con Swing by jnj");

  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  setBounds(100, 100, 419, 234);

  contentPane = new JPanel();
  contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
  setContentPane(contentPane);
  // distribución nula para poder posicionar los elementos
  // en las coordenadas que queramos
  contentPane.setLayout(null);
  

  JTabbedPane panelDePestanas = new JTabbedPane(JTabbedPane.TOP);
  panelDePestanas.setBounds(10, 11, 383, 174);
  contentPane.add(panelDePestanas);
  
  // éste es el primer panel
  // que se añade como pestaña al 'tabbedPane'
  JPanel panel1 = new JPanel();
  panelDePestanas.addTab("Panel 1", null, panel1, null);
  // al panel le pongo distribución nula para
  // posicionar los elementos en las coordenadas que
  // quiera
  panel1.setLayout(null);
  
  // una etiqueta en el panel de la pestaña 1
  JLabel lbl1 = new JLabel("Primera pestau00F1a..");
  lbl1.setBounds(10, 11, 348, 14);
  panel1.add(lbl1);
  
  // otro panel de igual forma
  JPanel panel2 = new JPanel();
  panelDePestanas.addTab("Panel 2", null, panel2, null);
  panel2.setLayout(null);
  
  // otra etiqueta ésta vez en el segundo panel
  JLabel lbl2 = new JLabel("Segunda pestau00F1a..");
  lbl2.setBounds(10, 11, 290, 14);
  panel2.add(lbl2);
 }
}