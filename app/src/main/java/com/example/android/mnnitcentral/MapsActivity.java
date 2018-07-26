package com.example.android.mnnitcentral;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private DatabaseReference ref;
    private GoogleMap mGoogleMap;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = findViewById(R.id.ma);

        if (googleServicesAvailable()) {
            Toast.makeText(this, "perfect!!", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_maps);
            initMap();
        }
        else
        {}
    }
    public void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "CAN'T CONNECT TO PLAY SERVICES", Toast.LENGTH_LONG).show();
        }
        return false;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
         mGoogleMap = googleMap;
            geoLocate();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//        }
//        mGoogleMap.setMyLocationEnabled(true);

    }
    private void goToLocationZoom(LatLng M,float zoom)
    {
        LatLng ll= M;
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(ll,zoom);
        mGoogleMap.moveCamera(update);
    }
    public void geoLocate()  {
        ref = FirebaseDatabase.getInstance().getReference().child(getIntent().getStringExtra("class").toLowerCase())
        .child(getIntent().getStringExtra("title").toLowerCase()).child("lists");
        Log.v("ref",""+ref);


        //Chnages part1
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                final List<String> areas = new ArrayList<String>();
//
//                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
//                    String areaName = areaSnapshot.child("name").getValue(String.class);
//                    areas.add(areaName);
//                }
//
//                Spinner areaSpinner = (Spinner) findViewById(R.id.drop_down);
//                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(MapsActivity.this, android.R.layout.simple_spinner_item, areas);
//                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                areaSpinner.setAdapter(areasAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        //End


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Map_Coordinates cor = dataSnapshot1.getValue(Map_Coordinates.class);
                    LatLng location = new LatLng(cor.getLat(),cor.getLon());
                    Log.v("lat",""+cor.getLat());
                    Log.v("lon",""+cor.getLon());

                    float color = cor.getCol();
                    goToLocationZoom(location,15);
                    mGoogleMap.addMarker(new MarkerOptions().title(cor.getName()).position(location))
                              .setIcon(BitmapDescriptorFactory.defaultMarker(color));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.toolbar_layout, container, false);
//
//        //YOUR STUFF
//        return rootView;
//    }

//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View v = inflater.inflate(R.layout.action_sort, container, false);
//
//        String [] values =
//                {"Time at Residence","Under 6 months","6-12 months","1-2 years","2-4 years","4-8 years","8-15 years","Over 15 years",};
//        Spinner spinner = (Spinner) v.findViewById(R.id.drop_down2);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
//        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        spinner.setAdapter(adapter);
//        Log.v("running","this tuns");
//        return v;
//
//    }

}
