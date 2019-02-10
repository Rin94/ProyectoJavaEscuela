package edu.uaz.poo2.clienteescolar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 
 * @author Clase que maneja la activity de Detalle Carrera
 *
 */
public class DetalleCarreraActivity extends Activity {
	EditText claveCarrera;
	EditText nombreCarrera;
	Button botonEliminar;
	Button botonModificar;
	Carrera c;
	String ruta="";
	boolean esNuevaCarrera;
	
	// Dialogo para pedir confirmacion
	private AlertDialog dialogoConfirmacionEliminacion;
	/**
	 * Metodo que verifica si se esta usando el servidor de JSG y si si es cambia la ruta por el parametro
	 * @param uri Ruta que se quiere anexar en el path
	 */
	public void cambiarRuta(String uri){
		String urlAComparar=getString(R.string.url_base);
		System.out.println(urlAComparar);
		String arreglo[]=urlAComparar.split("0/+");
		if(arreglo[1].equals("ServidorRESTControlEscolarJSG/webresources/")){
			ruta=uri;
		}
		else{
			ruta="";
		}
		
	}
	/**
	 * Metodo que se ejecuta al inicializar la activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_carrera);
		claveCarrera = (EditText) findViewById(R.id.clave_carrera);
		nombreCarrera = (EditText) findViewById(R.id.nombre_carrera);
		botonEliminar = (Button) findViewById(R.id.boton_eliminar_carrera);
		botonModificar = (Button) findViewById(R.id.boton_modificar_carrera);
		creaDialogoConfirmacion();
		obtenDatos();
	}
	/**
	 * Metodo que crea un dialogo para verificar si un usuario quiere eliminar una carrera o no
	 */
	private void creaDialogoConfirmacion() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Se le pone un titulo
		builder.setTitle(R.string.etiqueta_titulo_confirmacion_eliminacion);
		// Se le pone un texto		
		builder.setMessage(R.string.etiqueta_confirmacion_eliminacion_carrera);
		// Se indica quien maneja el evento de dar click en SI		
		builder.setPositiveButton(R.string.etiqueta_si, new ManejadorConfirmacion());
		// Se indica quien maneja el evento de dar click en NO		
		builder.setNegativeButton(R.string.etiqueta_no, null);
		// Se crea el cuadro de dialogo con la informacion especificada
		dialogoConfirmacionEliminacion = builder.create();
	}

	/**
	 * Clase interna que maneja los eventos del dialogo
	 * @author Jared Daniel Salinas Gonzalez
	 * crea un hilo de eliminar carrera con la url con el servicio correspondiente
	 *
	 */
	private class ManejadorConfirmacion
		implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			TareaEliminaCarrera tarea = new TareaEliminaCarrera();
			cambiarRuta("eliminacarrera/");
			tarea.execute(getString(R.string.url_base) + "carrera/"+ruta
					+ c.getClaveCarrera());			
		}
		
	}
	/**
	 * Metodo que obtienen datos de una carrera existente y los llena en el activiy caso contrario el boton de modificar
	 * cambia a agregar
	 */
	private void obtenDatos() {
		Intent intent = getIntent();
		c = (Carrera) intent.getSerializableExtra("detalle");
		if (c != null) {
			claveCarrera.setText(c.getClaveCarrera());
			nombreCarrera.setText(c.getNombreCarrera());
			claveCarrera.setEnabled(false);
			esNuevaCarrera = false;
			cambiarRuta("modificacarrera/");
			
			
		} else {
			botonEliminar.setVisibility(View.INVISIBLE);
			botonModificar.setText(R.string.etiqueta_boton_agregar_carrera);
			esNuevaCarrera = true;
			cambiarRuta("agregarcarrera/");
		}
	}
	
	/**
	 * Metodo que regresa a la activity anterior
	 * @param origenClick
	 */
	public void regresaALista(View origenClick) {
		super.onBackPressed();
	}
	/**
	 * Metodo que regresa a la activity anterior con los cambios realizados en la base de datos
	 * @param operacion la operacion de agregar modificar o eliminar
	 */
	public void regresaDatos(int operacion) {
		Intent datos = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("Operacion", operacion);
		bundle.putSerializable("Carrera", c);
		datos.putExtras(bundle);
		setResult(RESULT_OK, datos);
		finish();
	}
	/**
	 * Metodo que maneja el evento del boton de eliminar carrera que manda a llamar al dialogo de confirmacion de eliminacion
	 * @param origenClick el boton
	 */
	public void eliminaCarrera(View origenClick) {
		// Pide confirmacion de la eliminacion
		dialogoConfirmacionEliminacion.show();
	}
	/**
	 * Metodo que guarda la info de una carrera en la base de datos y se manda a llamar el hilo que maneja los cambios en 
	 * la base de datos
	 * @param origenClick
	 */
	public void guardaCarrera(View origenClick) {
		if (claveCarrera.getText().toString().isEmpty()
				|| nombreCarrera.getText().toString().isEmpty()) {
			Toast.makeText(this,
					R.string.etiqueta_error_carreras,					
					Toast.LENGTH_LONG).show();
			return;
		}
		if (c == null) {
			c = new Carrera();
		}
		c.setClaveCarrera(claveCarrera.getText().toString());
		c.setNombreCarrera(nombreCarrera.getText().toString());
		TareaGuardaCarrera tarea = new TareaGuardaCarrera();
		tarea.execute(getString(R.string.url_base) + "carrera/"+ruta);
	}
	/**
	 * Clase que maneja el hilo que manda a llamar el servicio de eliminar una carrera
	 * @author Jared Daniel Salinas Gonzalez
	 * 
	 *
	 */
	private class TareaEliminaCarrera extends AsyncTask<String, Void, Boolean> {
		/**
		 * Metodo que solicita el servicio de eliminar una carrera en la base de datos
		 * @return falso si no se pudo eliminar, verdadero caso contrario
		 */
		@Override
		protected Boolean doInBackground(String... params) {
			URL url = null;
			HttpURLConnection conn = null;
			try {
				url = new URL(params[0]);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("DELETE");

				int codigo = conn.getResponseCode();
				conn.disconnect();
				if (codigo / 100 == 2) {
					return true;
				} else {
					System.out.println("codigo de error: "+codigo);
					return false;
				}
			} catch (MalformedURLException eu) {
				System.err.println("URL invalido");
			} catch (IOException eio) {
				System.err.println("Error al conectarse a servicio de eliminar carrera");
				eio.printStackTrace();
			}
			return false;
		}
		/**
		 * Metodo que manda un mensaje si se pudo o no se pudo eliminar la carrera y usa el metodo regresa datos
		 * para regresar a la activity anterior con la operacion eliminar
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(DetalleCarreraActivity.this,
						R.string.aviso_carrera_eliminada,
						Toast.LENGTH_LONG).show();
				regresaDatos(ListaCarrerasActivity.OPERACION_ELIMINAR);
			} else {
				Toast.makeText(DetalleCarreraActivity.this,
						R.string.aviso_carrera_no_eliminada,
						Toast.LENGTH_LONG)
						.show();
			}
		}
	}
	/**
	 * Clase que maneja el hilo que solicita la modificacion o la agregacion de una carrera
	 * @author Jared Daniel Salinas Gonzalez
	 *
	 */
	private class TareaGuardaCarrera extends AsyncTask<String, Void, Boolean> {
		/**
		 * Metodo que manda al servicio que  modifica o agrega una carrera
		 * @return verdadero si se pudo, falso caso contrario
		 */
		@Override
		protected Boolean doInBackground(String... params) {
			URL url = null;
			HttpURLConnection conn = null;
			String strUrl = params[0];
			try {
				if (!esNuevaCarrera) {
					strUrl+="/"+c.getClaveCarrera();
				}
				url = new URL(strUrl);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				if (esNuevaCarrera) {					
					conn.setRequestMethod("POST");
				}
				else {
					conn.setRequestMethod("PUT");
				}				
				Map<String,String> mapaDatos = new HashMap<String, String>();
				mapaDatos.put("claveCarrera",c.getClaveCarrera());
				mapaDatos.put("nombreCarrera",c.getNombreCarrera());
				System.out.println("Mapa: "+mapaDatos);
				JSONObject obj = new JSONObject(mapaDatos);
				String datos = obj.toString();
				byte info[] = datos.getBytes();
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Content-Length", 
						Integer.toString(info.length));
				OutputStream os = conn.getOutputStream();
				os.write(info);
				int codigo = conn.getResponseCode();
				conn.disconnect();
				if (codigo / 100 == 2) {
					return true;
				} else {
					return false;
				}
			} catch (MalformedURLException eu) {
				System.err.println("URL invalido");
				eu.printStackTrace();
			} catch (IOException eio) {
				System.err.println("Error al conectarse a servicio de guardar carrera");
				eio.printStackTrace();
			}
			catch (Exception ee) {
				ee.printStackTrace();
			}
			return false;
		}
		/**
		 * Metodo que muestra un aviso si se pudo guardar o no la carrera y usa el metodo
		 * regresaDatos con la operacion correspondiente
		 * @param booleano resultado de la operacion doInBackGround
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(DetalleCarreraActivity.this,
						R.string.aviso_carrera_guardada, Toast.LENGTH_LONG)
						.show();
				if (!esNuevaCarrera) {
					regresaDatos(ListaCarrerasActivity.OPERACION_MODIFICAR);
				} else {
					regresaDatos(ListaCarrerasActivity.OPERACION_AGREGAR);
				}
			} else {
				Toast.makeText(DetalleCarreraActivity.this,
						R.string.aviso_carrera_no_guardada,						
						Toast.LENGTH_LONG).show();
			}
		}

	}
}
