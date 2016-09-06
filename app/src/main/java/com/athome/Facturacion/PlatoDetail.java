package com.athome.Facturacion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.athome.R;

/**
 * Created by Lady on 01/01/2016.
 */
public class PlatoDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plato_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
    public  void HacerPedidoPlato (View view){

        Intent intent = new Intent(PlatoDetail.this, Factura.class);
        startActivity(intent);
    }
}