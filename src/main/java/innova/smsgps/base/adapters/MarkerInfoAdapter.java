package innova.smsgps.base.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Locale;

import innova.smsgps.R;
import innova.smsgps.entities.Tracker;

/**
 * Created by USUARIO on 29/05/2016.
 */
public class MarkerInfoAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener
{
    LayoutInflater inflater;
    private HashMap<Marker, Tracker> markerInfoTracker = new HashMap<Marker, Tracker>();

    public MarkerInfoAdapter(HashMap<Marker, Tracker> markerInfoTracker, Context context)
    {
        this.markerInfoTracker  = markerInfoTracker;
        inflater                = LayoutInflater.from(context);
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }
    @Override
    public View getInfoContents(Marker marker)
    {
        View v          = inflater.inflate(R.layout.window_info_marker, null);
        Tracker tracker = markerInfoTracker.get(marker);
        TextView tvDetalle  = (TextView)v.findViewById(R.id.tvDetalle);
        String detalle = String.format(Locale.getDefault(), "FechaHora : %s \nBateria : %s \nVelocidad : %s", tracker.getFechaHora(), tracker.getBateria(), tracker.getVelocidad());
        tvDetalle.setText(detalle);

        return v;
    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
    }

}