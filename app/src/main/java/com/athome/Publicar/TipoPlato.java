package com.athome.Publicar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by Lady on 02/01/2016.
 */
public class TipoPlato extends DialogFragment {

    public TipoPlato() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSingleListDialog();
    }

    public AlertDialog createSingleListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final CharSequence[] items = new CharSequence[4];


        items[0] = "Dasayuno";
        items[1] = "Almuerzo";
        items[2] = "Cena";
        items[3] = "Onces";

        builder.setTitle("Tipo de Plato").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Seleccionaste: " + items[which], Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getActivity().getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("tipo_Plato", (String) items[which]);
                editor.putBoolean("seleccionTipoPlato",true);
                editor.commit();
            }
        });

        return builder.create();
    }


}

