package com.athome.Publicar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.athome.Principal.ActividadPrincipal;
import com.athome.R;
import com.firebase.client.Firebase;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lady on 02/01/2016.
 */
public class Fotos extends AppCompatActivity {

    private static final String SERVER_ADDRESS = "http://athome.net23.net/";
    private String APP_DIRECTORY = "myPictureApp/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private String TEMPORAL_PICTURE_NAME = "temporal.jpg";
    private Firebase myFirebaseRef;
    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;

    private ImageView imageUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.fotos);
        myFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url)).child("platos");
        imageUpload = (ImageView) findViewById(R.id.imageToUpload);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cargar_fotos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_Camara:
                openCamera();
                break;
            case R.id.action_Galeria:
                Intent abrirGaleria = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                abrirGaleria.setType("image/*");
                startActivityForResult(abrirGaleria.createChooser(abrirGaleria, "Selecciona la Foto de tu plato"), SELECT_PICTURE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PHOTO_CODE:
                if(resultCode == RESULT_OK){
                    String dir =  Environment.getExternalStorageDirectory() + File.separator
                            + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;
                    Bitmap bitmap;
                    bitmap = BitmapFactory.decodeFile(dir);
                    imageUpload.setImageBitmap(bitmap);
                }
                break;

            case SELECT_PICTURE:
                if(resultCode == RESULT_OK){
                    Uri path = data.getData();
                    imageUpload.setImageURI(path);
                }
                break;
        }

    }

    private void openCamera() {
        //Donde se guarda foto en la SD
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        file.mkdirs();

        String path = Environment.getExternalStorageDirectory() + File.separator
                + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;

        File newFile = new File(path);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        startActivityForResult(intent, PHOTO_CODE);
    }

    public void guardarPlato(View view){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(imageUpload.getDrawable() ==null){
            dialogNoHayFoto();
            return;
        }else if (networkInfo != null && networkInfo.isConnected()) {
            Bitmap imagen = ((BitmapDrawable)imageUpload.getDrawable()).getBitmap();
            new UploadImage(redimensionarImagenMaximo(imagen,600,500)).execute();
        }else{
            dialogNoHayInternet();
            return;
        }


    }

    private class UploadImage extends AsyncTask<Void, Void, Void> {

        Bitmap image;
        ProgressDialog loading;

        public UploadImage(Bitmap image){
            this.image=image;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(Fotos.this, "Cocinando Tú Plato", "Espera un momento...",true,true);
            loading.setCancelable(false);
        }


        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            SharedPreferences preferences = getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
            String id_Usuario = preferences.getString("id_Usuario", "");
            String nombrePlato = preferences.getString("nombre_Plato", "");
            String resumenPlato = preferences.getString("resumen_Plato", "");
            String tipoPlato = preferences.getString("tipo_Plato", "");
            Long cantidadPlato = preferences.getLong("cantidad_Plato", 0);
            Long precioPlato = preferences.getLong("precio_Plato", 0);

            Firebase guardarPlatoFB;
            guardarPlatoFB = myFirebaseRef.push();
            Map<String, Object> platoUsuario = new HashMap<String, Object>();
            String nombreFoto = guardarPlatoFB.getKey();
            String url = SERVER_ADDRESS + "pictures/" + nombreFoto + ".JPG";
            platoUsuario.put("nombre_Plato", nombrePlato);
            platoUsuario.put("resumen_Plato", resumenPlato);
            platoUsuario.put("precio_Plato", precioPlato);
            platoUsuario.put("cantidad_Plato", cantidadPlato);
            platoUsuario.put("tipo_Plato", tipoPlato);
            platoUsuario.put("url_Foto_Plato", url);
            platoUsuario.put("id_Usuario", id_Usuario);


            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            dataToSend.add(new BasicNameValuePair("name",nombreFoto));

            HttpParams httpRequestParams = getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "SavePicture.php");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
                guardarPlatoFB.setValue(platoUsuario);

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Error al guardar Tú Plato: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            Intent intent = new Intent(Fotos.this, ActividadPrincipal.class);
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            loading.dismiss();
            Toast.makeText(getApplicationContext(),"Plato Publicado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private HttpParams getHttpRequestParams(){

        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }

    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

    public void dialogNoHayFoto (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("Selecciona una foto. Abre tu cámara o de la galeria");
        builder.setPositiveButton("OK",null);
        builder.create();
        builder.show();
    }

    public void dialogNoHayInternet (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage("No hay conexión a internet. Compruebalo");
        builder.setPositiveButton("OK",null);
        builder.create();
        builder.show();
    }



}

