package com.paweldev.maszynypolskie.ui.devices;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.config.Config;
import com.paweldev.maszynypolskie.model.Device;
import com.paweldev.maszynypolskie.repository.DeviceRepository;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.FragmentUtils;
import com.paweldev.maszynypolskie.utils.GPSUtils;


public class DevicesGpsFragment extends Fragment {

    private View v;
    private MapView mMapView;
    private GoogleMap googleMap;
    Location location;
    Device currentDevice;

    public DevicesGpsFragment() {

    }

    public DevicesGpsFragment(Location location, Device currentDevice) {
        this.location = location;
        this.currentDevice = currentDevice;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_devices_gps, container, false);

// ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Lokalizacja");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Współrzędne maszyny");
//
        if (location != null) {
            mMapView = (MapView) v.findViewById(R.id.map_device);
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume(); // needed to get the map to display immediately
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @SuppressLint("MissingPermission")
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;

                    // For showing a move to my location button
                    googleMap.setMyLocationEnabled(true);

                    // For dropping a marker at a point on the Map
                    LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(latlng).title(currentDevice.getName()).snippet(currentDevice.getCustomerName()));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(8).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });


        } else {
            Toast.makeText(getActivity(), "Brak danych o lokalizacji", Toast.LENGTH_SHORT).show();
        }

/// ADD GPS LOCATION ////////////
        final Button addLocationButton = v.findViewById(R.id.add_gps_location_device_gps_fragment);
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (location != null) {
                    if (Config.getIsAdmin()==true) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Potwierdzenie operacji")
                                .setMessage("Czy nepewno nadpisać istniejącą lokalizacje maszyny?\n" + currentDevice.getName())
                                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        String locationString = GPSUtils.location.getLatitude() + ";" + GPSUtils.location.getLongitude();
                                        try {
                                            if (locationString != null) {
                                                String[] gpsLocationSplit = locationString.split(";");
                                                location = new Location("");
                                                location.setLatitude(Double.parseDouble(gpsLocationSplit[0]));
                                                location.setLongitude(Double.parseDouble(gpsLocationSplit[1]));
                                                currentDevice.setGpsLocation(locationString);
                                                DeviceRepository.updateDevice(currentDevice);
                                                getFragmentManager().popBackStack();
                                                FragmentUtils.replaceFragment(new DevicesGpsFragment(location, currentDevice), getFragmentManager());

                                            }
                                        } catch (Exception e) {
                                            MessageFragment messageFragment = new MessageFragment();
                                            messageFragment.setMessage(e.getMessage());
                                            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                            e.printStackTrace();
                                        }
                                    }

                                })
                                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        Toast.makeText(getActivity(), "Brak uprawnień.", Toast.LENGTH_SHORT).show();
                    }

                } else {


                    String locationString = GPSUtils.location.getLatitude() + ";" + GPSUtils.location.getLongitude();
                    try {
                        if (locationString != null) {
                            String[] gpsLocationSplit = locationString.split(";");
                            location = new Location("");
                            location.setLatitude(Double.parseDouble(gpsLocationSplit[0]));
                            location.setLongitude(Double.parseDouble(gpsLocationSplit[1]));
                            currentDevice.setGpsLocation(locationString);
                            DeviceRepository.updateDevice(currentDevice);
                            getFragmentManager().popBackStack();
                            FragmentUtils.replaceFragment(new DevicesGpsFragment(location, currentDevice), getFragmentManager());

                        }
                    } catch (Exception e) {
                        MessageFragment messageFragment = new MessageFragment();
                        messageFragment.setMessage(e.getMessage());
                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                        e.printStackTrace();
                    }
                }
            }
        });

// PREVIOUS BUTTON //
        Button prevButton = (Button) v.findViewById(R.id.preview_device_gps_fragment);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

// NAVIGATION TO CUSTOMER BUTTON //
        Button navigationToCustomerButton = v.findViewById(R.id.car_navigation_device_gps_fragment);
        navigationToCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location != null) {
                    String uri = "google.navigation:q=" + location.getLatitude() + "," + location.getLongitude();
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(uri));
                    startActivity(intent);
                }
            }
        });

        return v;
    }

}