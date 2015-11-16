package innova.smsgps;

import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private HashMap<Marker, HistorialRegistros> marcadorHashMap = new HashMap<Marker, HistorialRegistros>(); // Inicializamos el hashmap

    String URL_connect="http://smsgps.comli.com/ws_android/ws_sms_gps/ws_listar_registros.php";


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
        pendingPintarMarcadores();


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



    //{{ Pintar Marcadores
    public void pendingPintarMarcadores()
    {
        // INDICE
        if (managerSqlite.ejecutarConsulta(62, new RegistroAlerta()) == 1)  // Si por lo menos hay un registro o no ocurrió algún error..
        {
            pintarMarcadores();
        }
    }

    private void pintarMarcadores()
    {
        try
        {
            map.clear();
            View marker = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_info_map, null);

            for (int i = 0; i < ListRegistrosAlertas.getListHistorial().size(); i++)
            {
                HistorialRegistros historialRegistros = new HistorialRegistros();
                historialRegistros      = ListRegistrosAlertas.getListHistorial().get(i);
                LatLng coordenadas 		= new LatLng(Double.valueOf(historialRegistros.getLatitud()), Double.valueOf(historialRegistros.getLongitud()));


                TextView txtTipoAlerta  = (TextView) marker.findViewById(R.id.txtTipoAlerta);
                ImageView iconAlerta    = (ImageView) marker.findViewById(R.id.iconAlerta);

                txtTipoAlerta.setText(historialRegistros.getIdTipoAlerta());
                if (historialRegistros.getIdTipoAlerta().equals("1"))
                {
                    iconAlerta.setImageDrawable(managerUtils.getDrawable(this, R.drawable.ic_marcador_alerta));
                }
                // Agregamos opciones de marcador Principal (Posicion e Icono)
                MarkerOptions OpcionesMarcador = new MarkerOptions().position(coordenadas);
                OpcionesMarcador.icon(BitmapDescriptorFactory.fromBitmap(managerUtils.getBitmapCanvas(ActivityMap.this, marker)));

                // Customizamos los datos del Info Marcador y agregamos el marcador
                Marker MarcadorActual = map.addMarker(OpcionesMarcador);
                marcadorHashMap.put(MarcadorActual, historialRegistros);
                map.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
                map.setOnInfoWindowClickListener(new MarkerInfoWindowAdapter());
            }
        }catch (Exception e){}
    }


    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener
    {

        public MarkerInfoWindowAdapter()
        {
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }
        @Override
        public View getInfoContents(Marker marker)
        {
            View v  = getLayoutInflater().inflate(R.layout.marker_info_window, null);

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


    public void onClickConfig(View view)
    {
        switch (view.getId())
        {
            case R.id.btnSincronizarServidor:
                new asynListarRegistros().execute();
                break;
        }
    }


    @Override
    public void listenerTimer()
    {

    }

    //region REGISTRAR EN POR WEB SERVICES
    private boolean listarRegistros()
    {
        int status = -1;
        ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

        postparameters2send.add(new BasicNameValuePair("limite","10"));

        JSONArray jsonData =   post.getserverdata(postparameters2send, URL_connect);

        if (jsonData!=null && jsonData.length() > 0)
        {
            JSONObject json_data;
            try {
                json_data   = jsonData.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                status      =   json_data.getInt("status");
                Log.e("loginstatus", "status= " + status);//muestro por log que obtuvimos
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (status  ==  0)  // [{"status":"0"}]
            {
                return false;
            }
            else                // [{"status":"1"}] ACTUALIZAMOS FLAG
            {
                // Armamos arreglo
                JSONObject jsonObject;
                ListRegistrosAlertas listRegistrosAlertas   = new ListRegistrosAlertas();
                ArrayList<HistorialRegistros> list          = new ArrayList<HistorialRegistros>();

                for (int i = 1; i < jsonData.length(); i++)
                {
                    try {
                        jsonObject = jsonData.getJSONObject(i);
                        HistorialRegistros historial = new HistorialRegistros();               // BAD PRACTICE
                        historial.setIdFacebook(jsonObject.getString("IdFacebook"));
                        historial.setIdTipoAlerta(jsonObject.getString("IdTipoAlerta"));
                        historial.setLatitud(jsonObject.getString("Lat"));
                        historial.setLongitud(jsonObject.getString("Lng"));
                        historial.setFechaHora(jsonObject.getString("FechaHora"));
                        list.add(historial);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listRegistrosAlertas.setListHistorial(list);
                return true;
            }

        }else   //json obtenido invalido verificar parte WEB.
        {
            return false;
        }
    }



    class asynListarRegistros extends AsyncTask< String, Integer, Integer > {

        //        String user,pass;
        protected Integer doInBackground(String... params) {
            //enviamos y recibimos y analizamos los datos en segundo plano.
            if (listarRegistros() == true){
                return 1; //login valido
            }else{
                return 0; //login invalido
            }

        }
        protected void onPostExecute(Integer result)
        {
            if(result == 1)
            {
                pintarMarcadores();
            }
        }

    }
    //endregion

}
