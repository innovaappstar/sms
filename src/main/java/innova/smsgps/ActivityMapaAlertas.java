package innova.smsgps;

import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import innova.smsgps.marker.MarkerInfoWindowAdapter;
import innova.smsgps.task.DownListaAlertas;
import innova.smsgps.task.DownListaAlertas.DownListaAlertasCallback;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityMapaAlertas extends BaseActivity implements DownListaAlertasCallback{

    RelativeLayout contenedorMapaView;
    LinearLayout contenedorMapaPrincipal;
    private Handler mHandler = new Handler();

    private boolean expandir = false;
    private GoogleMap map;
    private HashMap<Marker, HistorialRegistros> marcadorHashMap = new HashMap<Marker, HistorialRegistros>(); // Inicializamos el hashmap

    DownListaAlertas objDownListaAlertas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_mapa_alertas_apolo);
        contenedorMapaView      = (RelativeLayout)findViewById(R.id.contenedorMapaView);
        contenedorMapaPrincipal = (LinearLayout)findViewById(R.id.contenedorPrincipalMapa);
        objDownListaAlertas     = new DownListaAlertas(this);

        // map
        FragmentManager myFragmentManager = getFragmentManager();
        MapFragment myMapFragment   = (MapFragment)myFragmentManager.findFragmentById(R.id.map);
        map = myMapFragment.getMap();
        map.setMyLocationEnabled(true);
        map.moveCamera(UltimaUbicacionCamara());        //myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void listenerTimer() {

    }



    /**
     * Evento que se ejecutará al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.contenedorExpandir:
                expandir(contenedorMapaView, expandir);
                return;
            case R.id.contenedorAlertas:
//                enviarMensajeIPC(1, new String[]{"1", "2"});
                objDownListaAlertas.execute(new String[]{"10"});

                return;
        }
    }

    private void expandir(View view, boolean expandir)
    {
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        ObjectAnimator anim = null;
        if (!expandir)
            anim = ObjectAnimator.ofFloat(view, "translationY", 0, getTOP() );
        else
            anim = ObjectAnimator.ofFloat(view, "translationY", getTOP(), 0 );

        anim.setInterpolator(bounceInterpolator);
        anim.setDuration(1100).start();
        setParams(view, expandir);

        this.expandir =! expandir;
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

    private void pintarMarcadores(final ListRegistrosAlertas listRegistrosAlertas)
    {
        try
        {
            // PASO 03
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    mHandler.post(new Runnable()
//                    {
//                        @Override
//                        public void run()
//                        {
//
//                        }
//                    });
//                }
//            }).start();
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    map.clear();
                    View marker = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_info_map, null);

                    for (int i = 0; i < listRegistrosAlertas.getListaAlertas().size(); i++)
                    {
                        HistorialRegistros historialRegistros = new HistorialRegistros();
                        historialRegistros      = listRegistrosAlertas.getListaAlertas().get(i);
                        LatLng coordenadas 		= new LatLng(Double.valueOf(historialRegistros.getLatitud()), Double.valueOf(historialRegistros.getLongitud()));


                        TextView txtTipoAlerta  = (TextView) marker.findViewById(R.id.txtTipoAlerta);
                        ImageView iconAlerta    = (ImageView) marker.findViewById(R.id.iconAlerta);

                        txtTipoAlerta.setText(historialRegistros.getIdTipoAlerta());
                        if (historialRegistros.getIdTipoAlerta().equals("1"))
                        {
                            iconAlerta.setImageDrawable(managerUtils.getDrawable(ActivityMapaAlertas.this, R.drawable.ic_marcador_alerta));
                        }
                        // Agregamos opciones de marcador Principal (Posicion e Icono)
                        MarkerOptions OpcionesMarcador = new MarkerOptions().position(coordenadas);
                        OpcionesMarcador.icon(BitmapDescriptorFactory.fromBitmap(managerUtils.getBitmapCanvas(ActivityMapaAlertas.this, marker)));

                        // Customizamos los datos del Info Marcador y agregamos el marcador
                        Marker MarcadorActual = map.addMarker(OpcionesMarcador);
                        marcadorHashMap.put(MarcadorActual, historialRegistros);
                        map.setInfoWindowAdapter(new MarkerInfoWindowAdapter(getApplicationContext(), marcadorHashMap));
                        map.setOnInfoWindowClickListener(new MarkerInfoWindowAdapter(getApplicationContext(), marcadorHashMap));
                    }
                }
            });



        }catch (Exception e)
        {
            managerUtils.imprimirLog(e.getMessage());
        }
    }

    //region getLongitud Altura Vistas

    public int getTOP()
    {
        return (getContenedorHeight()/2 - getContenedorHeight()) + getTitleBarHeight()/2;
    }
    /**
     * Establece parámetros de layout
     * @param view
     * @param expandir
     */
    private void setParams(View view, boolean expandir)
    {
        LinearLayout.LayoutParams param = null;
        if(!expandir)
            param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT, 0f);
        else
            param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,0, 3.0f);

//        param.topMargin = getStatusBarHeight();
        view.setLayoutParams(param);
    }

    private int getTitleBarHeight() {
        int viewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return (viewTop - getStatusBarHeight());
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Simple función que retorna la longitud del
     * contenedor padre.
     * @return int
     **/
    public int getContenedorHeight()
    {
        return contenedorMapaPrincipal.getHeight();
    }

    /**
     * Simple callback que regresará la lista de alertas
     * del servidor..
     **/

    @Override
    public void DownListaExecute(int resultado, ListRegistrosAlertas listRegistrosAlertas) {
        if ( resultado == 1)
        {
            pintarMarcadores(listRegistrosAlertas);
        }else
        {
            managerUtils.imprimirToast(this, "Ocurrio un error durante la consulta.");
        }
    }
    //endregion


}



