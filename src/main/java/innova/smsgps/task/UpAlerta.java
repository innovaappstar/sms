package innova.smsgps.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import innova.smsgps.beans.RegistroAlerta;
import innova.smsgps.constantes.CONSTANT;
import innova.smsgps.managerhttp.Httppostaux;
import innova.smsgps.sqlite.ManagerSqlite;
import innova.smsgps.utils.ManagerUtils;

/**
 * Created by USUARIO on 28/11/2015.
 */
public class UpAlerta extends AsyncTask< String, String, String > {

    /**
     * ($nickUsuario, $lat, $lng, $fechaHora)
     */

    /**
     * URL WS
     **/
    String URL = CONSTANT.PATH_WS + CONSTANT.WS_REGISTRO_ALERTA;
    /**
     * INDICES SQLITE
     **/
    private int CRUD_ACTUALIZAR = 21;

    /***/
    int mTipoAlerta = 0;

    /**
     * REGISTRAR ALERTA
     **/
    RegistroAlerta registroAlerta = new RegistroAlerta();
    /**
     * M?todo para probar webServices
     */
    Httppostaux post;
    ManagerSqlite managerSqlite                 = null;     // MANEJADOR DE FUNCIONES C.R.U.D. SQLITE.
    ManagerUtils managerUtils                   = null;     // MANEJADOR DE FUNCIONES TIPO UTILS.
    /**
     * Enumerables
    **/
    Context mContext;


    public UpAlerta(Context context, int tipoAlerta)
    {
        post                = new Httppostaux();
        mContext            = context;
        managerSqlite       = new ManagerSqlite(mContext);
        managerUtils        = new ManagerUtils();
        this.mTipoAlerta    = tipoAlerta;
        insertarAlerta();
    }

    private void insertarAlerta()
    {
        // Tipo de ALerta
        // EJECUTAMOS ASYNTASK PARA REGISTRAR EN WEBSERVICE
        registroAlerta.setIdTipoAlerta(mTipoAlerta);
        int indiceEjecutarConsultaAlerta = 1;
        if (managerSqlite.ejecutarConsulta(indiceEjecutarConsultaAlerta, registroAlerta, null , null) == 1)   // NULL = BAD PRACTICE
        {
            this.execute();
        }
    }

    //        String user,pass;
    protected String doInBackground(String... params) {
        if (registroCorrecto()){
            return "ok";
        }else{
            return "err";
        }

    }

    /*Una vez terminado doInBackground segun lo que halla ocurrido
    pasamos a la sig. activity
    o mostramos error*/
    protected void onPostExecute(String result)
    {
        if(result.equals("ok"))
        {
            if (managerSqlite.ejecutarConsulta(CRUD_ACTUALIZAR, registroAlerta, null , null) == 1) // BAD PRACTICE
            {
                managerUtils.imprimirToast(mContext, "Registrado correctamente en el servidor...");
            }
        }else
        {

        }
//        managerUtils.imprimirToast(mContext, result);
    }




    private boolean registroCorrecto()
    {
        int status = -1;
        ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

        postparameters2send.add(new BasicNameValuePair("NickUsuario"            ,registroAlerta.getNickUsuario()));  // IDFACEBOOK X NICKUSUARIO
        postparameters2send.add(new BasicNameValuePair("idTipoAlerta"           ,registroAlerta.getIdTipoAlerta() + ""));
        postparameters2send.add(new BasicNameValuePair("lat"                    ,registroAlerta.getLatitud()));
        postparameters2send.add(new BasicNameValuePair("lng"                    ,registroAlerta.getLongitud()));
        postparameters2send.add(new BasicNameValuePair("fechaHora"              ,registroAlerta.getFechaHora()));
        postparameters2send.add(new BasicNameValuePair("IdRegistroAlertasMovil" ,registroAlerta.getIdRegistroAlertasMovil()));

        JSONArray jdata =   post.getserverdata(postparameters2send, URL);

        if (jdata!=null && jdata.length() > 0)
        {
            JSONObject json_data;
            try {
                json_data   = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
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
                return true;
            }

        }else   //json obtenido invalido verificar parte WEB.
        {
            return false;
        }
    }

}