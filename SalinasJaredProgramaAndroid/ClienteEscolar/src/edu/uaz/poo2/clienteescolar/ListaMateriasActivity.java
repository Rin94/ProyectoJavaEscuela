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
 * Clase que maneja el activity de Lista Materias
 * @author Jared Daniel Salinas Gonzalez
 *
 */
public class ListaMateriasActivity extends Activity {
	ListView listaMaterias;
	ArrayList<Materia> datosMaterias;
	
	ArrayAdapter<Materia> adapterMaterias;	
	public final static int NUEVA_MATERIA=100;
	public final static int MATERIA_EXISTENTE=101;
	public final static int OPERACION_ELIMINAR = 1000;
	public final static int OPERACION_MODIFICAR = 1001;
	public final static int OPERACION_AGREGAR = 1002;
	/**
	 * Metodo que inicializa o crea el activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_materias);
		listaMaterias = (ListView)
				findViewById(R.id.lista_materias);
		listaMaterias.setOnItemClickListener(
				new ManejadorListaMaterias());
	}
	/**
	 * Metodo que mantiene actualizado la lista de materias dede el bundle
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		datosMaterias = (ArrayList<Materia>) 
				savedInstanceState.get("materias");
	}
	/**
	 * Metodo que manda a llamar el hilo que se encarga de poner la lista de materias del list view
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (datosMaterias==null) {
			TareaBajaMaterias tarea =
					new TareaBajaMaterias();
			tarea.execute(getString(R.string.url_base)
					+"materia");
		}
		else {
			adapterMaterias = new ArrayAdapter<Materia>(
					ListaMateriasActivity.this,
					android.R.layout.simple_list_item_single_choice,
					datosMaterias
				);
			listaMaterias.setAdapter(adapterMaterias);
		}
	}
	/**
	 * Metodo que guarda los datos de la lista materias
	 */
	@Override
	protected void onSaveInstanceState(
			Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (datosMaterias!=null) {
			outState.putSerializable("materias", 
					datosMaterias);
		}
	}
	
	
	
	/**
	 * Metodo que maneja el evento del boton de agregar materiaa, empieza el activity  de detalle materia sin pasar
	 * datos de materia
	 * @param origenClick boton agregar
	 */
	public void nuevaMateria(View origenClick) {
		Intent intent =
				new Intent(ListaMateriasActivity.this,
						DetalleMateriaActivity.class);
		startActivityForResult(intent, NUEVA_MATERIA);
	}
	/**
	 * Clase interna  que  maneja el hilo que solicita al servidor una lista de materias de la base de datos
	 * @author Jared Daniel Salinas Gonzalez
	 *
	 */
	private class TareaBajaMaterias
		extends AsyncTask<String, Void, List<Materia>> 
	{
		/**
		 * Metodo que manda a llamar el servicio que obtiene una lista de materias
		 * @param url del servicio
		 * @return una lista de materias
		 */
		@Override
		protected List<Materia> doInBackground(String... params) {
			URL url=null;
			HttpURLConnection conn = null;
			BufferedReader entrada;
			List<Materia> resultado=null;
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
					resultado = new ArrayList<Materia>();
					for (int i=0; i<datos.length(); i++) {
						JSONObject elem = datos.getJSONObject(i);
						Materia m = new Materia();
						m.setClaveCarrera(elem.getString("claveCarrera"));
						m.setNombreMateria(elem.getString("nombreMateria"));
						m.setSemestre(Short.parseShort(elem.getString("semestre")));
						m.setClaveMateria(elem.getString("claveMateria"));
						resultado.add(m);
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
				System.out.println(ejson.getMessage());
			}
			catch (IOException eio) {
				System.err.println("Error al conectarse a servicio de carreras");
				eio.printStackTrace();
			}						
			return resultado;
		}
		/**
		 * Metodo que pasa al adapter la lista de materias
		 */
		@Override
		protected void onPostExecute(List<Materia> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			adapterMaterias = new ArrayAdapter<Materia>(
					ListaMateriasActivity.this,
					android.R.layout.simple_list_item_single_choice,
					result
				);
			listaMaterias.setAdapter(adapterMaterias);
			datosMaterias = (ArrayList<Materia>)result;
		}
		
		
	}
	
	/**
	 * Clase interna que maneja los eventos de la lista de materias
	 * @author Jared Daniel Salinas Gonzalez
	 * 
	 */
	private class ManejadorListaMaterias
		implements OnItemClickListener{


		/**
		 * Metodo que manda a llamar el activity de detalle materias con la informacion de la materia seleccionada
		 */
		@Override
		public void onItemClick(AdapterView<?> lista, 
				View arg1, 
				int selectedIndex,
				long arg3) {
			Intent intent =
					new Intent(ListaMateriasActivity.this,
							DetalleMateriaActivity.class);
			intent.putExtra("detalle", 
					(Materia)lista.getItemAtPosition(selectedIndex)
					);
			startActivityForResult(intent, MATERIA_EXISTENTE);
			
		}
		
	}

	/**
	 * Metodo que al recibir una operacion de agregar, eliminar o modificar, actualiza la lista de materias
	 */
	@Override
	protected void onActivityResult(int requestCode, 
			int resultCode, 
			Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==MATERIA_EXISTENTE) {
			if (resultCode==RESULT_OK) {
				Bundle datos = data.getExtras();
				int operacion_realizada =
						datos.getInt("Operacion");
				Materia m = (Materia)
						datos.getSerializable("Materia");
				adapterMaterias.remove(m);
				if (operacion_realizada == OPERACION_MODIFICAR) {
					adapterMaterias.add(m);
				}
			}
		}
		else {
			if(data!=null){
				Bundle datos = data.getExtras();
				Materia m = (Materia)
						datos.getSerializable("Materia");
				adapterMaterias.add(m);
				
			}
			
		}
	}
	
	
	
	
}
