package innova.smsgps.marker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import innova.smsgps.R;
import innova.smsgps.beans.HistorialRegistros;

/**
 * Created by USUARIO on 24/12/2015.
 */
public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener
{
    Context context;
    private HashMap<Marker, HistorialRegistros> marcadorHashMap; // Inicializamos el hashmap
    LayoutInflater inflater ;

    public MarkerInfoWindowAdapter(Context context,HashMap<Marker, HistorialRegistros> marcadorHashMap )
    {
        this.context = context;
        this.marcadorHashMap = marcadorHashMap;
        inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }


    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }
    @Override
    public View getInfoContents(Marker marker)
    {
        View v  = inflater.inflate(R.layout.marker_info_window, null);

        HistorialRegistros marcadorBeans = marcadorHashMap.get(marker);

        TextView txtTipoAlerta 		= (TextView)v.findViewById(R.id.txtTipoAlerta);
        TextView txtFechaHora 	    = (TextView)v.findViewById(R.id.txtTipoFechaHora);
        txtFechaHora.setText(marcadorBeans.getFechaHora());

        if(marcadorBeans.getIdTipoAlerta().equals("1"))
            txtTipoAlerta.setText("Denuncia");
        else if(marcadorBeans.getIdTipoAlerta().equals("2"))
            txtTipoAlerta.setText("Emergencia");
        return v;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
    }


}
