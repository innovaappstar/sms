package innova.smsgps.task;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import innova.smsgps.constantes.CONSTANT;
import innova.smsgps.entities.Friend;
import innova.smsgps.entities.Tracker;
import innova.smsgps.entities.TrackerUser;
import innova.smsgps.managerhttp.Httppostaux;
import innova.smsgps.utils.Utils;

/**
 * Created by USUARIO on 19/05/2016.
 */
public class TrackerAsyncTask extends AsyncTask< String, Integer, Integer > {


    String URL = CONSTANT.PATH_WS + CONSTANT.WS_TRACKER_MAP;

    String description = "";

    ArrayList<Friend> alFriend      = new ArrayList<Friend>();
    ArrayList<Tracker> alTracker    = new ArrayList<Tracker>();
//    int ACTION = 0;


    TrackerUsuarioCallback tuCallback;
    Httppostaux httppostaux;

    Tracker tracker = new Tracker();

    public interface TrackerUsuarioCallback
    {
        void onTrackerUser(TrackerUser trackerUser);
    }

    public TrackerAsyncTask(TrackerUsuarioCallback tuCallback, Tracker tracker)
    {
        httppostaux     = new Httppostaux();
        this.tuCallback = tuCallback;
        this.tracker    = tracker;
    }

    // region ciclos asynctask
    protected Integer doInBackground(String... params)
    {
        return tracking();
    }


    protected void onPostExecute(Integer result)
    {
        TrackerUser trackerUser = new TrackerUser(result, description , this.tracker);
        trackerUser.setAlFriend(alFriend);
        trackerUser.setAlTracker(alTracker);

        tuCallback.onTrackerUser(trackerUser);
    }
    //endregion


    // función de fondo..
    private int tracking()
    {
        int status = 0;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("idUsuario"   , String.valueOf(tracker.getIdUsuario())));
        nameValuePairs.add(new BasicNameValuePair("email"       , tracker.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("actionTrack" , String.valueOf(tracker.getACTION())));
        nameValuePairs.add(new BasicNameValuePair("fechaHoraInicio" ,tracker.getFechaInicio()));
        nameValuePairs.add(new BasicNameValuePair("fechaHoraFinal"  ,tracker.getFechaFin()));
        nameValuePairs.add(new BasicNameValuePair("idFriend"    , String.valueOf(tracker.getIdFriend())));

        JSONArray jsonArray =   httppostaux.getserverdata(nameValuePairs, URL);
        if (jsonArray != null && jsonArray.length() > 0)
        {
            Utils.LOG(jsonArray.length() + " size" );
            JSONObject jsonObject;
            try
            {
                jsonObject  =   jsonArray.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                status      =   jsonObject.getInt("status");
                description =   jsonObject.getString("description");
                if (jsonArray.length() > 1) // actualizamos datos registrados anteriormente...
                {
                    for (int i = 1; i < jsonArray.length(); i++)    // loop
                    {
                        jsonObject  =   jsonArray.getJSONObject(i);
                        if (tracker.getACTION() == Tracker.LISTAR || tracker.getACTION() == Tracker.UBICACION)
                        {
                            int idUsuarioFriend     = Integer.valueOf(jsonObject.getString("IdUsuario"));   // cast
                            String lat              = jsonObject.getString("Lat");
                            String lng              = jsonObject.getString("Lng");
                            String velocidad        = jsonObject.getString("Velocidad");
                            String bateria          = jsonObject.getString("Bateria");
                            String fechaHora        = jsonObject.getString("FechaHora");

                            alTracker.add(new Tracker(lat, lng, velocidad, bateria, fechaHora, idUsuarioFriend));
                        }else if (tracker.getACTION() == Tracker.SINCRONIZAR)
                        {
                            int idUsuarioFriend     = Integer.valueOf(jsonObject.getString("IdUsuario"));   // cast
                            String nickFriend       = jsonObject.getString("NickUsuario");
                            String nombreFriend     = jsonObject.getString("NombreUsuario");
                            String apellidoFriend   = jsonObject.getString("ApellidosUsuario");
                            String fotoURL          = jsonObject.getString("FotoURL");

                            alFriend.add(new Friend(idUsuarioFriend, nickFriend, nombreFriend, apellidoFriend, fotoURL));
                        }
                    }
                }
                Utils.LOG(jsonArray.toString());
            } catch (JSONException e)
            {
                description = e.getMessage();
            }
            return status; // [{"status":"0"}] -- [{"status":"1"}] -- [{"status":"-1"}]
        }else   //json obtenido invalido verificar parte WEB.
        {
            description = "json null.";
            return status;
        }
    }

}