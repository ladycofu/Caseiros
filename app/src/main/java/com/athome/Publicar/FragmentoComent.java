package com.athome.Publicar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.athome.R;

/**
 * Created by Lady on 01/01/2016.
 */
public class FragmentoComent extends Fragment {

    private LinearLayoutManager linearLayout;

    public FragmentoComent() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_inicio, container, false);

        RecyclerView reciclador = (RecyclerView)view.findViewById(R.id.reciclador);
        linearLayout = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(linearLayout);

        AdaptadorComentarios adaptador = new AdaptadorComentarios();
        reciclador.setAdapter(adaptador);
        reciclador.addItemDecoration(new DecoracionLineaDivisoria(getActivity()));

        return view;
    }


}