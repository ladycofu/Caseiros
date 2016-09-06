package com.athome.TabsPedido;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.athome.Localizacion.GPSTracker;
import com.athome.Localizacion.Ubicaciones;
import com.athome.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lady on 01/01/2016.
 */
public class LocationFragment extends Fragment {
    double LATITUDE = 4.6590063;
    double LONGITUDE = -74.1316563;


    GPSTracker gps;

    MapView mapView;
    GoogleMap map;
    private ArrayList<Ubicaciones> mMyMarkersArray = new ArrayList<Ubicaciones>();
    private HashMap<Marker, Ubicaciones> mMarkersHashMap;
    public LocationFragment() {

    }

    //Ubicaciones ubi=Ubicaciones.AmasDisponibles.get(i);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);


        //mapFragment.getMapAsync(this);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);


        // Gets to GoocgleMap from the MapView and does initialization stuff
        //map = mapView.getMap();
        //map.setMapType(GoogleMap.MAP_);

        map.getUiSettings().setMyLocationButtonEnabled(false);

        map.setMyLocationEnabled(true);
        mMarkersHashMap = new HashMap<Marker, Ubicaciones>();

        mMyMarkersArray.add(new Ubicaciones("Gloria Diaz", "w1", Double.parseDouble("48.869894"), Double.parseDouble("2.319642")));
        mMyMarkersArray.add(new Ubicaciones("Oscar Velez", "m1", Double.parseDouble("48.871334"), Double.parseDouble("2.316595")));
        mMyMarkersArray.add(new Ubicaciones("Laura Gomez", "w2", Double.parseDouble("48.873140"), Double.parseDouble("2.318183")));
        mMyMarkersArray.add(new Ubicaciones("Carlos Archila", "m2", Double.parseDouble("48.870656"), Double.parseDouble("2.321015")));
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());
        plotMarkers(mMyMarkersArray);
        gps = new GPSTracker(getActivity());

        if(gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng df = new LatLng(latitude,longitude);//4.659767, -74.058137);
            CameraPosition camPos = new CameraPosition.Builder()
                    .target(df)
                    .zoom(18)
                    .bearing(45)
                    .tilt(70)
                    .build();


            CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
            //map.animateCamera(cameraUpdate);
            map.animateCamera(camUpd3);
            map.addMarker(new MarkerOptions()
                    .position(df)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mi_marker))
                    .snippet("A preparado algo especial"));

        } else {
            gps.showSettingsAlert();
        }



        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity(), DetailDialog.class);
                startActivity(intent);
            }
        });



        return v;
    }
    private void plotMarkers(ArrayList<Ubicaciones> markers)
    {
        if(markers.size() > 0)
        {
            for (Ubicaciones myMarker : markers)
            {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ubi));

                Marker currentMarker = map.addMarker(markerOption);
                mMarkersHashMap.put(currentMarker, myMarker);
                map.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }
    private int manageMarkerIcon(String markerIcon)
    {
        if (markerIcon.equals("m1"))
            return R.drawable.m1;
        else if(markerIcon.equals("m2"))
            return R.drawable.m2;
        else if(markerIcon.equals("m3"))
            return R.drawable.m3;
        else if(markerIcon.equals("w1"))
            return R.drawable.w1;
        else if(markerIcon.equals("w2"))
            return R.drawable.w2;
        else if(markerIcon.equals("w3"))
            return R.drawable.w3;
        else if(markerIcon.equals("w4"))
            return R.drawable.w4;
        else
            return R.drawable.icondefault;
    }



    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
    {
        public MarkerInfoWindowAdapter()
        {
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker)
        {

            View v = getActivity().getLayoutInflater().inflate(R.layout.info_marker, null);


            Ubicaciones myMarker = mMarkersHashMap.get(marker);

            ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_icon);

            TextView markerLabel = (TextView)v.findViewById(R.id.marker_label);

            TextView anotherLabel = (TextView)v.findViewById(R.id.another_label);

            markerIcon.setImageResource(manageMarkerIcon(myMarker.getmIcon()));

            markerLabel.setText(myMarker.getmLabel());
            anotherLabel.setText("A preparado algo especial");

            return v;
        }
    }
}
