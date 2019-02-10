package edu.uaz.poo2.clienteescolar;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * Clase que maneja el activity de Lista Periodo Escolar
 * @author Jared Daniel Salinas Gonzalez
 *
 */
public class ListaPeriodoEscolarActivity extends Activity {
	ListView listaPeriodo;
	ArrayList<PeriodoEscolar> datosPeriodos;
	ArrayAdapter<PeriodoEscolar> adapterPeriodos;	
	public final static int NUEVO_PERIODO=100;
	public final static int PERIODO_EXISTENTE=101;
	public final static int OPERACION_ELIMINAR = 1000;
	public final static int OPERACION_MODIFICAR = 1001;
	public final static int OPERACION_AGREGAR = 1002;
	/**
	 * Metodo que inicializa o crea el activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_periodos);
		listaPeriodo = (ListView)
				findViewById(R.id.lista_periodos);
		listaPeriodo.setOnItemClickListener(
				new ManejadorListaPeriodos());
	}
	/**
	 * Metodo que mantiene actualizado la lista de periodos escolares dede el bundle
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		datosPeriodos = (ArrayList<PeriodoEscolar>) 
				savedInstanceState.get("periodos");
	}
	/**
	 * Metodo que manda a llamar el hilo que se encarga de poner la lista de periodos escolares del list view
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (datosPeriodos==null) {
			TareaBajaPeriodos tarea =
					new TareaBajaPeriodos();
			tarea.execute(getString(R.string.url_base)
					+"periodoescolar");
		}
		else {
			adapterPeriodos = new ArrayAdapter<PeriodoEscolar>(
					ListaPeriodoEscolarActivity.this,
					android.R.layout.simple_list_item_single_choice,
					datosPeriodos
				);
			listaPeriodo.setAdapter(adapterPeriodos);
		}
	}
	/**
	 * Metodo que guarda los datos de la lista de periodos escolares
	 */
	@Override
	protected void onSaveInstanceState(
			Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (datosPeriodos!=null) {
			outState.putSerializable("periodos", 
					datosPeriodos);
		}
	}
	
	
	
	/**
	 * Metodo que maneja el evento del boton de agregar periodo escolar, empieza el activity  de detalle periodo escolar sin pasar
	 * datos de periodo escolar
	 * @param origenClick boton agregar
	 */
	public void nuevoPeriodo(View origenClick) {
		/*Intent intent =
				new Intent(ListaPeriodoEscolarActivity.this,
						DetallePeriodoEscolarActivity.class);
		startActivityForResult(intent, NUEVO_PERIODO);*/
		TareaBajaSigPeriodos tarea =
				new TareaBajaSigPeriodos();
		tarea.execute(getString(R.string.url_base)
				+"periodoescolar/buscasigidperiodo");
	}
	/**
	 * Clase interna  que  maneja el hilo que solicita al servidor una lista de periodos escolares de la base de datos
	 * @author Jared Daniel Salinas Gonzalez
	 *
	 */
	private class TareaBajaPeriodos
		extends AsyncTask<String, Void, List<PeriodoEscolar>> 
	{
		/**
		 * Metodo que manda a llamar el servicio que obtiene una lista de periodos escolares
		 * @param url del servicio
		 * @return una lista de periodos escolares
		 */
		@Override
		protected List<PeriodoEscolar> doInBackground(String... params) {
			URL url=null;
			HttpURLConnection conn = null;
			BufferedReader entrada;
			List<PeriodoEscolar> resultado=null;
			try {
				url = new URL(params[0]);
				conn = (HttpURLConnection)
						url.openConnection();
				conn.setRequestProperty("Accept", 
	                    "application/json");
				int codigo = conn.getResponseCode();
				if (codigo==HttpURLConnection.HTTP_OK) {
					entrada = new BufferedReader(
					 new InputStreamReader(conn.getInputStream()));
					String respuesta=entrada.readLine();
					JSONArray datos = new JSONArray(respuesta);
					resultado = new ArrayList<PeriodoEscolar>();
					for (int i=0; i<datos.length(); i++) {
						JSONObject elem = datos.getJSONObject(i);
						PeriodoEscolar pe = new PeriodoEscolar();
						pe.setIdPeriodo(Integer.parseInt((elem.getString("idPeriodo"))));
						pe.setPeriodo(Short.parseShort((elem.getString("periodo"))));
						pe.setYear(Short.parseShort(elem.getString("year")));
						resultado.add(pe);
					}
					
					System.out.println(respuesta);
				}
				conn.disconnect();
			}
			catch (MalformedURLException eu) {
				System.err.println("URL invalido");
			}
			catch (JSONException ejson) {
				System.err.println("Error al convertir datos JSON");
			}
			catch (IOException eio) {
				System.err.println("Error al conectarse a servicio de periodos");
				eio.printStackTrace();
			}						
			return resultado;
		}
		
		/**
		 * Metodo que pasa al adapter la lista de periodos escolares
		 */
		@Override
		protected void onPostExecute(List<PeriodoEscolar> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			adapterPeriodos = new ArrayAdapter<PeriodoEscolar>(
					ListaPeriodoEscolarActivity.this,
					android.R.layout.simple_list_item_single_choice,
					result
				);
			listaPeriodo.setAdapter(adapterPeriodos);
			datosPeriodos = (ArrayList<PeriodoEscolar>)result;
		}
		
		
	}
	
