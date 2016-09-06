package com.athome.TabsPedido;

import android.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.athome.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

/**
 * Created by ladyto on 03/09/16.
 */
public class MapFragment extends Fragment {
    SupportMapFragment myMapFragment;
    GoogleMap myMap;
    View convertView;
    FragmentManager fm;
    private Marker markerColombia;
    private Marker markerArgentina;
    private Marker markerEcuador;
    public static final String EXTRA_LATITUD = "LATITUD";
    public static final String EXTRA_LONGITUD = "LONGITUD";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        convertView=inflater.inflate(R.layout.fragment_mapas,null);
        //fm=getChildFragmentManager();
        //View v = inflater.inflate(R.layout.fragment_maps, container, false);
        myMapFragment=(SupportMapFragment) fm.findFragmentById(R.id.map);



        // here instead of using getMap(), we are using getMapAsync() which returns a callback and shows map only when it gets ready.
        //it automatically checks for null pointer or older play services version, getMap() is deprecated as mentioned in google docs.


        myMapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googlemap) {
                // TODO Auto-generated method stub
                myMap=googlemap;
                // Markers
                LatLng colombia = new LatLng(4.6, -74.08);
                markerColombia = googlemap.addMarker(new MarkerOptions()
                        .position(colombia)
                        .title("Colombia")
                );

                LatLng ecuador = new LatLng(-0.217, -78.51);
                markerEcuador = googlemap.addMarker(new MarkerOptions()
                        .position(ecuador)
                        .title("Ecuador")
                        .draggable(true)
                );

                LatLng argentina = new LatLng(-34.6, -58.4);
                markerArgentina = googlemap.addMarker(
                        new MarkerOptions()
                                .position(argentina)
                                .title("Argentina")
                );

                // Cámara
                googlemap.moveCamera(CameraUpdateFactory.newLatLng(argentina));

                // Eventos
                googlemap.setOnMarkerClickListener(this.getActivity());
                googlemap.setOnMarkerDragListener(this);
                googlemap.setOnInfoWindowClickListener(this);
            }
        });


        return convertView;
    }
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.equals(markerColombia)) {

            myMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    Intent intent = new Intent(MarkersActivity.this, MarkerDetailActivity.class);
                    intent.putExtra(EXTRA_LATITUD, marker.getPosition().latitude);
                    intent.putExtra(EXTRA_LONGITUD, marker.getPosition().longitude);
                    startActivity(intent);
                }

                @Override
                public void onCancel() {

                }
            });

            return true;

        }

        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        if (marker.equals(markerEcuador)) {
            Toast.makeText(this, "START", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        if (marker.equals(markerEcuador)) {
            String newTitle = String.format(Locale.getDefault(),
                    getString(R.string.marker_detail_latlng),
                    marker.getPosition().latitude,
                    marker.getPosition().longitude);

            setTitle(newTitle);
        }
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker.equals(markerEcuador)) {
            Toast.makeText(this, "END", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.equals(markerArgentina)) {

            ArgentinaDialogFragment.newInstance(marker.getTitle(),
                    getString(R.string.argentina_full_snippet))
                    .show(getSupportFragmentManager(), null);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
