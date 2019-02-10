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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
/**
 * 
 * @author Jared Daniel Salinas Gonzalez
 * Clase que maneja la activity de DetallePeriodoEscolar
 *
 */
public class DetallePeriodoEscolarActivity extends Activity {
	
	EditText idPeriodo;
	EditText year;
	Spinner semestre;
	ArrayAdapter adapter;
	Button botonEliminar;
	Button botonModificar;
	PeriodoEscolar pe;
	Short periodoSeleccionado;
	boolean esNuevoPeriodo;
	boolean esPrimero;
	boolean fuePresionado;
	String ruta="";
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
	 * Clase que crea el activity 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_periodo);
		idPeriodo = (EditText) findViewById(R.id.id_periodo);
		year = (EditText) findViewById(R.id.anio_periodo);
		semestre = (Spinner) findViewById(R.id.spinner_periodos);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.spinnerPeriodos,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		
		semestre.setAdapter(adapter);
		botonEliminar = (Button) findViewById(R.id.boton_eliminar_periodo);
		botonModificar = (Button) findViewById(R.id.boton_modificar_periodo);
		creaDialogoConfirmacion();
		obtenDatos();
	}
	/**
	 * Metodo que crea un dialogo de confirmacion, cuando se desea eliminar un periodo escolar de la base de datos
	 */
	private void creaDialogoConfirmacion() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Se le pone un titulo
		builder.setTitle(R.string.etiqueta_titulo_confirmacion_eliminacion);
		// Se le pone un texto
		builder.setMessage(R.string.etiqueta_confirmacion_eliminacion_periodo);
		// Se indica quien maneja el evento de dar click en SI
		builder.setPositiveButton(R.string.etiqueta_si,
				new ManejadorConfirmacion());
		// Se indica quien maneja el evento de dar click en NO
		builder.setNegativeButton(R.string.etiqueta_no, null);
		// Se crea el cuadro de dialogo con la informacion especificada
		dialogoConfirmacionEliminacion = builder.create();
	}
	/**
	 * 
	 * @author Jared Daniel Salinas Gonzalez
	 * Clase que maneja los eventos del los dialogos de eliminacion, si se desea eliminar el periodo escolar crea un hilo
	 * para solicitar la eliminacion del periodo escolar
	 *
	 */
	private class ManejadorConfirmacion implements
			DialogInterface.OnClickListener {
		/**
		 * Metodo que crea el hilo de mandar mallar el servicio que elimina el periodo escolar seleccionado por el usuario
		 */
		@Override
		public void onClick(DialogInterface dialog, int which) {
			TareaEliminaPeriodo tarea = new TareaEliminaPeriodo();
			cambiarRuta("eliminarperiodoescolar/");
			tarea.execute(getString(R.string.url_base) + "periodoescolar/"+ruta
					+ pe.getIdPeriodo());
		}

	}
	/**
	 * Metodo que obtiene los datos de un periodo escolar si es que existen y si no, se cambia el texto del boton para que
	 * se encarge de agregar el periodo
	 */
	private void obtenDatos() {
		Intent intent = getIntent();
		pe = (PeriodoEscolar) intent.getSerializableExtra("detalle");
		idPeriodo.setEnabled(false);
		
		if (pe.getYear() != 0) {
			ArrayAdapter myAdap = (ArrayAdapter) semestre.getAdapter();
			idPeriodo.setText(pe.getIdPeriodo().toString());
			year.setText(pe.getYear() + "");
			if (pe.getPeriodo() == 2) {
				//System.out.println("Enero-Junio");
				periodoSeleccionado = pe.getPeriodo();
				int spinnerPosition=myAdap.getPosition("Enero-Junio");
				semestre.setSelection(spinnerPosition);
				//semestre.getItemAtPosition(0);
				//adapter.getItem(0);;

			} else if (pe.getPeriodo() == 1) {
				//System.out.println("Agosto-Diciembre");
				periodoSeleccionado = pe.getPeriodo();
				//semestre.setSelection(1);
				//semestre.getItemAtPosition(1);
				//adapter.getItem(1);
				int spinnerPosition=myAdap.getPosition("Agosto-Diciembre");
				semestre.setSelection(spinnerPosition);

			} else if (pe.getPeriodo() == 3) {
				System.out.println("Verano");
				periodoSeleccionado = pe.getPeriodo();
				//semestre.setSelection(3);
				//semestre.getItemAtPosition(2);
				int spinnerPosition=myAdap.getPosition("Verano");
				semestre.setSelection(spinnerPosition);
				
				
				//adapter.getItem(2);

			}
			//idPeriodo.setEnabled(false);
			cambiarRuta("modificarperiodoescolar/");
			esNuevoPeriodo = false;
		} else {
			idPeriodo.setText(pe.getIdPeriodo().toString());
			botonEliminar.setVisibility(View.INVISIBLE);
			botonModificar.setText(R.string.etiqueta_boton_agregar_periodo);
			esNuevoPeriodo = true;
			periodoSeleccionado=pe.periodoSpinn();
			cambiarRuta("agregarperiodoescolar/");
		}
		semestre.setOnItemSelectedListener(new ManejadorSpinners());
	}
	/**
	 * Metodo que regresa a la activity anterior
	 * @param origenClick
	 */
	public void regresaALista(View origenClick) {
		super.onBackPressed();
	}
	/**
	 * Metodo que regresa a la activity anterior con un resultado de la operacion que se realizo
	 * @param operacion operacion de agregar, modificar o eliminar un periodo escolar
	 */
	public void regresaDatos(int operacion) {
		Intent datos = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("Operacion", operacion);
		bundle.putSerializable("PeriodoEscolar", pe);
		datos.putExtras(bundle);
		setResult(RESULT_OK, datos);
		finish();
	}
	/**
	 * Metodo que maneja el evento del boton de eliminar periodo escolar
	 * @param origenClick boton eliminar
	 */
	public void eliminaPeriodoEscolar(View origenClick) {
		// Pide confirmacion de la eliminacion
		dialogoConfirmacionEliminacion.show();
	}
	/**
	 * Metodo  que maneja el evento del boton de agregar/modificar un periodo escolar, checa si los datos obligatorios
	 * fueron puesto y manda a llamar al hilo  que maneja el almacenamiento de dicho periodo escolar
	 * @param origenClick boton
	 * 
	 */
	public void guardaPeriodo(View origenClick) {
		System.out.println(fuePresionado);
		Integer anioValido=null;
		if (idPeriodo.getText().toString().isEmpty()
				|| year.getText().toString().isEmpty() || periodoSeleccionado==null) {
			Toast.makeText(this, R.string.etiqueta_error_carreras,
					Toast.LENGTH_LONG).show();
			return;
		}
		if(anioValido.parseInt(year.getText().toString())>2014||anioValido.parseInt(year.getText().toString())<2000){
			Toast.makeText(this, R.string.etiqueta_error_anio,
					Toast.LENGTH_LONG).show();
			return;
			
			
		}
		
		if (pe == null) {
			pe = new PeriodoEscolar();
		}
		pe.setIdPeriodo(Integer.parseInt(idPeriodo.getText().toString()));
		pe.setYear(Short.parseShort(year.getText().toString()));
		pe.setPeriodo(periodoSeleccionado);
		System.out.println("Para evitar nullo"+pe.getPeriodo());
		TareaGuardaPeriodo tarea = new TareaGuardaPeriodo();
		tarea.execute(getString(R.string.url_base) + "periodoescolar/"+ruta);
	}
	
	/**
	 * Clase interna que maneja el hilo que elimina un periodo escolar de la base de datos
	 * @author Jared Daniel Salinas Gonzalez
	 *
	 */
	private class TareaEliminaPeriodo extends AsyncTask<String, Void, Boolean> {
		/**
		 * Metodo que solicita el servicio de eliminar una materia
		 * @return falso si no se pudo, verdadero caso contrario
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
					System.out.println("codigo de error: " + codigo);
					return false;
				}
			} catch (MalformedURLException eu) {
				System.err.println("URL invalido");
			} catch (IOException eio) {
				System.err
						.println("Error al conectarse a servicio de eliminar carrera");
				eio.printStackTrace();
			}
			return false;
		}
		/**
		 * Metodo que manda un mensaje con el resultado de la operacion y usa el metodo de regresaDatos con la operacion
		 * de eliminar
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(DetallePeriodoEscolarActivity.this,
						R.string.aviso_periodo_eliminada, Toast.LENGTH_LONG)
						.show();
				regresaDatos(ListaCarrerasActivity.OPERACION_ELIMINAR);
			} else {
				Toast.makeText(DetallePeriodoEscolarActivity.this,
						R.string.aviso_periodo_no_eliminada, Toast.LENGTH_LONG)
						.show();
			}
		}
	}
	/**
	 * Clase que maneja el hilo que modifica o agrega un periodoescolar de la base de datos
	 * @author Jared Daniel Salinas Gonzalez
	 *
	 */
	private class TareaGuardaPeriodo extends AsyncTask<String, Void, Boolean> {

		@Override
		/**
		 * Metodo que manda a llamar el servicio de agregar o modificar un periodo escolar segun sea el caso
		 * @param URL del servidor y el servicio solocitado
		 * @return falso si no se pudo agregar/modificar, true caso contrario
		 */
		protected Boolean doInBackground(String... params) {
			URL url = null;
			HttpURLConnection conn = null;
			String strUrl = params[0];
			try {
				if (!esNuevoPeriodo) {
					strUrl += "/" + pe.getIdPeriodo();
				}
				url = new URL(strUrl);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				if (esNuevoPeriodo) {
					conn.setRequestMethod("POST");
				} else {
					conn.setRequestMethod("PUT");
				}
				Map<String, String> mapaDatos = new HashMap<String, String>();
				mapaDatos.put("idPeriodo", pe.getIdPeriodo().toString());
				mapaDatos.put("year", pe.getYear() + "");
				mapaDatos.put("periodo", periodoSeleccionado + "");
				System.out.println("Mapa: " + mapaDatos);
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
				System.err
						.println("Error al conectarse a servicio de guardar periodo");
				eio.printStackTrace();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			return false;
		}
		/**
		 * Metodo que manda un mensaje si se pudo o no se pudo agregar/modifcar el periodo y usa el metodo regresaDatos
		 * con la operacion indicada segun sea el caso
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(DetallePeriodoEscolarActivity.this,
						R.string.aviso_periodo_guardada, Toast.LENGTH_LONG)
						.show();
				if (!esNuevoPeriodo) {
					regresaDatos(ListaPeriodoEscolarActivity.OPERACION_MODIFICAR);
				} else {
					regresaDatos(ListaPeriodoEscolarActivity.OPERACION_AGREGAR);
				}
			} else {
				Toast.makeText(DetallePeriodoEscolarActivity.this,
						R.string.aviso_periodo_no_guardada, Toast.LENGTH_LONG)
						.show();
			}
		}

		
	}
	/**
	 * Clase que maneja los eventos del spinner de los periodo "Enero-Junio", "Agosto-Diciembre", "verano" 
	 * @author Jared Daniel Salinas Gonzalez
	 *
	 */
	private class ManejadorSpinners implements
	OnItemSelectedListener {
		/**
		 * Metodo que muestra que semestre se escogio y usa esa informacion para agregar o modificar un periodo escolar
		 * en la base de datos
		 */
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {


			if (esPrimero == false) {
				esPrimero = true;

			} else{
				System.out.println("Spinn: "+semestre.getItemAtPosition(arg2));
				semestre.getItemAtPosition(arg2);
				if (arg2 == 0) {
					System.out.println();
					
					fuePresionado=true;
					

				} else if (arg2 == 1) {
		
					fuePresionado=true;

				} else if (arg2 == 2) {
		
					fuePresionado=true;
				}
				else{
					fuePresionado=false;
				}
				periodoSeleccionado=
						pe.periodoSpinn(semestre.getItemAtPosition(arg2));
				System.out.println("Periodo: "+periodoSeleccionado);
	
			}
		}
		/**
		 * Metodo no utilizado
		 */
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			fuePresionado=false;
	
		}
	}

}
