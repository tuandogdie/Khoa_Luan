package com.example.shippermanager.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.shippermanager.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.SphericalUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle extras = getIntent().getExtras();
        lat = extras.getDouble("lat");
        lng = extras.getDouble("lng");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setTitle("BẢN ĐỒ");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private LatLng getLatLngFromAddress(String address) throws IOException {
        Geocoder coder = new Geocoder(this);
        List<Address> lstAddress = coder.getFromLocationName(address,5);
        if(lstAddress == null) return null;
        Address location = lstAddress.get(0);
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        return new LatLng(lat,lng);
    }

    private List<LatLng> getPath(DirectionsApiRequest req)
    {
        try {
            List<LatLng> path = new ArrayList();
            DirectionsResult res = req.await();
            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return path;
        } catch(Exception ex) {
            Log.e("error", ex.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
//            AIzaSyAQ98LDwfFxMuX46r1us23C2x0g1b6oTy4
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey("AIzaSyDfM042SyxHXm8CO-OA3bavSrBW0C_FmZk")
                    .build();

            LatLng shopAddress = getLatLngFromAddress("12 Nguyễn Văn Bảo, Phường 4, Gò Vấp, Thành phố Hồ Chí Minh");
            LatLng targetAddress = new LatLng(lat,lng);
            mMap.addMarker(new MarkerOptions().position(shopAddress).title("Shop address"));
            mMap.addMarker(new MarkerOptions().position(targetAddress).title("Target address"));
            //Define list to get all latlng for the route
            double dis = SphericalUtil.computeDistanceBetween(shopAddress,targetAddress);
            Log.e("t",String.format("%.2f", dis / 1000));
            String p1 = shopAddress.latitude +","+shopAddress.longitude;
            String p2 = targetAddress.latitude +","+targetAddress.longitude;

            //Execute Directions API request
            DirectionsApiRequest req = DirectionsApi.getDirections(context, p1, p2);
            List<LatLng> path = getPath(req);

            //Draw the polyline
            if (path.size() > 0) {
                PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
                mMap.addPolyline(opts);
            }
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetAddress, 20));
            //context.shutdown();
        }catch (Exception e)
        {
            e.printStackTrace();
        }




    }



}