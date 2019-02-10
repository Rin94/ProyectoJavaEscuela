package edu.uaz.poo2.clienteescolar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
/**
 * Clase que representa la activity principal del programa cliente escolar
 * @author Jared Daniel Salinas Gonzalez
 *
 */
public class ControlEscolarActivity extends Activity {
	/**
	 * Metodo que crea el activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_escolar);
	}
	/**
	 * Metodo que maneja el evento del boton que muestra el activity de lista carreras
	 * @param origenClick boton sel..
	 */
	public void muestraListaCarreras(View origenClick) {
		Intent intent = new Intent(
				this,ListaCarrerasActivity.class);
		startActivity(intent);
	}
	/**
	 * Metodo que maneja el evento del boton que muestra el activity de lista materias
	 * @param origenClick boton sel..
	 */
	public void muestraListaMaterias(View origenClick){
		Intent intent= new Intent(this,ListaMateriasActivity.class);
		startActivity(intent);
	}
	/**
	 * Metodo que maneja el evento del boton que muestra el activity de lista periodos escolares
	 * @param origenClick boton sel..
	 */
	public void muestraListaPeriodos(View origenClick){
		Intent intent= new Intent(this,ListaPeriodoEscolarActivity.class);
		startActivity(intent);
		
	}
}
