package com.athome.Principal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.athome.Login.Login;
import com.athome.Login.Perfil;
import com.athome.Login.Perfil2;
import com.athome.Publicar.CargarDatos2;
import com.athome.R;
import com.firebase.client.Firebase;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lady on 30/12/2015.
 */
public class ActividadPrincipal extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView nombreUsuario;
    private View harderView;
    private CircleImageView imagenUsuario;
    private String loadedImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.actividad_principal);

        agregarToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout3);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view3);

        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            seleccionarItem(navigationView.getMenu().getItem(0));
        }
        View headerView = navigationView.inflateHeaderView(R.layout.nv_header);
        nombreUsuario = (TextView) headerView.findViewById(R.id.nombreUsuarioNavigation);
        imagenUsuario = (CircleImageView) headerView.findViewById(R.id.circle_image);

        SharedPreferences preferences = getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
        nombreUsuario.setText(preferences.getString("Nombre", ""));
        loadedImage = preferences.getString("urlFoto", "");
        new DownloadImage().execute();

        // Para el proceso de los platos en Mas Cercanos.


    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            // ab.setHomeAsUpIndicator(R.drawable.drawer_toggle);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

    }

    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (itemDrawer.getItemId()) {
            case R.id.nav_pedidos:
                fragmentoGenerico=new FragmentPrincipal();
                break;
            case R.id.nav_tuspedidos:
                startActivity(new Intent(this,ListaPedidos.class));
                break;
            case R.id.nav_perfil:
                startActivity(new Intent(this, Perfil2.class));
                break;
            case R.id.nv_Publicar_plato:
                startActivity(new Intent(this, CargarDatos2.class));
                break;

            case R.id.nav_ordenes:
                fragmentoGenerico= new FragmentPublicar();

                break;
            case R.id.nav_info:
                startActivity(new Intent(this, About.class));
                break;
            case R.id.nav_log_out:
                SharedPreferences preferences = getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Nombre", null);
                editor.commit();
                startActivity(new Intent(this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                break;
        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor_principal, fragmentoGenerico)
                    .commit();
        }

        // Setear título actual
        setTitle(itemDrawer.getTitle());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                drawerLayout.openDrawer(Gravity.RIGHT);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            mp.reset();                               //resets the media player
            mp.release();
        }
        return super.onKeyDown(keyCode, event);
    }
    */

    private class DownloadImage extends AsyncTask<Void,Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            String url = loadedImage;

            try{
                URLConnection connection = new URL(url).openConnection();
                connection.setConnectTimeout(1000 * 30);
                connection.setReadTimeout(1000*30);

                return BitmapFactory.decodeStream((InputStream) connection.getContent(), null, null);

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            if(bitmap !=null){
                imagenUsuario.setImageBitmap(bitmap);
            }

        }
    }


}

