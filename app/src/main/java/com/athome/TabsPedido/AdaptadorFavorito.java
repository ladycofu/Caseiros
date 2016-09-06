package com.athome.TabsPedido;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.athome.R;
import com.athome.Tools.BajarImagenPlato;
import com.bumptech.glide.Glide;

/**
 * Created by Lady on 01/01/2016.
 */
public class AdaptadorFavorito extends RecyclerView.Adapter<AdaptadorFavorito.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public TextView precio;
        public ImageView imagen;

        public ViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.nombre_comida);
            precio = (TextView) v.findViewById(R.id.precio_comida);
            imagen = (ImageView) v.findViewById(R.id.miniatura_comida);
        }
    }

    public AdaptadorFavorito() {
    }

    @Override
    public int getItemCount() {
        return Comida.PLATOS_GENERALES.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_fragment_fa, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Comida item = Comida.PLATOS_GENERALES.get(i);

        viewHolder.nombre.setText(item.getNombre());
        viewHolder.precio.setText("€" + item.getPrecio());
        if (viewHolder.imagen != null) {
            new BajarImagenPlato(viewHolder.imagen).execute(item.getUrlPlato());
        }


    }


}
