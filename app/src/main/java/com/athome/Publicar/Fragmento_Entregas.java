package com.athome.Publicar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.athome.R;

/**
 * Created by Lady on 01/01/2016.
 */
public class Fragmento_Entregas extends Fragment {

    public Fragmento_Entregas() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_entregas, container, false);
    }


}
