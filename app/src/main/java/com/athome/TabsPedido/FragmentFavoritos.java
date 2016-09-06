package com.athome.TabsPedido;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.athome.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by Lady on 01/01/2016.
 */
public class FragmentFavoritos extends Fragment {

    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorFavorito adaptador;
    private Firebase myFirebaseRef;

    public FragmentFavoritos() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_inicio, container, false);
        reciclador = (RecyclerView) view.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        myFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url)).child("platos");
        agregarPlato();
        Comida.PLATOS_GENERALES.clear();

        return view;
    }

    public void agregarPlato(){

        myFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                long precioPlato = (long) dataSnapshot.child("precio_Plato").getValue();
                String nombrePlato = (String) dataSnapshot.child("nombre_Plato").getValue();
                String urlPlato = (String) dataSnapshot.child("url_Foto_Plato").getValue();
                Comida.PLATOS_GENERALES.add(new Comida(precioPlato, nombrePlato, urlPlato));

                adaptador = new AdaptadorFavorito();
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
