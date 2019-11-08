package BDUpdateTopicListener;

import javax.jms.Message;
import javax.jms.MessageListener;

public class BDTopicListener implements MessageListener {

	private int ContadorVersionBD;
	private String estadoActual;
	

	public BDTopicListener(int contadorVersionBD, String estadoActual) {
		super();
		ContadorVersionBD = contadorVersionBD;
		this.estadoActual = estadoActual;
	}


	//FUNCIONALIDAD DE MASTER (meter comprobaciones mas adelante)
//	public void comprobar() {
//		int i;
//		//evitar que sume numeros muy altos
//		if(Collections.min(SeguimientoKeepalive)==1000) {
//			for(i=0;i<SeguimientoKeepalive.size();i++) {
//				SeguimientoKeepalive.set(i, SeguimientoKeepalive.get(i)-1000);
//			}
//			setContadorKeepAlives(getContadorVersionBD()-1000);
//		}
//		//comprobar que no hay fallos de conexion 
//		for(i=0;i<SeguimientoKeepalive.size();i++) {
//			if(ContadorKeepAlives-SeguimientoKeepalive.get(i)>=3) {
//				//Si los keepalive se retrasan, se expulsa
//				SeguimientoKeepalive.remove(i);
//				trackers.remove(i);
//				System.out.println("Expulsado");
//			}
//			
//		}
//	}

	



	@Override
	//This method is call when a new Message arrives to the topic
	public void onMessage(Message message) {		
		if (message != null) {
			try {
				//TODO cambiar de estado normal a sugerencia cuando llegue un mensaje de sugerencia
			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}		
	}


	public int getContadorVersionBD() {
		return ContadorVersionBD;
	}


	public void setContadorVersionBD(int contadorVersionBD) {
		ContadorVersionBD = contadorVersionBD;
	}

	public String getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(String estadoActual) {
		this.estadoActual = estadoActual;
	}
}