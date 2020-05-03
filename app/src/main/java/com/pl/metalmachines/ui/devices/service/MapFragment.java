package com.pl.metalmachines.ui.devices.service;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pl.metalmachines.R;
import com.pl.metalmachines.model.Device;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View v;
    private MapView mMapView;
    private GoogleMap googleMap;
    Location location;
    Device currentDevice;

    public MapFragment(Location location,Device currentDevice) {
        this.location = location;
        this.currentDevice = currentDevice;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_google_maps, container, false);

        if (location!=null) {

// ADD NAME TO TOOLBAR //
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Lokalizacja");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Miejsce podpisania serwisu");
//
            mMapView = (MapView) v.findViewById(R.id.map);
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
                    googleMap.setMyLocationEnabled(true);

                    // For dropping a marker at a point on the Map
                    LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(sydney).title(currentDevice.getName()).snippet(currentDevice.customerName));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(8).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });


        }else{
            getFragmentManager().popBackStack();
            Toast.makeText(getActivity(),"Brak danych o lokalizacji", Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
