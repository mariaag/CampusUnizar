package com.example.campusunizar;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import test.CampusUnizar.library.Httppostaux;
import android.widget.LinearLayout.LayoutParams;
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
import android.widget.Toast;

public class ActividadesPublicas extends Activity implements View.OnClickListener{
	Connection conexionMySQL;//Variable de conexi�n
	
	// String URL_connect
    String IP_Server="192.168.1.38";//IP DE NUESTRO PC
	//String IP_Server="192.168.43.80:8080";//IP EDUROAM
    String URL_connect="http://"+IP_Server+"/campusUnizar/publicActiv.php";//ruta en donde estan nuestros archivos
    
  //Di�logo que muestra un indicador de progreso y un mensaje de texto opcional o vista
    private ProgressDialog pDialog;
    Httppostaux post;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actividades_publicas);
		//Manejador del env�o de peticiones
		post=new Httppostaux();
		//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
		new asynclogin().execute();        		               
	}
	
	 public boolean checklogindata(String username ,String password ){
	    	
	    if 	(username.equals("") || password.equals("")){
	    	Log.e("Login ui", "checklogindata user or pass error");
	    return false;
	    
	    }else{
	    	
	    	return true;
	    }
   	}
    
	//vibra y muestra un Toast
    public void err_activ(){
    	Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(200);
	    Toast toast1 = Toast.makeText(getApplicationContext(),"Error: No hay actividades p�blicas disponibles", Toast.LENGTH_SHORT);
 	    toast1.show();    	
    }
    /*Valida el estado del logueo solamente necesita como parametros el usuario y passw*/
    public boolean activPublicas() {
    	
    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
     		
		postparameters2send.add(new BasicNameValuePair("actividades","publicas"));

		//realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata=post.getserverdata(postparameters2send,URL_connect);
		
		//si lo que obtuvimos no es null
		if (jdata!=null && jdata.length() > 0){
		    return true;
		}else{	//json obtenido invalido verificar parte WEB.
			Log.e("JSON  ", "ERROR");
			return false;
		}
    }
    
    public void crearBotones(){
    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
 		
		postparameters2send.add(new BasicNameValuePair("actividades","publicas"));

		//realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata=post.getserverdata(postparameters2send,URL_connect);
		LinearLayout vista = (LinearLayout)findViewById(R.id.botonesPubli);
		Button act;
    	for(int i=1; i<jdata.length(); i++){
    		
			try {
				String activAct;
				activAct=jdata.getString(i).toString();

				Log.e("actividad" + i,activAct);//muestro por log que obtuvimos
				act = new Button(vista.getContext());
				act.setText(activAct);
				act.setContentDescription(activAct);
				act.setTextColor(Color.parseColor("#ffffff"));
				act.setBackgroundColor(Color.parseColor("#2d6898"));
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			    params.setMargins(0, 3, 0, 0);
			    act.setLayoutParams(params);
				act.setClickable(true);
				act.setOnClickListener(this);
				vista.addView(act);
//					setContentView(act);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
    }
    
    class asynclogin extends AsyncTask< String, String, String > {
   	 
    	String user,pass;
        @Override
		protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(ActividadesPublicas.this);
            pDialog.setMessage("Buscando actividades....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
		@Override
		protected String doInBackground(String... params) {
			
			//enviamos y recibimos y analizamos los datos en segundo plano.
			
			if (activPublicas()){    		    		
    			return "hayPublicas"; //login valido
    		}else{    		
    			return "noHayPublicas"; //login invalido     	          	  
    		}
		}
       
		/*Una vez terminado doInBackground segun lo que halla ocurrido 
		pasamos a la sig. activity
		o mostramos error*/
        @Override
		protected void onPostExecute(String result) {

           Log.e("onPostExecute=",""+result);
           
           if (result.equals("noHayPublicas")){
            	err_activ();
           }else{
            	crearBotones();
           }
           pDialog.dismiss();//ocultamos progess dialog.
        }		
    }
    public void onClick(View v) {
    	String actividad=v.getContentDescription().toString();
		Intent in = new Intent(this,ActividadPublicaActual.class);
		in.putExtra("actividad", actividad);
	    startActivity(in);
    }
}
