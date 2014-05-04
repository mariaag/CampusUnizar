package com.example.campusunizar;

import java.sql.Connection;
import test.CampusUnizar.library.Httppostaux;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ActividadPublicaActual extends Activity{
Connection conexionMySQL;//Variable de conexi�n
    
  //Di�logo que muestra un indicador de progreso y un mensaje de texto opcional o vista
   // private ProgressDialog pDialog;
    Httppostaux post;
    
    // 	String URL_connect
    String directorio="/campusUnizar/publicActiv.php";
    String URL_connect;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actividad_actual);
		//Manejador del env�o de peticiones
		post=new Httppostaux();
		URL_connect= post.getURL(directorio);
		//si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros
//		new asynclogin().execute();  
		Bundle bundle=getIntent().getExtras();
		String extras= bundle.getString("actividad");
		String[] datos=extras.split("&");
		String actividad=datos[0];
		TextView titulo=(TextView) findViewById(R.id.titulo);
		titulo.setText(actividad);
	}
}
