package innova.smsgps.task;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import innova.smsgps.constantes.CONSTANT;
import innova.smsgps.constantes.Constantes;
import innova.smsgps.entities.InfoProcesos;
import innova.smsgps.entities.User;
import innova.smsgps.managerhttp.Httppostaux;

/**
 * Created by USUARIO on 19/05/2016.
 */
public class LoginUserAsyncTask extends AsyncTask< String, Integer, Integer > {


    String URL = CONSTANT.PATH_WS + CONSTANT.WS_LOGIN_USUARIO;
    String detalle = "";

    User user = null;
    LoginUsuarioCallback luCallback;
    Httppostaux httppostaux;


    public interface LoginUsuarioCallback
    {
        void onLoginUser(InfoProcesos infoProcesos);
    }

    public LoginUserAsyncTask(LoginUsuarioCallback luCallback, User user)
    {
        httppostaux = new Httppostaux();
        this.luCallback = luCallback;
        this.user       = user;
    }

    // region ciclos asynctask
    protected Integer doInBackground(String... params)
    {
        return autenticacion(params);
    }


    protected void onPostExecute(Integer result)
    {
        InfoProcesos infoProcesos = new InfoProcesos();
        if (result == Constantes.RESULT_ERROR)
            infoProcesos.setDetalleError(detalle);

        luCallback.onLoginUser(infoProcesos);
    }
    //endregion


    // función de fondo..
    private int autenticacion(String[] parametros)
    {
        int status = 0;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("actionLogin"       , user.getACTION_LOGIN()));
        nameValuePairs.add(new BasicNameValuePair("nickUsuario"       , user.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("passwordUsuario"   , user.getPassword()));
        if (parametros.length > 3)
        {
            nameValuePairs.add(new BasicNameValuePair("nombreUsuario"     ,user.getFirstName()));
            nameValuePairs.add(new BasicNameValuePair("apellidosUsuario"  ,user.getLastName()));
            nameValuePairs.add(new BasicNameValuePair("generoUsuario"     ,user.getGender()));
            nameValuePairs.add(new BasicNameValuePair("ciudadUsuario"     ,user.getLocale()));
            nameValuePairs.add(new BasicNameValuePair("idFacebook"        ,user.getId()));
        }
        JSONArray jsonArray =   httppostaux.getserverdata(nameValuePairs, URL);
        if (jsonArray != null && jsonArray.length() > 0)
        {
            JSONObject jsonObject;
            try
            {
                jsonObject  =   jsonArray.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                status      =   jsonObject.getInt("status");
            } catch (JSONException e)
            {
                detalle = e.getMessage();
            }
            return status; // [{"status":"0"}] -- [{"status":"1"}] -- [{"status":"-1"}]
        }else   //json obtenido invalido verificar parte WEB.
        {
            detalle = "json null.";
            return status;
        }
    }

}