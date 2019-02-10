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
import android.content.DialogInterface.OnClickListener;
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
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author Jared Daniel Salinas González
 * Clase que maneja los eventos del Activity "Detalle Materia"
 *
 */
public class DetalleMateriaActivity extends Activity {
	EditText claveCarrera;
	EditText nombreMateria;
	EditText semestre;
	EditText claveMateria;
	TextView clave;
	Button botonEliminar;
	Button botonModificar;
	Spinner listaCarrera;
	Materia m;
	String esClave;
	boolean esNuevaMateria;
	String posicion="";
	ArrayList<Carrera> datosCarreras;
	ArrayAdapter<Carrera> adapterCarreras;
	// Dialogo para pedir confirmacion
	private AlertDialog dialogoConfirmacionEliminacion;
	String ruta="";
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
	 * Metodo que permite crear el activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_materia);
		clave=(TextView)findViewById(R.id.clave_text);
		clave.setVisibility(4);
		claveCarrera = (EditText) findViewById(R.id.clave_carrera_mat);
		claveCarrera.setVisibility(4);
		nombreMateria = (EditText) findViewById(R.id.nombre_materia);
		semestre=(EditText)findViewById(R.id.semestre);
		
		
		claveMateria=(EditText)findViewById(R.id.clave_materia);
		botonEliminar = (Button) findViewById(R.id.boton_eliminar_materia);
		botonModificar = (Button) findViewById(R.id.boton_modificar_materia);
		creaDialogoConfirmacion();
		listaCarrera=(Spinner)findViewById(R.id.spinner_carreras);
		if (datosCarreras==null) {
			TareaBajaCarreras tarea =
					new TareaBajaCarreras();
			tarea.execute(getString(R.string.url_base)
					+"carrera");
		}
		else {
			adapterCarreras = new ArrayAdapter<Carrera>(
					DetalleMateriaActivity.this,
					android.R.layout.simple_spinner_dropdown_item,
					datosCarreras
				);
			listaCarrera.setAdapter(adapterCarreras);
		}
		
		
		
		obtenDatos();
		listaCarrera.setOnItemSelectedListener(new ManejadorSpinners());
		
	}
	/**
	 * Metodo que crea una una ventana con el dialogo si se quiere eliminar una materia
	 */
	private void creaDialogoConfirmacion() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Se le pone un titulo
		builder.setTitle(R.string.etiqueta_titulo_confirmacion_eliminacion);
		// Se le pone un texto		
		builder.setMessage(R.string.etiqueta_confirmacion_eliminacion_materia);
		// Se indica quien maneja el evento de dar click en SI		
		builder.setPositiveButton(R.string.etiqueta_si, new ManejadorConfirmacion());
		// Se indica quien maneja el evento de dar click en NO		
		builder.setNegativeButton(R.string.etiqueta_no, null);
		// Se crea el cuadro de dialogo con la informacion especificada
		dialogoConfirmacionEliminacion = builder.create();
	}

	/**
	 * 
	 * @author Jared Daniel Salinas Gonzalez
	 * Clse interna que maneja el dialogo de confirmacion, si se desea eliminar la materia seleccionada, manda a llamar
	 * eliminaMateria 
	 *
	 */
	private class ManejadorConfirmacion
		implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			TareaEliminaMateria tarea = new TareaEliminaMateria();
			cambiarRuta("eliminamateria/");
			tarea.execute(getString(R.string.url_base) + "materia/"+ruta
					+ m.getClaveMateria());			
		}
		
	}
	/**
	 * Metodo que obtiene los datos de una materia seleccionada en lista Materia Activity si la materia existe, caso contrario
	 * muestra un boton de agregar materia
	 */
	private void obtenDatos() {
		Intent intent = getIntent();
		m = (Materia) intent.getSerializableExtra("detalle");
		
		if (m != null) {
			claveCarrera.setText(m.getClaveCarrera());
			nombreMateria.setText(m.getNombreMateria());
			claveMateria.setText(m.getClaveMateria());
			semestre.setText(m.getSemestre()+"");
			cambiarRuta("modificamateria");
			TareaNombreCarrera tarea =
					new TareaNombreCarrera();
			tarea.execute(getString(R.string.url_base)
					+"materia/obtennombrecarrera/"+m.getClaveCarrera()+"?");
			/*
			try{
				listaCarrera.setSelection(adapterCarreras.getPosition(new Carrera(c.getClaveCarrera(),"")));
				
			}catch(Exception npe ){
				System.out.println("Error: "+npe.getMessage());
				
			}
			*/

			claveMateria.setEnabled(false);
			claveCarrera.setEnabled(false);
			esNuevaMateria = false;
		} else {
			botonEliminar.setVisibility(View.INVISIBLE);
			botonModificar.setText(R.string.etiqueta_boton_agregar_materia);
			esNuevaMateria = true;
			cambiarRuta("agregarmateria/");
			
		}
		//listaCarrera.setOnItemSelectedListener(new ManejadorSpinners());
		
		
		
	}
	/**
	 * Metodo que nos permite regresar a la activity anterior
	 * @param origenClick el boton presionado
	 */
	public void regresaALista(View origenClick) {
			
			super.onBackPressed();
		
			
			
		
		
		
	}
	/**
	 * Metodo que pone en el bundle los cambios hechos en la base de datos para que en la activity de lista materia sea
	 * actualizada
	 * @param operacion cual fue la operacion que se realizo, agregar,modificar eliminar
	 */
	public void regresaDatos(int operacion) {
		Intent datos = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("Operacion", operacion);
		bundle.putSerializable("Materia", m);
		datos.putExtras(bundle);
		setResult(RESULT_OK, datos);
		finish();
	}
	/**
	 * Metodo que usa el dialogo de "confirmar eliminacion" para eliminar una materia de la base de datos
	 * @param origenClick boton de borrar materia
	 */

	public void eliminaMateria(View origenClick) {
		// Pide confirmacion de la eliminacion
		dialogoConfirmacionEliminacion.show();
	}
	
	/**
	 * Metodo que se activa al clickear agregar o modificar materia, comprueba que los campos obligatorios esten llenos, si no
	 * se manda una advertencia que se introduzca los datos y ejecuta el hilo de TareaGuardaMateria
	 * @param origenClick boton de agregar/modificar
	 */
	public void guardaMateria(View origenClick) {
		//listaCarrera.setOnItemSelectedListener(new ManejadorSpinners());
		if (claveMateria.getText().toString().isEmpty()
				|| nombreMateria.getText().toString().isEmpty()
				||claveCarrera.getText().toString().isEmpty()
				||semestre.getText().toString().isEmpty()) {
			Toast.makeText(this,
					R.string.etiqueta_error_carreras,					
					Toast.LENGTH_LONG).show();
			return;
		}
		if (m == null) {
			m = new Materia();
		}
		m.setClaveCarrera(claveCarrera.getText().toString());
		m.setNombreMateria(nombreMateria.getText().toString());
		m.setSemestre(Short.parseShort(semestre.getText().toString()));
		m.setClaveMateria(claveMateria.getText().toString());
		TareaGuardaMateria tarea = new TareaGuardaMateria();
		tarea.execute(getString(R.string.url_base) + "materia/"+ruta);
	}
	/**
	 * Clase interna que manda solicita al servidor que elimine una materia de la base de datos
	 * @author Jared Daniel Salinas Gonzalez
	 *
	 */
	private class TareaEliminaMateria extends AsyncTask<String, Void, Boolean> {
		/**
		 * Metodo que se ejecuta en segundo plano, guarda los parametros introducidos en una url y se manda a llamar el
		 * servicio asociado a la eliminacion de materias en el servidor
		 * 
		 * @return Verdadero si se pudo eliminar, falso caso contrario
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
		 * Metodo que una vez ejecutada la tarea de elimnar una tarea, manda un aviso si se pudo eliminar la materia 
		 * si se pudo además del mensaje usa el metodo regresa datos mandando la constante OPERACION_ELIMINAR
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(DetalleMateriaActivity.this,
						R.string.aviso_materia_eliminada,
						Toast.LENGTH_LONG).show();
				regresaDatos(ListaMateriasActivity.OPERACION_ELIMINAR);
			} else {
				Toast.makeText(DetalleMateriaActivity.this,
						R.string.aviso_materia_no_eliminada,
						Toast.LENGTH_LONG)
						.show();
			}
		}
	}
	/**
	 * Metodo que agrega o modifica una materia en la base de datos
	 * @author Jared Daniel Salinas Gonzalez
	 *
	 */
	private class TareaGuardaMateria extends AsyncTask<String, Void, Boolean> {
		/**
		 * Metodo manda a llamar los servicios para agregar o modificar una materia desde la base de datos
		 * @params La url del servidor
		 * @return verdadero si se pudo agregar o modificar dicha materia, false caso contrario
		 */
		@Override
		protected Boolean doInBackground(String... params) {
			URL url = null;
			HttpURLConnection conn = null;
			String strUrl = params[0];
			try {
				if (!esNuevaMateria) {
					strUrl+="/"+m.getClaveMateria();
				}
				url = new URL(strUrl);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				if (esNuevaMateria) {					
					conn.setRequestMethod("POST");
				}
				else {
					conn.setRequestMethod("PUT");
				}				
				Map<String,String> mapaDatos = new HashMap<String, String>();
				mapaDatos.put("claveMateria",m.getClaveMateria());
				mapaDatos.put("nombreMateria",m.getNombreMateria());
				mapaDatos.put("semestre",m.getSemestre()+"");
				mapaDatos.put("claveCarrera",m.getClaveCarrera());
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
		 * Metodo que manda un aviso si se pudo guardar la info de la materia y si se pudo  usa el metodo 
		 * regresaDatos con la operacion de modificar o agregar se el caso que fuese
		 * @param result es verdadero o falso si es que se pudo guardar la info de la materia
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(DetalleMateriaActivity.this,
						R.string.aviso_materia_guardada, Toast.LENGTH_LONG)
						.show();
				if (!esNuevaMateria) {
					regresaDatos(ListaCarrerasActivity.OPERACION_MODIFICAR);
				} else {
					regresaDatos(ListaCarrerasActivity.OPERACION_AGREGAR);
				}
			} else {
				Toast.makeText(DetalleMateriaActivity.this,
						R.string.aviso_materia_no_guardada,						
						Toast.LENGTH_LONG).show();
			}
		}

	}
	/**
	 * Case interna que maneja el hilo que va solicitar la informacion de obtener las carreras de la base de datos
	 * para el spinner del activity
	 * @author Jared Daniel Salinas Gonzalez
	 *
	 */
	private class TareaBajaCarreras
			extends AsyncTask<String, Void, List<Carrera>> 
			{
		
				/**
				 * Metodo que regresa una lista con la informacion de las carreras
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
				 * Metodo que le pasa al adapter la lista de carreras obtenidas por el hilo
				 * @param result lista de carreras
				 */
				@Override
				protected void onPostExecute(List<Carrera> result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					adapterCarreras = new ArrayAdapter<Carrera>(
							DetalleMateriaActivity.this,
							android.R.layout.simple_spinner_dropdown_item,
							result
							);
					listaCarrera.setAdapter(adapterCarreras);
					datosCarreras = (ArrayList<Carrera>)result;
				}
	
	
			}
	/**
	 * Clase que maneja los eventos asociados con el spinner de carreras
	 * @author Jared Daniel Salinas Gonzalez
	 *
	 */
	private class ManejadorSpinners implements OnItemSelectedListener{
		Boolean esPrimero=true;
		int contador=0;
			@Override
			/**
			 * Si en la activity de ListaMaterias se clickea por una materia, cambia el elemento del spinner a la carrera
			 * asociada con la materia, caso contrario solo muestra el spinner por defecto, al clickear un elemento del spinner
			 * hace que podamos agregar o cambiar la carrera asociada a la materia
			 * 
			 */
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
					System.out.println(esNuevaMateria);
					try{
						
						if(esNuevaMateria==false){
							listaCarrera.setSelection(adapterCarreras.getPosition(new Carrera(m.getClaveCarrera(),"")));
							if(contador>1){
								Carrera c= new Carrera();
								listaCarrera.setSelection(arg2);
								c=(Carrera)listaCarrera.getItemAtPosition(arg2);
								esClave=c.getClaveCarrera();
								claveCarrera.setText(esClave);
								
								
							}
							System.out.println(contador);
							contador++;
							
						}
						else if(esNuevaMateria==true){
							try{
								Carrera c= new Carrera();
								c=(Carrera)listaCarrera.getItemAtPosition(arg2);
								
								
								esClave=c.getClaveCarrera();
								claveCarrera.setText(esClave);
								
							}catch(ClassCastException cce){
								
							}
							
							
							
						}
						//esPrimero=true;
						
						//claveCarrera.setText(esClave);
						
					}catch(NullPointerException npe){
						System.out.println("Hola");
						
					}
					
					
					
				//}
			
				
					
					
				
				
				
			}
			/**
			 * Metodo de la interfaz on item selected listener que no se usa
			 */
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
			
		}
	/**
	 * Clase que maneja el hilo que  obtiene el nombre de una carrera asociada a una materia en especifico
	 * @author Jared Daniel Salinas Gonzalez
	 *
	 */
	
	private class TareaNombreCarrera
		extends AsyncTask<String, Void, String> 
		{
		/**
		 * Metodo que obtiene el nombre de la carrera
		 * @param una cadena con el nombre de la carrera
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
				System.out.println(codigo);
				if (codigo==HttpURLConnection.HTTP_OK) {
					entrada = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
	
					resultado=entrada.readLine();
					
					//System.out.println("Nombre de Carrera: "+resultado);
				}
				conn.disconnect();
			}
			catch (MalformedURLException eu) {
				System.err.println("URL invalido");
			}
	
			catch (IOException eio) {
				System.err.println("Error al conectarse a servicio de carreras");
				eio.printStackTrace();
			}	
			
			return resultado;
		}
		/**
		 * Asocia el nombre y la clave de la carrera en el activity
		 * @param cadena con el nombre de la carrera de la materia seleccionada
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			posicion=result;
			Carrera c= new Carrera();
			c.setNombreCarrera(posicion);
			c.setClaveCarrera(claveCarrera.toString());
			System.out.println("Carrera: "+posicion);
			//ArrayAdapter myAdap=(ArrayAdapter)listaCarrera.getAdapter();
			//System.out.println(c);
			//int spinnerPosition = myAdap.getPosition("IBE101");
			
			//System.out.println("La posicion del spineer es: "+spinnerPosition);
			//listaCarrera.setSelection(spinnerPosition);
			//listaCarrera.setSelection(4);
			
		
			//listaCarrera.setSel
		
	
			}
		}
}

