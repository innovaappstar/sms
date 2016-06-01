package innova.smsgps.activities;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import innova.smsgps.CircleImageView;
import innova.smsgps.R;
import innova.smsgps.base.adapters.FriendsAdapter;
import innova.smsgps.base.adapters.MarkerInfoAdapter;
import innova.smsgps.entities.Friend;
import innova.smsgps.entities.Tracker;
import innova.smsgps.entities.TrackerUser;
import innova.smsgps.task.TrackerAsyncTask;
import innova.smsgps.utils.Utils;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityMapTrack extends BaseActivity implements TrackerAsyncTask.TrackerUsuarioCallback, AdapterView.OnItemClickListener
{
    boolean isMostrarListaAbs = false;
    LinearLayout llFuncionesActionBar;
    Tracker tracker = new Tracker();

    private GoogleMap mapTrack;

    ListView lvFriends ;
    private static Bitmap bmFotoFriend = null;

    private HashMap<Marker, Tracker> markerInfoTracker = new HashMap<Marker, Tracker>(); // Inicializamos el hashmap


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_title, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        setContentView(R.layout.activity_mapa_track);

        llFuncionesActionBar= (LinearLayout)findViewById(R.id.llFuncionesActionBar);
        lvFriends           = (ListView)findViewById(R.id.lvFriends);
        lvFriends.setOnItemClickListener(ActivityMapTrack.this);
        // map
        FragmentManager myFragmentManager = getFragmentManager();
        MapFragment myMapFragment   = (MapFragment)myFragmentManager.findFragmentById(R.id.mapTrack);
        mapTrack    = myMapFragment.getMap();
        mapTrack.setMyLocationEnabled(true);
        mapTrack.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-12.004706, -77.048350), 12));        //myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapTrack.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    /**
     * @param alTracker ArrayList Tracker
     */
    private void drawLocation(ArrayList<Tracker> alTracker)
    {
        for (int i = 0; i < alTracker.size(); i++)
        {
            Tracker tracker = alTracker.get(i);
            try
            {
                View marker = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_marker, null);
                ImageView ivFriend = (ImageView)marker.findViewById(R.id.ivFriend);
                if (bmFotoFriend != null)
                    ivFriend.setImageBitmap(bmFotoFriend);

                LatLng latLng = new LatLng(tracker.getLatToDouble(), tracker.getLngToDouble());
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Utils.getConvertirInView(ActivityMapTrack.this, marker)));
                // Customizamos los datos del Info Marcador

                Marker markerWindow = mapTrack.addMarker(markerOptions);
                markerInfoTracker.put(markerWindow, tracker);
                mapTrack.setInfoWindowAdapter(new MarkerInfoAdapter(markerInfoTracker, this));
                mapTrack.setOnInfoWindowClickListener(new MarkerInfoAdapter(markerInfoTracker, this));
            }catch (Exception e)
            {
                Utils.LOG(e.getMessage() + " PARSE EXCEPTION");
            }
        }
    }






    @Override
    public void listenerTimer()
    {
    }


    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.rlOptionFlotante:
//                managerUtils.imprimirToast(ActivityMapTrack.this, "options");
//                managerUtils.imprimirToast(ActivityMapTrack.this, Utils.getFechaHora(0));
                break;
            case R.id.tvActionSincronizarContactos:
                tracker = new Tracker(Tracker.SINCRONIZAR);
                new TrackerAsyncTask(this, tracker).execute();
                break;
            case R.id.ivActionAbs:
                managerUtils.deslizadoVertical(llFuncionesActionBar, this, isMostrarListaAbs);
                isMostrarListaAbs = !isMostrarListaAbs;
                break;
            default:
                break;
        }
    }


    @Override
    public void onTrackerUser(TrackerUser trackerUser)
    {
        if (trackerUser.isCorrecto())
        {
            if (trackerUser.getTracker().getACTION() == Tracker.SINCRONIZAR)
            {
                this.lvFriends.setAdapter(new FriendsAdapter(trackerUser.getAlFriend(), ActivityMapTrack.this));
            }else if (trackerUser.getTracker().getACTION() == Tracker.UBICACION)
            {
                this.drawLocation(trackerUser.getAlTracker());
            }
        }
        managerUtils.imprimirToast(ActivityMapTrack.this, trackerUser.getDescription());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (parent.getItemAtPosition(position) instanceof Friend)
        {
            // tracking friend
            Friend friend = (Friend)parent.getItemAtPosition(position);
            String fechaHoraInicio  =  Utils.getFechaHora(0, false);
            String fechaHoraFin     =  Utils.getFechaHora(1, false);
            tracker = new Tracker(Tracker.UBICACION, fechaHoraInicio, fechaHoraFin, friend.getIdUsuario());
            new TrackerAsyncTask(this, tracker).execute();
            // copiamos contenido bitmap
            try
            {
                CircleImageView ivFriend = (CircleImageView)view.findViewById(R.id.ivFriend);
                Bitmap bmOrigen     = ((BitmapDrawable)ivFriend.getDrawable()).getBitmap();
                bmFotoFriend        = bmOrigen.copy(bmOrigen.getConfig(), true);
            }catch (Exception e)
            {
                managerUtils.imprimirToast(this, "Error");
            }


        }else if (parent.getItemAtPosition(position) instanceof Tracker)
        {

        }
    }




}

//                mapTrack.addMarker()
//                mapTrack.addPolyline(new PolylineOptions().add(latLng).width(5).color(Color.RED));


