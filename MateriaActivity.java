package edu.uaz.poo2.clienteandroidescolares;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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



import edu.uaz.poo2.entidades.Carrera;
import edu.uaz.poo2.entidades.Materia;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MateriaActivity extends Activity {
	private Spinner spinnerMateria;
	private EditText editNombreMateria;
	private EditText editClaveMateria;
	private EditText editSemestreMateria;
	private Spinner spinnerCarrera;
	private List<Materia> datos;
	private ArrayAdapter<Materia> adapter;
	private List<Carrera> datosCarrera;
	private ArrayAdapter<Carrera> adapterCarrera;
	private ManejadorSpinnerMaterias manejador;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_materia);
		manejador = new ManejadorSpinnerMaterias();
		spinnerMateria = (Spinner) findViewById(R.id.spinnerMateria);
		
		spinnerMateria.setOnItemSelectedListener(manejador);
		llenaAdapter();
//		llenaAdapterCarrera();
		spinnerMateria.setAdapter(adapter);
	}
	
	public void eliminar(View origen){
		String id = editClaveMateria.getText().toString();
		String urlservicio = getString(R.string.urlservicio_Materia) + "/" + id;
		URL url = null;
		HttpURLConnection conn = null;
		try{
			url = new URL(urlservicio);
			conn  = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			int codigo = conn.getResponseCode();
			if(codigo/100 != 2){
				System.out.println("Error en codigo recibido: " + codigo);
				Toast.makeText(this, "No se pudo elimina", Toast.LENGTH_LONG).show();
				return;
			}
			Toast.makeText(this, "Materia" + id + " eliminada", Toast.LENGTH_LONG).show();
			adapter.remove(new Materia( id,"",0,""));
			regresarALista(origen);
		}catch(MalformedURLException e){
			e.printStackTrace();
		} catch (IOException o) {
			o.printStackTrace();
		} 
	}
	private void llenaAdapter() {
		URL url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		BufferedReader in = null;
		try{
			url = new URL(getString(R.string.urlservicio_Materia));
			conn  = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Accept", "application/json");
			int codigo = conn.getResponseCode();
			if(codigo != HttpURLConnection.HTTP_OK){
				System.out.println("codigo recibido: " + codigo);
			}
			is = conn.getInputStream();
			in = new BufferedReader(new InputStreamReader(is));
			String respuesta = in.readLine();
			//JSONObject obj = new JSONObject(respuesta);
			//JSONArray a = obj.getJSONArray("carrera");
			JSONArray a = new JSONArray(respuesta);
			datos = new ArrayList<Materia>();
			Materia selecciona = new Materia("","Selecciona Materia ...",0,"");
			datos.add(selecciona);
			Materia nueva = new Materia("NUEVA","NuevaMateria...",1,"selecciona");
			datos.add(nueva);
			for(int i=0; i < a.length(); i++){
				JSONObject o = a.getJSONObject(i);
				Materia m = new Materia(
						o.getString("claveMateria"),
						o.getString("nombreMateria"),
						Integer.parseInt(o.getString("semestre")),
						o.getString("claveCarrera"));
				datos.add(m);
			}
			adapter = new ArrayAdapter<Materia>(this,
					android.R.layout.simple_spinner_item, datos);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
		}catch(MalformedURLException e){
			e.printStackTrace();
		} catch (IOException o) {
			o.printStackTrace();
		} catch (JSONException je) {
			je.printStackTrace();
		}
	}

	public void regresarALista(View origen){
		setContentView(R.layout.activity_materia);
		spinnerMateria = (Spinner) findViewById(R.id.spinnerMateria);
		spinnerMateria.setOnItemSelectedListener(manejador);
		spinnerMateria.setAdapter(adapter);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.carrera, menu);
		return true;
	}
	
	private class ManejadorSpinnerMaterias implements OnItemSelectedListener{
		private boolean isFirst = true;
		@Override
		public void onItemSelected(AdapterView<?> spinner, View selectedView, int selectedIndex,
				long selectedID) {
			
			if(isFirst){
				isFirst = false;
			}
			else{
				
				setContentView(R.layout.detalles_materia_layout);
				spinnerCarrera = (Spinner) findViewById(R.id.spinnerCarreraDeMateria);
		//		spinnerCarrera.setOnItemSelectedListener(null);
				llenaAdapterCarrera();
				spinnerCarrera.setAdapter(adapterCarrera);
			Materia m = (Materia) spinner.getItemAtPosition(selectedIndex);	
			editClaveMateria = (EditText) findViewById(R.id.editClaveMateria);
			Button botonEliminar = (Button) findViewById(R.id.buttonEliminarMateria);
			Button botonModificar = (Button) findViewById(R.id.buttonModificarMateria);
			editNombreMateria = (EditText) findViewById(R.id.editNombreMateria);
			editSemestreMateria = (EditText) findViewById(R.id.editSemestreMateria);
		
			if(!m.getClaveMateria().equals("NUEVA")){
				editClaveMateria.setText(m.getClaveMateria());
				editNombreMateria.setText(m.getNombreMateria());
				editSemestreMateria.setText(String.valueOf(m.getSemestre()));
				editClaveMateria.setEnabled(false);
				//spinnerCarrera.setSelection(0);
				spinnerCarrera.setSelection(adapterCarrera.getPosition(new Carrera(m.getClaveCarrera(),"")));
					
			}else{
				botonEliminar.setVisibility(View.INVISIBLE);
				botonModificar.setText(R.string.label_boton_agregar_materia);
			}
		
			
			isFirst = true;
			
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
		private void llenaAdapterCarrera() {
			URL url = null;
			HttpURLConnection conn = null;
			InputStream is = null;
			BufferedReader in = null;
			try{
				url = new URL(getString(R.string.urlservicio_Carrera));
				conn  = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Accept", "application/json");
				int codigo = conn.getResponseCode();
				if(codigo != HttpURLConnection.HTTP_OK){
					System.out.println("codigo recibido: " + codigo);
				}
				is = conn.getInputStream();
				in = new BufferedReader(new InputStreamReader(is));
				String respuesta = in.readLine();
				//JSONObject obj = new JSONObject(respuesta);
				//JSONArray a = obj.getJSONArray("carrera");
				JSONArray a = new JSONArray(respuesta);
				datosCarrera = new ArrayList<Carrera>();
				Carrera selecciona = new Carrera("","Selecciona Carrera ...");
				datosCarrera.add(selecciona);
				//Carrera nueva = new Carrera("NUEVA","NuevaCarrera...");
				//datosCarrera.add(nueva);
				for(int i=0; i < a.length(); i++){
					JSONObject o = a.getJSONObject(i);
					Carrera c = new Carrera(
							o.getString("claveCarrera"),
							o.getString("nombreCarrera"));
					datosCarrera.add(c);
				}
		
				adapterCarrera = new ArrayAdapter<Carrera>(getParent(),
						android.R.layout.simple_spinner_item,datosCarrera);
						//new ArrayAdapter<Carrera>(this,
						//android.R.layout.simple_spinner_item, datosCarrera);
				adapterCarrera.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				
			}catch(MalformedURLException e){
				e.printStackTrace();
			} catch (IOException o) {
				o.printStackTrace();
			} catch (JSONException je) {
				je.printStackTrace();
			}
		}
		
		
	}
	
	public void guardaCambios(View origen){
		String clave = editClaveMateria.getText().toString();
		String nombre = editNombreMateria.getText().toString();
		String urlservicio = getString(R.string.urlservicio_Materia);
		int semestre = Integer.parseInt(editSemestreMateria.getText().toString());
		Carrera posCarrera =(Carrera) spinnerCarrera.getSelectedItem();
		Map<String,String> mapaDatos = new HashMap<String, String>();
		mapaDatos.put("claveMateria", clave);
		mapaDatos.put("nombreMateria", nombre);
		mapaDatos.put("semestre", String.valueOf(semestre));
		mapaDatos.put("claveCarrera", posCarrera.getClaveCarrera());
		JSONObject obj = new JSONObject(mapaDatos);
		String datos = obj.toString();
		byte[] porEnviar = datos.getBytes();
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream os = null;
		try{
			url = new URL(urlservicio);
			conn  = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			Button b = (Button) origen;
			if(b.getText().toString().equals(getString(R.string.label_boton_modificar_materia))){
				conn.setRequestMethod("PUT");
			}else{
				conn.setRequestMethod("POST");
			}
			
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Content-Length", porEnviar.length+"");
			os = conn.getOutputStream();
			os.write(porEnviar);
			int codigo = conn.getResponseCode();
			if(codigo/100 != 2){
				System.out.println("Error en codigo recibido: " + codigo);
				Toast.makeText(this, "No se pudo realizar operacion", Toast.LENGTH_LONG).show();
				return;
			}
			Toast.makeText(this, "Cambios guardados", Toast.LENGTH_LONG).show();
			adapter.remove(new Materia( clave,nombre,0,""));
			adapter.add(new Materia( clave,nombre,0,""));
			
			
		}catch(MalformedURLException e){
			e.printStackTrace();
		} catch (IOException o) {
			o.printStackTrace();
		} 
	}
	
	
	
	
}
