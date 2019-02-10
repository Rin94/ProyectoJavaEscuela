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
 * Clase que maneja el activity de Lista carreras
 * @author Jared Daniel Salinas Gonzalez
 *
 */
public class ListaCarrerasActivity extends Activity {
	ListView listaCarreras;
	ArrayList<Carrera> datosCarreras;
	ArrayAdapter<Carrera> adapterCarreras;	
	public final static int NUEVA_CARRERA=100;
	public final static int CARRERA_EXISTENTE=101;
	public final static int OPERACION_ELIMINAR = 1000;
	public final static int OPERACION_MODIFICAR = 1001;
	public final static int OPERACION_AGREGAR = 1002;
	/**
	 * Metodo que inicializa o crea el activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_carreras);
		listaCarreras = (ListView)
				findViewById(R.id.lista_carreras);
		listaCarreras.setOnItemClickListener(
				new ManejadorListaCarreras());
	}
	/**
	 * Metodo que mantiene actualizado la lista de carreras dede el bundle
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		datosCarreras = (ArrayList<Carrera>) 
				savedInstanceState.get("carreras");
	}
	/**
	 * Metodo que manda a llamar el hilo que se encarga de poner la lista de carreras de list view
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (datosCarreras==null) {
			TareaBajaCarreras tarea =
					new TareaBajaCarreras();
			tarea.execute(getString(R.string.url_base)
					+"carrera");
		}
		else {
			adapterCarreras = new ArrayAdapter<Carrera>(
					ListaCarrerasActivity.this,
					android.R.layout.simple_list_item_single_choice,
					datosCarreras
				);
			listaCarreras.setAdapter(adapterCarreras);
		}
	}
	/**
	 * Metodo que guarda los datos de la lista de carreras
	 */
	@Override
	protected void onSaveInstanceState(
			Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (datosCarreras!=null) {
			outState.putSerializable("carreras", 
					datosCarreras);
		}
	}
	
	
	
	/**
	 * Metodo que maneja el evento del boton de agregar carrera, empieza el activity  de detalle carrera sin datos de carrera
	 * @param origenClick boton agregar
	 */
	public void nuevaCarrera(View origenClick) {
		Intent intent =
				new Intent(ListaCarrerasActivity.this,
						DetalleCarreraActivity.class);
		startActivityForResult(intent, NUEVA_CARRERA);
	}
	/**
	 * Clase interna  que  maneja el hilo que solicita al servidor una lista de carreras de la base de datos
	 * @author Jared Daniel Salinas Gonzalez
	 *
	 */
	private class TareaBajaCarreras
		extends AsyncTask<String, Void, List<Carrera>> 
	{
		/**
		 * Metodo que manda a llamar el servicio que obtiene una lista de carreras
		 * @param url del servicio
		 * @return una lista de carreras 
		 */
		@Override
		protected List<Carrera> doInBackground(String... params) {
			URL url=null;
			HttpURLConnection conn = null;
			BufferedReader entrada;
			List<Carrera> resultado=null;
			try {
				url = new URL(params[0]);
				conn = (HttpURLConnection)
						url.openConnection();
				conn.setRequestProperty("Accept", 
	                    "application/json");
				int codigo = conn.getResponseCode();
				System.out.println("Codigo :"+codigo);
				if (codigo==HttpURLConnection.HTTP_OK) {
					entrada = new BufferedReader(
					 new InputStreamReader(conn.getInputStream()));
					String respuesta=entrada.readLine();
					JSONArray datos = new JSONArray(respuesta);
					resultado = new ArrayList<Carrera>();
					for (int i=0; i<datos.length(); i++) {
						JSONObject elem = datos.getJSONObject(i);
						Carrera c = new Carrera();
						c.setClaveCarrera(elem.getString("claveCarrera"));
						c.setNombreCarrera(elem.getString("nombreCarrera"));
						resultado.add(c);
					}
					
					//System.out.println(respuesta);
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
				System.err.println("Error al conectarse a servicio de carreras");
				eio.printStackTrace();
			}
			
			
			return resultado;
		}
		/**
		 * Metodo que pasa al adapter la lista de carreras
		 */
		@Override
		protected void onPostExecute(List<Carrera> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			adapterCarreras = new ArrayAdapter<Carrera>(
					ListaCarrerasActivity.this,
					android.R.layout.simple_list_item_single_choice,
					result
				);
			listaCarreras.setAdapter(adapterCarreras);
			datosCarreras = (ArrayList<Carrera>)result;
		}
		
		
	}
	
	/**
	 * Clase interna que maneja los eventos de la lista de carreras
	 * @author Jared Daniel Salinas Gonzalez
	 * 
	 *
	 */
	private class ManejadorListaCarreras
		implements OnItemClickListener{


		/**
		 * Metodo que manda a llamar el activity de detalle carrera con la informacion de la carrera seleccionada
		 */
		@Override
		public void onItemClick(AdapterView<?> lista, 
				View arg1, 
				int selectedIndex,
				long arg3) {
			Intent intent =
					new Intent(ListaCarrerasActivity.this,
							DetalleCarreraActivity.class);
			intent.putExtra("detalle", 
					(Carrera)lista.getItemAtPosition(selectedIndex)
					);
			startActivityForResult(intent, CARRERA_EXISTENTE);
			
		}
		
	}

	/**
	 * Metodo que al recibir una operacion de agregar, eliminar o modificar, actualiza la lista de carreras
	 */
	@Override
	protected void onActivityResult(int requestCode, 
			int resultCode, 
			Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==CARRERA_EXISTENTE) {
			if (resultCode==RESULT_OK) {
				Bundle datos = data.getExtras();
				int operacion_realizada =
						datos.getInt("Operacion");
				Carrera c = (Carrera)
						datos.getSerializable("Carrera");
				adapterCarreras.remove(c);
				if (operacion_realizada == OPERACION_MODIFICAR) {
					adapterCarreras.add(c);
				}
			}
		}
		else {
			if (data!=null) {
                Bundle datos = data.getExtras();
                Carrera c = (Carrera)
                        datos.getSerializable("Carrera");
                adapterCarreras.add(c);
            }
			
		}
	}
	
	
	
	
}
