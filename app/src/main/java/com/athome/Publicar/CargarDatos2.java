package com.athome.Publicar;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;


import com.athome.R;

/**
 * Created by Lady on 01/01/2016.
 */
public class CargarDatos2 extends AppCompatActivity {

    private EditText tituloPlato, resumenPlato, precioPlato, cantidadPlato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cargar_datos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tituloPlato = (EditText) findViewById(R.id.tutulo_input);
        resumenPlato = (EditText) findViewById(R.id.resumen_input);
        precioPlato = (EditText) findViewById(R.id.precio_input);
        cantidadPlato = (EditText) findViewById(R.id.cantidad_input);
    }
    public  void btnTipoPlato (View view){

        FragmentManager fragmentManager = getSupportFragmentManager();
        new TipoPlato().show(fragmentManager, "TipoPlato");
    }

    public  void btnSiguienteFoto (View view){

        SharedPreferences preferences = getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        boolean dialogTipoPlato = preferences.getBoolean("seleccionTipoPlato", false);
        //String prueba = preferences.getString("tipo_Plato", "");
        System.out.println("PRUEBA PLATO: "+dialogTipoPlato);
        if ((tituloPlato.length()==0) || (resumenPlato.length()==0) || (precioPlato.length()==0) || (cantidadPlato.length()==0)) {
            datosPlato();
            return;

        }else if (dialogTipoPlato == false){
            dialogTipoPlato();
            return;
        }else{

            final ProgressDialog progressDialog = new ProgressDialog(CargarDatos2.this);
            progressDialog.setTitle("Cocinando Tú Plato");
            progressDialog.setMessage("Espera un momento...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Thread.sleep(1500);
                        editor.putString("nombre_Plato", tituloPlato.getText().toString());
                        editor.putString("resumen_Plato", resumenPlato.getText().toString());
                        editor.putLong("precio_Plato", Long.parseLong(precioPlato.getText().toString()));
                        editor.putLong("cantidad_Plato", Long.parseLong(cantidadPlato.getText().toString()));
                        editor.commit();
                        Intent intent = new Intent(CargarDatos2.this, Fotos.class);
                        startActivity(intent);

                    } catch (Exception e) {

                    }
                    progressDialog.dismiss();

                }
            }).start();
        }






    }

    public void dialogTipoPlato (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("Escoge qué tipo de plato es tú comida :)");
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
    }

    public void datosPlato() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("Completa los datos de Tú plato :)");
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
    }

}

