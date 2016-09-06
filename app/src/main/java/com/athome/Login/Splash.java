package com.athome.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.athome.Principal.ActividadPrincipal;
import com.athome.R;
import com.athome.Tools.ValidateNetworkLocationService;

/**
 * Created by Lady on 30/12/2015.
 */
public class Splash extends Activity { public static final int REQUEST_CODE = 0;
    public static final int TOAST_DURATION = Toast.LENGTH_LONG;
    public static final int REQUEST_CODE_NETWORK = 1;
    private Handler handlerSplash;
    private String nombreUsuario;
    private ValidateNetworkLocationService validateNLServices;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        validateNLServices = new ValidateNetworkLocationService(this);
        validateNetworkProvider();
    }

    protected void onResume() {
        super.onResume();
        handlerSplash = new Handler();
        splashScreenActivityHandler();

    }

    private void splashScreenActivityHandler() {
        int TIME_SPLASH = 3000;
        handlerSplash.postDelayed(new Runnable() {
            @Override
            public void run() {
                // acción que se ejecutan pasados los milisegundos.

            }
        }, TIME_SPLASH);
    }


    private void validateNetworkProvider() {
        if (!validateNLServices.isOnline()) {
            validateNLServices.buildAlertDialogNoNetwork();
        } else validateGPSProvider();
    }

    private void validateGPSProvider() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            validateNLServices.buildAlertMessageNoGps();
        }else startActivityMain();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NETWORK && resultCode == 0)
            if (validateNLServices.isOnline()) {
                validateGPSProvider();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Es necesario tener acceso a internet para el buen funcionamiento de la aplicación.",
                        TOAST_DURATION)
                        .show();
                validateNetworkProvider();
            }
        if (requestCode == REQUEST_CODE && resultCode == 0) {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (provider.equals("")) {
                Toast.makeText(
                        getApplicationContext(),
                        "Es necesario tener los servicios de localización activados para poder ingresar a la aplicación.",
                        TOAST_DURATION)
                        .show();
                validateGPSProvider();
            }
            startActivityMain();

        }
    }


    private void startActivityMain() {

        finish(); // No se ingresa esta activity a la pila. (Elimina).
        SharedPreferences preferences = getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
        nombreUsuario = preferences.getString("Nombre", null);
        //Limpiar los valores que esten guardados.
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("seleccionTipoPlato");
        editor.commit();
        if (nombreUsuario == null) {
            Intent intent = new Intent(Splash.this, Login.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(Splash.this, ActividadPrincipal.class);
            startActivity(intent);
        }
    }
}

