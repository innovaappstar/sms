package innova.smsgps.task;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import innova.smsgps.constantes.CONSTANT;
import innova.smsgps.entities.RegistroTrack;
import innova.smsgps.entities.RegistroTrackUser;
import innova.smsgps.managerhttp.Httppostaux;
import innova.smsgps.utils.Utils;

/**
 * Created by USUARIO on 19/05/2016.
 */
public class RegistroTrackAsyncTask extends AsyncTask< String, Integer, Integer > {



    String URL = CONSTANT.PATH_WS + CONSTANT.WS_REGISTRO_ALERTA;

    String description = "";

    RegistroTrack registroTrack = new RegistroTrack();
    RegistroTrackCallback rtCallback;
    Httppostaux httppostaux;


    public interface RegistroTrackCallback
    {
        void onRegistroTrack(RegistroTrackUser registroTrackUser);
    }

    public RegistroTrackAsyncTask(RegistroTrackCallback rtCallback, RegistroTrack registroTrack)
    {
        httppostaux = new Httppostaux();
        this.rtCallback = rtCallback;
        this.registroTrack = registroTrack;
    }

    // region ciclos asynctask
    protected Integer doInBackground(String... params)
    {
        return registroTrack();
    }


    protected void onPostExecute(Integer result)
    {
        RegistroTrackUser registroTrackUser = new RegistroTrackUser(result, description);
        rtCallback.onRegistroTrack(registroTrackUser);
    }
    //endregion

//  ($nickUsuario, $lat, $lng, $fechaHora)

    // función de fondo..
    private int registroTrack()
    {
        int status = 0;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


        nameValuePairs.add(new BasicNameValuePair("action"          , String.valueOf(registroTrack.getACTION())));
        nameValuePairs.add(new BasicNameValuePair("nickUsuario"     , registroTrack.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("lat"             , registroTrack.getLatitud()));
        nameValuePairs.add(new BasicNameValuePair("lng"             , registroTrack.getLongitud()));
        nameValuePairs.add(new BasicNameValuePair("fechaHora"       , registroTrack.getFechaHora()));

        nameValuePairs.add(new BasicNameValuePair("idUsuario"       , registroTrack.getIdUsuario()));
        nameValuePairs.add(new BasicNameValuePair("velocidad"       , registroTrack.getVelocidad()));
        nameValuePairs.add(new BasicNameValuePair("bateria"         , registroTrack.getBateria()));



        JSONArray jsonArray =   httppostaux.getserverdata(nameValuePairs, URL);
        if (jsonArray != null && jsonArray.length() > 0)
        {
            JSONObject jsonObject;
            try
            {
                jsonObject  =   jsonArray.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                status      =   jsonObject.getInt("status");
                description =   jsonObject.getString("description");

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