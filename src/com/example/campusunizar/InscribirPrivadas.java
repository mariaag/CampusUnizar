package com.example.campusunizar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class InscribirPrivadas extends Activity{
	
	TextView textUser;
	String usuario;
	
	//Comentario para probar con git
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inscribirprivadas);
		
		//Obtenemos datos extra
		Bundle bundle=getIntent().getExtras();
		
		//Texto usuario
		usuario = bundle.getString("user");
		textUser = (TextView) findViewById(R.id.text_user2);
	    textUser.setText("Usuario: " + usuario);
	};
	
}
