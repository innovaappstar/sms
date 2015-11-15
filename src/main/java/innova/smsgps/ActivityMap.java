package innova.smsgps;

import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import innova.smsgps.beans.Coordenada;
import innova.smsgps.beans.HistorialRegistros;
import innova.smsgps.beans.ListRegistrosAlertas;
import innova.smsgps.beans.RegistroAlerta;

/**
 * Created by USUARIO on 01/11/2015.
 */
public class ActivityMap extends BaseActivity
{
    static int Contador ;

    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_map);
        FragmentManager myFragmentManager = getFragmentManager();
        MapFragment myMapFragment   = (MapFragment)myFragmentManager.findFragmentById(R.id.map);
        map = myMapFragment.getMap();
        map.setMyLocationEnabled(true);
        map.moveCamera(UltimaUbicacionCamara());        //myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        PintarMarcadores();
    }



    // Recupera ultima Ubicacion guardada ..
    private CameraUpdate UltimaUbicacionCamara()
    {
        Coordenada coordenada = new Coordenada();
        if (coordenada.getLatitud() == 0.0)
        {
            double lat = -12.004706;
            double lng = -77.048350;

            return CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12);
        }
        return CameraUpdateFactory.newLatLngZoom(new LatLng(coordenada.getLatitud(), coordenada.getLongitud()), 12);
    }


    /**
     * PINTAR MARCADORES
     */
    private HashMap<Marker, RegistroAlerta> MarcadorHashMap = new HashMap<Marker, RegistroAlerta>(); // Inicializamos el hashmap

    //{{ Pintar Marcadores
    public void PintarMarcadores()
    {


        try {
            // INDICE
            if (managerSqlite.ejecutarConsulta(62, new RegistroAlerta()) == 1)  // Si por lo menos hay un registro o no ocurrió algún error..
            {
                map.clear();
                View marker = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_info_map, null);

                for (int i = 0; i < ListRegistrosAlertas.getListHistorial().size(); i++)
                {
                    HistorialRegistros historialRegistros = new HistorialRegistros();
                    historialRegistros      = ListRegistrosAlertas.getListHistorial().get(i);
                    LatLng coordenadas 		= new LatLng(Double.valueOf(historialRegistros.getLatitud()), Double.valueOf(historialRegistros.getLongitud()));


                    TextView txtTipoAlerta  = (TextView) marker.findViewById(R.id.num_servicio);
                    ImageView iconAlerta    = (ImageView) marker.findViewById(R.id.img_marcador);
                    iconAlerta.setImageDrawable(getResources().getDrawable(R.drawable.ic_marcador_alerta));
                    txtTipoAlerta.setText(historialRegistros.getIdTipoAlerta());

//                    if (historialRegistros.getIdTipoAlerta().equals("1"))
//                    {
//                        Drawable myDrawable;
//                        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
//                            myDrawable = getDrawable(R.drawable.ic_marcador_alerta);
//                        } else {
//                            myDrawable = getResources().getDrawable(R.drawable.ic_marcador_alerta);
//                        }
//                        iconAlerta.setImageDrawable(myDrawable);
//                    }
                    //iconAlerta.setImageDrawable(getResources().getDrawable(R.drawable.ic_marcador_alerta));

                    // Agregamos opciones de marcador Principal (Posicion e Icono)
                    MarkerOptions OpcionesMarcador = new MarkerOptions().position(coordenadas);
                    OpcionesMarcador.icon(BitmapDescriptorFactory.fromBitmap(CrearImagen_Vista(marker)));

                    // Customizamos los datos del Info Marcador
//                    Marker MarcadorActual = map.addMarker(OpcionesMarcador);
//                    RegistroAlerta registroAlerta = new RegistroAlerta();
//                    registroAlerta.setIdTipoAlerta(Integer.valueOf(IdTipoAlerta));
//                    registroAlerta.setFechaHoraSqlite(FechaHora);
//
//
//                    MarcadorHashMap.put(MarcadorActual, registroAlerta);
                }

            }
        } catch (Exception e)
        {
            //Log.e("ss", e.getMessage());
        }


    }
    public Bitmap CrearImagen_Vista(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }




    public void onClickSmsGps(View view)
    {
        switch (view.getId())
        {
            case R.id.btnEnviarSms:

                break;
            case R.id.btnBluetooth:
                ServicioSms.escuchar();
                break;
        }
    }


    @Override
    public void listenerTimer()
    {

    }



}
