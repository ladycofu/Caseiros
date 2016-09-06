package com.athome.TabsPedido;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.athome.Facturacion.PlatoDetail;
import com.athome.Principal.ActividadPrincipal;
import com.athome.R;

/**
 * Created by Lady on 01/01/2016.
 */
public class DetailDialog extends AppCompatActivity {

    private TextView textNombrePlato;
    private TextView textDescrpcion;
    private TextView textPrecio;
    private RatingBar ratingbar;
    private Button pedido;
    private int number=1;
    String descrpcion="AJIACO";
    public DetailDialog(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        setContentView(R.layout.dialog_menu_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        textDescrpcion = (TextView) findViewById(R.id.textView_descrip_plato_dialog);

        textDescrpcion.setText(getString(R.string.Descripcion,descrpcion));

        ratingbar = (RatingBar) findViewById(R.id.ratingBar_dialog);
        float d= (float) ((number*5) /100);
        ratingbar.setRating(d);
    }
    public  void HacerPedidoDialog (View view){

        Intent intent = new Intent(DetailDialog.this, PlatoDetail.class);
        startActivity(intent);
    }
    private void  Calificacion(View view){
        ratingbar=(RatingBar)view.findViewById(R.id.ratingBar_dialog);
        float d= (float) ((number*5) /100);
        ratingbar.setRating(d);
    }
    private void DatosPlato(View view) {
        String nombre="Nombre del Plato";
        String descrpcion="Descrpcion del plato";
        String precio="€ 12";
        textNombrePlato = (TextView) view.findViewById(R.id.textView_nombre_plato_dialog);
        textDescrpcion = (TextView) view.findViewById(R.id.textView_descrip_plato_dialog);
        textPrecio = (TextView) view.findViewById(R.id.textView_precio_dialog);
        pedido = (Button) view.findViewById(R.id.Button_pedido_dialog);
        textNombrePlato.setText(nombre);
        textDescrpcion.setText(descrpcion);
        textPrecio.setText(precio);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fullscreen_dialog, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();


        switch (item.getItemId()) {
            case R.id.action_close:
                startActivity(new Intent(this, ActividadPrincipal.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

