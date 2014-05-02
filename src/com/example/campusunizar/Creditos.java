package com.example.campusunizar;

import java.util.ArrayList;



import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import test.CampusUnizar.library.Httppostaux;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Creditos extends Activity {
	
	
	
	EditText user;
	// String URL_connect
    String IP_Server="192.168.1.131";//IP DE NUESTRO PC
	//String IP_Server="192.168.43.80:8080";//IP EDUROAM
	String URL_connect="http://"+IP_Server+"/campusUnizar/creditos.php";//ruta en donde estan nuestros archivos
    String usuario;
    Httppostaux post;
    int totalCreditos;
    ArrayList<String> datosActividades = new ArrayList<String>();
    
    private ProgressDialog pDialog;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creditos);
		
		//Manejador del envío de peticiones
		post=new Httppostaux();
		
		//Obtenemos el nombre del usuario que se ha introducido inicialmente
		Bundle bundle=getIntent().getExtras();
        usuario = bundle.getString("user");
		
        new asynCreditos().execute(usuario); 	
}




public boolean mostrarActividades()
{
	try
	{
	    ListView lv = (ListView)findViewById(R.id.listViewCreditos);
	    
	    itemCreditoAdapter adapter = new itemCreditoAdapter(this, datosActividades);
		
		lv.setAdapter(adapter);
		
		TextView totalCreditosSuma = (TextView) findViewById(R.id.lbltotalCreditosSuma);
		totalCreditosSuma.setText(Integer.toString(totalCreditos));
		Log.d("creditosTotales", Integer.toString(totalCreditos));
		
		return true;
	}
	catch(Exception e)
	{
		Log.e("mostrarActividades", e.getMessage());
		return false;
	}

}


public boolean consultabbdd(String username) {
	
	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
 		
	postparameters2send.add(new BasicNameValuePair("usuario",username));
	    		
   //realizamos una peticion y como respuesta obtenes un array JSON
	JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);

	/*como estamos trabajando de manera local el ida y vuelta sera casi inmediato
	 * para darle un poco realismo decimos que el proceso se pare por unos segundos para poder
	 * observar el progressdialog                                                                                                                                                                                                                                           
	 * la podemos eliminar si queremos
	 */
    SystemClock.sleep(950);
	    		
    //si lo que obtuvimos no es null
	if (jdata!=null && jdata.length() > 0){
		try 
		{					
			int id;
			String nameActivity;
			int creditos;
			
			for (int i = 0; i < jdata.length(); i++) 
			{
			    JSONObject row = jdata.getJSONObject(i);
			    id = row.getInt("id_actividad");
			    nameActivity = row.getString("nombre");
			    creditos = row.getInt("Creditos");
			    totalCreditos += creditos;
			    String cadena = Integer.toString(id) + ";" + nameActivity +";"+ Integer.toString(creditos); 
			    datosActividades.add(cadena);
			}
		// Log.e("loginstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
		} catch (JSONException e) {
			e.printStackTrace();
		}		            
	             
			//validamos la existencia de datos
			 if (datosActividades.isEmpty()){
				 Log.e("Existen_Actividades ", "NoActividades");
				 return false;
			 }
			 else{
				 Log.e("Existen_Actividades ", "Actividades");
				 return true;
			 }		             
	  }else
  		{		
		  //json obtenido invalido verificar parte WEB.
		  Log.e("JSON  ", "ERROR");
		  //popUpNoActividades();
		  return false;
  		}	
}

public void popUpNoActividades()
{
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage("No esta suscrito a ninguna Actividad")
	        .setTitle("No Actividades")
	        .setCancelable(false)
	        .setNegativeButton("Aceptar",
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int id) {
	                        dialog.cancel();
	                    }
	                });
	AlertDialog alert = builder.create();
	alert.show();
}

class asynCreditos extends AsyncTask < String, String, String > {
	 
	String user;
    protected void onPreExecute() {


    }

	protected String doInBackground(String... params) {
		//obtnemos usr
		user=params[0];
        
		//enviamos y recibimos y analizamos los datos en segundo plano.
		if (consultabbdd(user)==true){    		    		
			return "ok"; //login valido
		}else{    		
			return "err"; //login invalido     	          	  
		}
    	
	}
   
	/*Una vez terminado doInBackground segun lo que halla ocurrido 
	pasamos a la sig. activity
	o mostramos error*/
    protected void onPostExecute(String result) {

    	if (result.equals("ok")){
    		if (!datosActividades.isEmpty())
    		{
    			Log.e("onPostExecute ", "valido");
    			mostrarActividades();
    			Log.e("onPostExecute - postMostrar ", "hecho");
    		}
    		else{
    			popUpNoActividades();
    		
    		}
        }else{
        	//Mirar Como controlamos los errores!	
        }
    	
    }
        
	}
	
}
