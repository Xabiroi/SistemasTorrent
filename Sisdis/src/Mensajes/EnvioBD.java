package Mensajes;

import java.io.File;

public class EnvioBD {
	private File BaseDeDatos;
	
	public EnvioBD() {
		
	}
	public EnvioBD(File BaseDeDatos) {
		this.BaseDeDatos=BaseDeDatos;
	}

	public File getBaseDeDatos() {
		return BaseDeDatos;
	}

	public void setBaseDeDatos(File baseDeDatos) {
		BaseDeDatos = baseDeDatos;
	}
}