	/**
	 * Clase interna que maneja los eventos de la lista de periodos escolares
	 * @author Jared Daniel Salinas Gonzalez
	 * 
	 */
	private class ManejadorListaPeriodos
		implements OnItemClickListener{


		/**
		 * Metodo que manda a llamar el activity de detalle materias con la informacion del periodo escolar seleccionado
		 */
		@Override
		public void onItemClick(AdapterView<?> lista, 
				View arg1, 
				int selectedIndex,
				long arg3) {
			
			Intent intent =
					new Intent(ListaPeriodoEscolarActivity.this,
							DetallePeriodoEscolarActivity.class);
			intent.putExtra("detalle", 
					(PeriodoEscolar)lista.getItemAtPosition(selectedIndex)
					);
			startActivityForResult(intent, PERIODO_EXISTENTE);
			
			
		}
		
	}

	/**
	 * Metodo que al recibir una operacion de agregar, eliminar o modificar, actualiza la lista de periodos escolares
	 */
	@Override
	protected void onActivityResult(int requestCode, 
			int resultCode, 
			Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==PERIODO_EXISTENTE) {
			if (resultCode==RESULT_OK) {
				Bundle datos = data.getExtras();
				int operacion_realizada =
						datos.getInt("Operacion");
				PeriodoEscolar pe = (PeriodoEscolar)
						datos.getSerializable("PeriodoEscolar");
				adapterPeriodos.remove(pe);
				if (operacion_realizada == OPERACION_MODIFICAR) {
					adapterPeriodos.add(pe);
				}
			}
		}
		else {
			if(data!=null){
				Bundle datos = data.getExtras();
				PeriodoEscolar pe = (PeriodoEscolar)
						datos.getSerializable("PeriodoEscolar");
				adapterPeriodos.add(pe);
				
			}
			
		}
	}
	/**
	 * Clase interna que maneja el hilo que consigue el siguente id de periodo escolar para agregar un nuevo periodo escolar
	 * en la base de datos desde el activity de detalle periodo escolar
	 * @author Jared Daniel Salinas Gonzalez
	 *
	 */
	private class TareaBajaSigPeriodos
	extends AsyncTask<String, Void, String> 
	{
	/**
	 * Metodo que solicita al servidor el servicio que obtiene el siguiente id del periodo escolar
	 * @param url del servicio
	 * @return una cadena con el siguiente id
	 */
	@Override
	protected String doInBackground(String... params) {
		URL url=null;
		HttpURLConnection conn = null;
		BufferedReader entrada;
		String resultado=null;
		try {
			url = new URL(params[0]);
			conn = (HttpURLConnection)
					url.openConnection();
			conn.setRequestProperty("Accept", 
                    "text/plain");
			int codigo = conn.getResponseCode();
			if (codigo==HttpURLConnection.HTTP_OK) {
				entrada = new BufferedReader(
				 new InputStreamReader(conn.getInputStream()));
				//String respuesta=entrada.readLine();
				
				
				//PeriodoEscolar pe = new PeriodoEscolar();
				//pe.setIdPeriodo(Integer.parseInt((elem.getString("idPeriodo"))));
				resultado=entrada.readLine();
					
				
				
				System.out.println(resultado);
			}
			conn.disconnect();
		}
		catch (MalformedURLException eu) {
			System.err.println("URL invalido");
		}
		
		catch (IOException eio) {
			System.err.println("Error al conectarse a servicio de periodos");
			eio.printStackTrace();
		}						
		return resultado;
	}
	/**
	 * Inicializa el activity de detalle periodo escolar para agregar un nuevo periodo esocolar en la base de datos
	 */
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		PeriodoEscolar peNuevo= new PeriodoEscolar(Integer.parseInt(result));
		Intent intent =
				new Intent(ListaPeriodoEscolarActivity.this,
						DetallePeriodoEscolarActivity.class);
		intent.putExtra("detalle", 
				(PeriodoEscolar)peNuevo
				);
		startActivityForResult(intent, NUEVO_PERIODO);
		
		
	}
	
	
}
	
	
	
	
}
