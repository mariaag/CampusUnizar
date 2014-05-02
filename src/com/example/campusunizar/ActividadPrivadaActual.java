package com.example.campusunizar;

import java.sql.Connection;

import test.CampusUnizar.library.Httppostaux;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ActividadPrivadaActual extends Activity  {
Connection conexionMySQL;//Variable de conexión
	
	// String URL_connect
    String IP_Server="192.168.1.38";//IP DE NUESTRO PC
	//String IP_Server="192.168.43.80:8080";//IP EDUROAM
	String URL_connect="http://"+IP_Server+"/campusUnizar/publicActiv.php";//ruta en donde estan nuestros archivos
    
  //Diálogo que muestra un indicador de progreso y un mensaje de texto opcional o vista
    //private ProgressDialog pDialog;
    Httppostaux post;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actividad_privada_actual);
		//Manejador del envío de peticiones
		post=new Httppostaux();
		//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
//		new asynclogin().execute();  
		Bundle bundle=getIntent().getExtras();
		String actividad=bundle.getString("actividad");
		TextView titulo=(TextView) findViewById(R.id.tituloPrivada);
		titulo.setText(actividad);
	}
}
