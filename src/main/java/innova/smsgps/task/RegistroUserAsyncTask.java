package innova.smsgps.task;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import innova.smsgps.application.Globals;
import innova.smsgps.constantes.CONSTANT;
import innova.smsgps.entities.LoginUser;
import innova.smsgps.entities.User;
import innova.smsgps.enums.IDSP1;
import innova.smsgps.managerhttp.Httppostaux;

/**
 * Created by USUARIO on 19/05/2016.
 */
public class RegistroUserAsyncTask extends AsyncTask< String, Integer, Integer > {


    String URL = CONSTANT.PATH_WS + CONSTANT.WS_REGISTRO_USUARIO;

    String description = "";
    String passwordUsuario = "";

    User user = new User();
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
        // guardamos password para regresarlo en el callback (no md5)
        this.passwordUsuario = user.getPassword();
    }

    // region ciclos asynctask
    protected Integer doInBackground(String... params)
    {
        return autenticacion();
    }


    protected void onPostExecute(Integer result)
    {
        LoginUser loginUser = new LoginUser(result, description, this.user);
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
        nameValuePairs.add(new BasicNameValuePair("lenguajeUsuario"     , user.getLanguaje())); // agregado..
        JSONArray jsonArray =   httppostaux.getserverdata(nameValuePairs, URL);
        if (jsonArray != null && jsonArray.length() > 0)
        {
            JSONObject jsonObject;
            try
            {
                jsonObject  =   jsonArray.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                status      =   jsonObject.getInt("status");
                description =   jsonObject.getString("description");
                if (jsonArray.length() > 1) // actualizamos datos registrados anteriormente...
                {
                    jsonObject = jsonArray.getJSONObject(1);
//                    public User(String idFacebook, String firstName, String email, String lastName, String gender, String password, String languajeEdit, String birthDayEdit, String countryEdit) {
                    String nickUsuario      = jsonObject.getString("NickUsuario");
                    String passwordUsuario  = this.passwordUsuario;
                    String generoUsuario    = jsonObject.getString("GeneroUsuario");
                    String apellidosUsuario = jsonObject.getString("ApellidosUsuario");
                    String onomasticoUsuario= jsonObject.getString("OnomasticoUsuario");
                    String ciudadUsuario    = jsonObject.getString("CiudadUsuario");
                    String fotoURL          = jsonObject.getString("FotoURL");
                    String nombreUsuario    = jsonObject.getString("NombreUsuario");
                    String lenguajeUsuario  = jsonObject.getString("LenguajeUsuario");
                    String idUsuario        = jsonObject.getString("IdUsuario");
                    String idFacebook       = jsonObject.getString("idFacebook");

                    user = new User(idFacebook, nombreUsuario, nickUsuario, apellidosUsuario, generoUsuario, passwordUsuario, lenguajeUsuario, onomasticoUsuario, ciudadUsuario);
//                    idUsuario =  idUsuario.replaceAll("\\s","");
                    Globals.getInfoMovil().setSpf1(IDSP1.IDUSUARIO, Integer.valueOf(idUsuario));
                }

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