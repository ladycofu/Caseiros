package com.athome.Facturacion;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.athome.Principal.ActividadPrincipal;
import com.athome.R;

/**
 * Created by Lady on 02/01/2016.
 */
public class Factura extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factura);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Factura.this, ActividadPrincipal.class);
                startActivity(intent);

                Snackbar.make(view, "Tu Orden esta Hecha", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
