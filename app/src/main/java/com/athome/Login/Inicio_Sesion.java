package com.athome.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.athome.Principal.ActividadPrincipal;
import com.athome.R;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

/**
 * Created by Lady on 30/12/2015.
 */
public class Inicio_Sesion extends AppCompatActivity {

    private EditText txtCorreoUsuario;
    private EditText txtPasswordUsuario;
    private Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.inicio_sesi_activity);

        txtCorreoUsuario = (EditText) findViewById(R.id.txtCorreoIniciarSesion);
        txtPasswordUsuario = (EditText) findViewById(R.id.txtPasswordIniciarSesion);
        myFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url)).child("usuarios");

    }

    public void ingresarUsuario(View view){

        final String correoU = txtCorreoUsuario.getText().toString();
        String pass = txtPasswordUsuario.getText().toString();

        if ((txtCorreoUsuario.length()==0)||(txtPasswordUsuario.length()==0)){
            dialogDatosIncompletos();
            return;
        }

        myFirebaseRef.authWithPassword(correoU, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                ingresarUsuario(correoU);
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                switch (firebaseError.getCode()) {
                    case FirebaseError.INVALID_EMAIL:
                        dialogCorreoErroneo();
                        break;
                    case FirebaseError.INVALID_PASSWORD:
                        dialogContraErronea();
                        break;
                    case FirebaseError.USER_DOES_NOT_EXIST:
                        dialogCorreoErroneo();
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

    public void dialogContraErronea() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("Contraseña Erronea :)");
        builder.setPositiveButton("OK",null);
        builder.create();
        builder.show();
    }

    public void dialogCorreoErroneo (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("Introduce bien el correo :)");
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

    public void dialogNoHayInternet() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("No Hay Internet, Revisa tu Conexión :)");
        builder.setPositiveButton("OK",null);
        builder.create();
        builder.show();
    }

    public void ingresarUsuario(String correo){
        Query queryRef = myFirebaseRef.orderByChild("correo_Usuario").equalTo(correo);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot snapshot, String previousChild) {
                final ProgressDialog progressDialog = new ProgressDialog(Inicio_Sesion.this);
                progressDialog.setTitle("Cocinando Tus Datos");
                progressDialog.setMessage("Espera un momento...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Thread.sleep(4000);
                            String nombre = (String) snapshot.child("nombre_Usuario").getValue();
                            Intent intentIniciarSesion = new Intent(Inicio_Sesion.this, ActividadPrincipal.class);
                            SharedPreferences preferences = getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Nombre", nombre);
                            editor.putString("id_Usuario", snapshot.getKey());
                            editor.commit();
                            startActivity(intentIniciarSesion.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                            finish();


                        } catch (Exception e) {

                        }
                        progressDialog.dismiss();

                    }
                }).start();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public  void BtonOlvidarPass (View view){

        FragmentManager fragmentManager = getSupportFragmentManager();
        new DialogOlvidoPass().show(fragmentManager, "DialogOlvidoPass");
    }

    public void lanzarRegistro(View view){
        Intent intent = new Intent(Inicio_Sesion.this, Registro.class);
        startActivity(intent);
    }

}



