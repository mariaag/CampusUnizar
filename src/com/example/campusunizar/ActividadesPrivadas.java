package com.example.campusunizar;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.mysql.jdbc.Connection;

import test.CampusUnizar.library.Httppostaux;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ActividadesPrivadas extends Activity implements View.OnClickListener{
	TextView textUser;
	String usuario;
	String nombreAsignatura;
	String[] datos; //Asignatura&idAsignatura
	Connection conexionMySQL;//Variable de conexión

    //Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
    private ProgressDialog pDialog;
	Httppostaux post;//Manejador de peticiones
	
		//String URL_connect
    String directorio="/campusUnizar/actividades_privadas.php";
    String URL_connect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actividades_privadas);
		//Manejador del envío de peticiones
	    post=new Httppostaux();
		URL_connect= post.getURL(directorio);
		//Recogemos la información de la actividad login
		Bundle extras = getIntent().getExtras();
	    usuario = extras.getString("user");
	
		textUser = (TextView) findViewById(R.id.text_user2);
	    textUser.setText("Usuario: " + usuario);
	  		
	    new asyncbotones().execute();
	}
	public boolean activPrivadas(String usuario){
		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
  		postparameters2send.add(new BasicNameValuePair("usuario",usuario));

  		//Se realiza la petición
  		JSONArray jdata= post.getserverdata(postparameters2send, URL_connect);
  		//String s = String.valueOf(jdata.length());
  		//Log.e("jdataleng", s);
  		//Si lo que recibimos no es null ni menor de cero
  		if (jdata!=null && jdata.length() > 0)
  			return true;
  		else
  			return false;
	}
	public boolean creaBotones(){
		//En este caso el array de parámetros va vacio
  		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
  		postparameters2send.add(new BasicNameValuePair("actividades","privadas"));
  		
  		LinearLayout layout_botones= (LinearLayout)findViewById(R.id.linearPrivadas);

  		//Se realiza la petición
  		JSONArray jdata= post.getserverdata(postparameters2send, URL_connect);
  		//String s = String.valueOf(jdata.length());
  		//Log.e("jdataleng", s);
  		//Si lo que recibimos no es null ni menor de cero
  		if (jdata!=null && jdata.length() > 0){
  			
  			//JSONObject json_data; //creamos un objeto JSON
  			int i = 0;
  			
	  			while (i <= jdata.length()){
	  				try {
	  					nombreAsignatura = jdata.getString(i);
	  					nombreAsignatura = nombreAsignatura.replaceAll("\"", "");
	  					//En el array hemos metido en cada indice Nombre&idAsignatura, vamos a separarlo
	  					 datos = nombreAsignatura.split("&");
	  					 Button newButton = new Button(this);
	  				     newButton.setText(datos[0]);//Primero tenemos el nombre de la asignatura
	  				     newButton.setContentDescription(nombreAsignatura);
	  				     newButton.setBackgroundColor(Color.parseColor("#2d6898"));
	  				     LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
	  				     params.setMargins(0, 3, 0, 0);
	  				     newButton.setLayoutParams(params);
	  			         newButton.setTextColor(Color.parseColor("#ffffff"));
	  			         newButton.setClickable(true);
	  					 newButton.setOnClickListener(this);
	  					 layout_botones.addView(newButton);
	  					 Log.e("nombreActividad",nombreAsignatura);//muestro por log que obtuvimos
	  				} catch (JSONException e) {
	  					// TODO Auto-generated catch block
	  					e.printStackTrace();
	  				}	
	  				i=i+1;
	  			}
	  			return true;
  		  }else{
  				return false;
  			}
		
	}
	
	//vibra y muestra un Toast
    public void err_privadas(){
    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"El usuario no está inscrito en ninguna actividad", Toast.LENGTH_SHORT);
 	    toast1.show();    	
    }
    
    @Override
	public void onClick(View v) {
		Intent in = new Intent(this,ActividadPrivadaActual.class);
		
		in.putExtra("idActividad", datos[1]);
		in.putExtra("tituloActividad", datos[0]);
		in.putExtra("user", usuario);
	    startActivity(in);
    }

/*		CLASE ASYNCTASK
 * 
 * usaremos esta para poder mostrar el dialogo de progreso mientras enviamos y obtenemos los datos
 * podria hacerse lo mismo sin usar esto pero si el tiempo de respuesta es demasiado lo que podria ocurrir    
 * si la conexion es lenta o el servidor tarda en responder la aplicacion sera inestable.
 * ademas observariamos el mensaje de que la app no responde.     
 */
    
    class asyncbotones extends AsyncTask< String, String, String > {
    	 
        @Override
		protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(ActividadesPrivadas.this);
            pDialog.setMessage("Actividades Privadas....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
		@Override
		protected String doInBackground(String... params) {
			
			//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (activPrivadas(usuario)==true){    		    		
    			return "ok"; //login valido
    		}else{    		
    			return "err"; //login invalido     	          	  
    		}
        	
		}
       
		/*Este método se ejecuta en otro hilo, por lo que no podremos modificar la
		 * UI desde él. Para ello, usaremos los tres métodos siguientes.*/
        @Override
		protected void onPostExecute(String result) {

           pDialog.dismiss();//ocultamos progess dialog.
           Log.e("onPostExecute=",""+result);
           
           if (result.equals("ok")){
				creaBotones();
            }else{
            	err_privadas();
            }
            
                									}
		
        }
}

