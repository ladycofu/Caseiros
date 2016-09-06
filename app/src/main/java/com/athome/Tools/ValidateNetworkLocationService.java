package com.athome.Tools;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.athome.Login.Login;
import com.athome.Login.Splash;

/**
 * Created by Lady on 18/12/2015.
 */
public class ValidateNetworkLocationService {


    private static Splash splasActivity;

    public ValidateNetworkLocationService(Splash splasActivity) {
        this.splasActivity = splasActivity;
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) splasActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(splasActivity);
        builder.setMessage("Tú GPS se encuentra deshabilitado, quieres habilitarlo?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        splasActivity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), splasActivity.REQUEST_CODE);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        Toast.makeText(
                                splasActivity.getApplicationContext(),
                                "Es necesario tener los servicios de localización activados para poder ingresar a la aplicación.",
                                splasActivity.TOAST_DURATION)
                                .show();
                        ValidateNetworkLocationService validateNetworkLocationService = new ValidateNetworkLocationService(splasActivity);
                        validateNetworkLocationService.buildAlertMessageNoGps();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void buildAlertDialogNoNetwork() {
        AlertDialog.Builder builder = new AlertDialog.Builder(splasActivity);

        builder.setTitle("Internet Desactivado").setCancelable(false)
                .setMessage("AtHome necesita acceso a Internet. Activa los servicios de Internet, por favor.")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                buildAlertDialogListNetwork();
                            }
                        });

        //    return builder.create();
        final AlertDialog alert = builder.create();
        alert.show();

    }

    public void buildAlertDialogListNetwork() {
        AlertDialog.Builder builder = new AlertDialog.Builder(splasActivity);

        final CharSequence[] items = new CharSequence[2];

        items[0] = "WIFI";
        items[1] = "Redes Móviles";

        builder.setTitle("Tipo de red").setCancelable(false)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                splasActivity.startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), splasActivity.REQUEST_CODE_NETWORK);
                                dialog.cancel();
                                break;
                            case 1:
                                splasActivity.startActivityForResult(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS), splasActivity.REQUEST_CODE_NETWORK);
                                dialog.cancel();
                                break;
                        }
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        Toast.makeText(
                                splasActivity.getApplicationContext(),
                                "Es necesario tener acceso a internet para el buen funcionamiento de la aplicación",
                                splasActivity.TOAST_DURATION)
                                .show();
                        ValidateNetworkLocationService validateNetworkLocationService = new ValidateNetworkLocationService(splasActivity);
                        validateNetworkLocationService.buildAlertDialogNoNetwork();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

}

