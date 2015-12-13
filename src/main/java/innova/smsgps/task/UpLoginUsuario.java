package innova.smsgps.task;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import innova.smsgps.constantes.CONSTANT;
import innova.smsgps.managerhttp.Httppostaux;

/**
 * Created by USUARIO on 28/11/2015.
 */
public class UpLoginUsuario extends AsyncTask< String, Integer, Integer > {


    String URL = CONSTANT.PATH_WS + CONSTANT.WS_LOGIN_USUARIO;


    /**
     * CALLBACK QUE ACTUARÁ DE LISTENER
     **/
    LoginUsuarioCallback loginCallback;

    /**
     * INTERFAZ QUE CONTENDRÁ EL MÉTODO CALLBACK
     **/
    public interface LoginUsuarioCallback
    {
        void LoginUsuarioExecute(int resultado);
    }


    /**
     * M?todo para probar webServices
     */
    Httppostaux post;

    public UpLoginUsuario(LoginUsuarioCallback loginCallback)
    {
        post = new Httppostaux();
        this.loginCallback = loginCallback;

    }


    //        String user,pass;
    protected Integer doInBackground(String... params)
    {
        return loginStatus(params);
    }

    /*Una vez terminado doInBackground segun lo que halla ocurrido
    pasamos a la sig. activity
    o mostramos error*/
    protected void onPostExecute(Integer result)
    {
        loginCallback.LoginUsuarioExecute(result);
    }


    private int loginStatus(String[] parametros)
    {
        int status = -1;
        ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

        postparameters2send.add(new BasicNameValuePair("actionLogin"       ,parametros[0]));
        postparameters2send.add(new BasicNameValuePair("nickUsuario"       ,parametros[1]));
        postparameters2send.add(new BasicNameValuePair("passwordUsuario"   ,parametros[2]));
        if (parametros.length > 3)
        {
            postparameters2send.add(new BasicNameValuePair("nombreUsuario"     ,parametros[3]));
            postparameters2send.add(new BasicNameValuePair("apellidosUsuario"  ,parametros[4]));
            postparameters2send.add(new BasicNameValuePair("generoUsuario"     ,parametros[5]));
            postparameters2send.add(new BasicNameValuePair("ciudadUsuario"     ,parametros[6]));
            postparameters2send.add(new BasicNameValuePair("idFacebook"        ,parametros[7]));
        }

        JSONArray jdata =   post.getserverdata(postparameters2send, URL);

        if (jdata!=null && jdata.length() > 0)
        {
            JSONObject json_data;
            try {
                json_data   = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                status      =   json_data.getInt("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status; // [{"status":"0"}] -- [{"status":"1"}] -- [{"status":"-1"}]

        }else   //json obtenido invalido verificar parte WEB.
        {
            return 0;
        }
    }

}