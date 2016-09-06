package com.athome.TabsPedido;

import com.athome.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lady on 01/01/2016.
 */
import com.athome.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lady on 01/01/2016.
 */
public class Comida {
    private long precio;
    private String nombre;
    private String urlPlato;



    public Comida(long precio, String nombre, String urlPlato) {
        this.precio = precio;
        this.nombre = nombre;
        this.urlPlato = urlPlato;
    }

    public static final List<Comida> PLATOS_GENERALES = new ArrayList<Comida>();
    public static final List<Comida> PLATOS_USUARIO = new ArrayList<Comida>();

    public long getPrecio() {return precio;}

    public String getNombre() {
        return nombre;
    }

    public String getUrlPlato() {return urlPlato;}
}

