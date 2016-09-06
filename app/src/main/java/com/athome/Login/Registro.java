package com.athome.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.athome.Principal.ActividadPrincipal;
import com.athome.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by Lady on 30/12/2015.
 */
public class Registro extends AppCompatActivity {

    private EditText txtNombreUsuario;
    private EditText txtCorreoUsuario;
    private EditText txtPassword;
    private Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity);
        Firebase.setAndroidContext(this);

        txtCorreoUsuario = (EditText) findViewById(R.id.txtCorreoRegistro);
        txtPassword = (EditText) findViewById(R.id.txtPasswordRegistro);
        txtNombreUsuario = (EditText) findViewById(R.id.txtNombreRegistro);
        myFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url)).child("usuarios");

    }

    //Metodo que se ejecutará cada vez que se presione el boton: Ingresar Usuario
    public void registrarUsuario (View view){

        final String nombreU = txtNombreUsuario.getText().toString();
        final String correoU = txtCorreoUsuario.getText().toString();
        String passU = txtPassword.getText().toString();

        if((txtNombreUsuario.length()==0)||  (txtCorreoUsuario.length()==0)
                || (txtPassword.length()==0)){
            dialogDatosIncompletos();
            return;
        }

        myFirebaseRef.createUser(correoU,passU, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(final Map<String, Object> result) {
                final ProgressDialog progressDialog = new ProgressDialog(Registro.this);
                progressDialog.setTitle("Cocinando Tus Datos");
                progressDialog.setMessage("Espera un momento...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Thread.sleep(4000);
                            Firebase registroFB;
                            registroFB = myFirebaseRef.child((String) result.get("uid"));
                            registroFB.child("correo_Usuario").setValue(correoU);
                            registroFB.child("nombre_Usuario").setValue(nombreU);
                            SharedPreferences preferences = getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Nombre", nombreU);
                            editor.putString("id_Usuario",(String) result.get("uid"));
                            editor.commit();

                            Intent intentIniciarSesion = new Intent(Registro.this, ActividadPrincipal.class);
                            startActivity(intentIniciarSesion.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                            finish();

                        } catch (Exception e) {

                        }
                        progressDialog.dismiss();

                    }
                }).start();

            }
            @Override
            public void onError(FirebaseError firebaseError) {
                switch (firebaseError.getCode()) {
                    case FirebaseError.EMAIL_TAKEN:
                        dialogCorreoExiste();
                        break;
                    case FirebaseError.INVALID_EMAIL:
                        dialogCorreoMal();
                        break;
                    case FirebaseError.NETWORK_ERROR:
                        dialogNoHayInternet();
                        break;
                    default:
                        // handle other errors
                        break;
                }
            }
        });


    }

    //Metodo que se ejecutará cada vez que se presione el boton: Iniciar Sesion
    public  void lanzarIniciarSesion (View view){

        Intent intent = new Intent(Registro.this, Inicio_Sesion.class);
        startActivity(intent);
    }

    public void dialogCorreoExiste (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("El correo ya existe en nuestra base de datos, Cambialo :)");
        builder.setPositiveButton("OK",null);
        builder.create();
        builder.show();
    }

    public void dialogDatosIncompletos (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("Faltan Datos Por Completar :)");
        builder.setPositiveButton("OK",null);
        builder.create();
        builder.show();
    }
    public void dialogCorreoMal (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("Introduce un Correo Válido :)");
        builder.setPositiveButton("OK",null);
        builder.create();
        builder.show();
    }
    public void dialogNoHayInternet() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("No Hay Internet, Revisa tu Conexión :)");
        builder.setPositiveButton("OK",null);
        builder.create();
        builder.show();
    }
}


