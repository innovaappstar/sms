package innova.smsgps.task;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import innova.smsgps.constantes.CONSTANT;
import innova.smsgps.entities.LoginUser;
import innova.smsgps.entities.User;
import innova.smsgps.managerhttp.Httppostaux;

/**
 * Created by USUARIO on 19/05/2016.
 */
public class RegistroUserAsyncTask extends AsyncTask< String, Integer, Integer > {


    String URL = CONSTANT.PATH_WS + CONSTANT.WS_REGISTRO_USUARIO;

    String description = "";

    User user = null;
    RegistroUsuarioCallback ruCallback;
    Httppostaux httppostaux;


    public interface RegistroUsuarioCallback
    {
        void onLoginUser(LoginUser loginUser);
    }

    public RegistroUserAsyncTask(RegistroUsuarioCallback ruCallback, User user)
    {
        httppostaux = new Httppostaux();
        this.ruCallback = ruCallback;
        this.user       = user;
    }

    // region ciclos asynctask
    protected Integer doInBackground(String... params)
    {
        return autenticacion();
    }


    protected void onPostExecute(Integer result)
    {
        LoginUser loginUser = new LoginUser(result, description);
        ruCallback.onLoginUser(loginUser);
    }
    //endregion


    // función de fondo..
    private int autenticacion()
    {
        int status = 0;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("nickUsuario"         , user.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("passwordUsuario"     , user.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("nombreUsuario"       , user.getFirstName()));
        nameValuePairs.add(new BasicNameValuePair("apellidosUsuario"    , user.getLastName()));
        JSONArray jsonArray =   httppostaux.getserverdata(nameValuePairs, URL);
        if (jsonArray != null && jsonArray.length() > 0)
        {
            JSONObject jsonObject;
            try
            {
                jsonObject  =   jsonArray.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                status      =   jsonObject.getInt("status");
                description =   jsonObject.getString("description");
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