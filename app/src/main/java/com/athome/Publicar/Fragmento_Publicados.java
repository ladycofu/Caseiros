package com.athome.Publicar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.athome.R;
import com.athome.TabsPedido.Comida;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

/**
 * Created by Lady on 01/01/2016.
 */
public class Fragmento_Publicados extends Fragment {

    private RecyclerView reciclador;
    private GridLayoutManager layoutManager;
    private AdaptadorPublicado adaptador;
    private Firebase myFirebaseRef;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_inicio, container, false);

        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        reciclador.setLayoutManager(layoutManager);
        myFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url)).child("platos");
        agregarPlato();
        Comida.PLATOS_USUARIO.clear();

        return view;
    }

    public void agregarPlato(){
        SharedPreferences preferences = getActivity().getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
        String id_Usuario = preferences.getString("id_Usuario", "");
        Query queryRef = myFirebaseRef.orderByChild("id_Usuario").equalTo(id_Usuario);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                long precioPlato = (long) dataSnapshot.child("precio_Plato").getValue();
                String nombrePlato = (String) dataSnapshot.child("nombre_Plato").getValue();
                String urlPlato = (String) dataSnapshot.child("url_Foto_Plato").getValue();
                Comida.PLATOS_USUARIO.add(new Comida(precioPlato, nombrePlato, urlPlato));


                adaptador = new AdaptadorPublicado();
                reciclador.setAdapter(adaptador);
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

}
