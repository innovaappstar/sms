package innova.smsgps.activities;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import innova.smsgps.R;
import innova.smsgps.application.Globals;
import innova.smsgps.enums.IDSP2;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityMapTrackHistorial extends BaseActivity
{

    private GoogleMap mapTrack;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_title, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        setContentView(R.layout.activity_mapa_track);
//        tvNumeroContacto = (TextView)findViewById(R.id.tvNumeroContacto);

        // map
        FragmentManager myFragmentManager = getFragmentManager();
        MapFragment myMapFragment   = (MapFragment)myFragmentManager.findFragmentById(R.id.mapTrack);
        mapTrack    = myMapFragment.getMap();
        mapTrack.setMyLocationEnabled(true);


        String lat = getIntent().getStringExtra(ActivityListaTrack.EXTRALAT);
        String lng = getIntent().getStringExtra(ActivityListaTrack.EXTRALNG);
        LatLng latLng = new LatLng( Double.valueOf(lat), Double.valueOf(lng));
        mapTrack.addMarker(new MarkerOptions()
                .position(latLng)
                .title(getIntent().getStringExtra(ActivityListaTrack.EXTRAFECHAHORA))
                .snippet(Globals.getInfoMovil().getSPF2(IDSP2.EMAILUSUARIO)));
//        mapTrack.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-12.004706, -77.048350), 12));        //myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapTrack.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));        //myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapTrack.setMapType(GoogleMap.MAP_TYPE_NORMAL);



    }


    @Override
    public void listenerTimer()
    {
    }


    public void onClick(View view)
    {
        switch (view.getId())
        {
            default:
                break;
        }
    }

}



