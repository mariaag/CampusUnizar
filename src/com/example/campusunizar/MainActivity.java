package com.example.campusunizar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

//Acceso a MySQl
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainActivity extends Activity {
	
	Connection conexionMySQL;//Variable de conexión
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Función que se utiliza para realizar la conexión con la BBDD
     **/
    public void conectarBDMySQL (String usuario, String contrasena, 
    		String ip, String puerto, String catalogo)
    {
    	if (conexionMySQL == null)    	
    	{
    		String urlConexionMySQL = "";
    		if (catalogo != "")
    			urlConexionMySQL = "jdbc:mysql://" + ip + ":" +	puerto + "/" + catalogo;
    		else
    			urlConexionMySQL = "jdbc:mysql://" + ip + ":" + puerto;
    		if (usuario != "" & contrasena != "" & ip != "" & puerto != "")
    		{
    			try 
    			{
					Class.forName("com.mysql.jdbc.Driver");
	    			conexionMySQL =	DriverManager.getConnection(urlConexionMySQL, 
	    					usuario, contrasena);					
				} 
    			catch (ClassNotFoundException e) 
    			{
    		      	  Toast.makeText(getApplicationContext(),
    		                    "Error: " + e.getMessage(),
    		                    Toast.LENGTH_SHORT).show();
    			} 
    			catch (SQLException e) 
    			{
			      	  Toast.makeText(getApplicationContext(),
			                    "Error: " + e.getMessage(),
			                    Toast.LENGTH_SHORT).show();
				}
    		}
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void lanzarLogin(View view) {
        Intent in = new Intent(this,LoginUsuario.class);
        startActivity(in);
    }  
    
    public void lanzarPublicas(View view) {
        Intent in = new Intent(this,ActividadesPublicas.class);
        startActivity(in);
    }  
    
    public void salir(View view) {
        finish();
    } 
}
