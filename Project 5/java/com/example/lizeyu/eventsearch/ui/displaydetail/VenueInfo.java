package com.example.lizeyu.eventsearch.ui.displaydetail;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lizeyu.eventsearch.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VenueInfo extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    private Context mCtx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.venue_info, container, false);
        mCtx = this.getActivity();

        String generalRule = getArguments().getString("generalRule");
        String name = getArguments().getString("name");
        String address = getArguments().getString("address");
        String phoneNumber = getArguments().getString("phoneNumber");
        String childRule = getArguments().getString("childRule");
        String city = getArguments().getString("city");
        String openHours = getArguments().getString("openHours");
        String latitude = getArguments().getString("latitude");
        String longitude = getArguments().getString("longitude");

        if (name != null) {
            ((TextView) rootView.findViewById(R.id.venue_info_row1_input)).setText(name);
        } else {
            rootView.findViewById(R.id.venue_info_row1).setVisibility(View.GONE);
        }
        if (address != null) {
            ((TextView) rootView.findViewById(R.id.venue_info_row2_input)).setText(address);
        } else {
            rootView.findViewById(R.id.venue_info_row2).setVisibility(View.GONE);
        }
        if (city != null) {
            ((TextView) rootView.findViewById(R.id.venue_info_row3_input)).setText(city);
        } else {
            rootView.findViewById(R.id.venue_info_row3).setVisibility(View.GONE);
        }
        if (phoneNumber != null) {
            ((TextView) rootView.findViewById(R.id.venue_info_row4_input)).setText(phoneNumber);
        } else {
            rootView.findViewById(R.id.venue_info_row4).setVisibility(View.GONE);
        }
        if (openHours != null) {
            ((TextView) rootView.findViewById(R.id.venue_info_row5_input)).setText(openHours);
        } else {
            rootView.findViewById(R.id.venue_info_row5).setVisibility(View.GONE);
        }
        if (generalRule != null) {
            ((TextView) rootView.findViewById(R.id.venue_info_row6_input)).setText(generalRule);
        } else {
            rootView.findViewById(R.id.venue_info_row6).setVisibility(View.GONE);
        }
        if (childRule != null) {
            ((TextView) rootView.findViewById(R.id.venue_info_row7_input)).setText(childRule);
        } else {
            rootView.findViewById(R.id.venue_info_row7).setVisibility(View.GONE);
        }
        if(latitude!=null && longitude!=null){
            final double lat = Double.parseDouble(latitude);
            final double lon = Double.parseDouble(longitude);
            mMapView = rootView.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);

            mMapView.onResume(); // needed to get the map to display immediately

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;

                    // For showing a move to my location button
                    if (ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);

                    // For dropping a marker at a point on the Map
                    LatLng sydney = new LatLng(lat,lon);
                    googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(16).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
        }



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
