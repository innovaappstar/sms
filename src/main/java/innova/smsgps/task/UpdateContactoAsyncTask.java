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
import innova.smsgps.entities.Contacto;
import innova.smsgps.entities.ContactoUser;
import innova.smsgps.enums.IDSP1;
import innova.smsgps.managerhttp.Httppostaux;
import innova.smsgps.utils.Utils;

/**
 * Created by USUARIO on 19/05/2016.
 */
public class UpdateContactoAsyncTask extends AsyncTask< String, Integer, Integer > {


    String URL = CONSTANT.PATH_WS + CONSTANT.WS_UPDATE_CONTACTO;

    String description = "";


    Contacto contacto = new Contacto();
    ContactoCallback cCallback;
    Httppostaux httppostaux;


    public interface ContactoCallback
    {
        void onContacto(ContactoUser contactoUser, Contacto contacto);
    }

    public UpdateContactoAsyncTask(ContactoCallback cCallback, Contacto contacto)
    {
        httppostaux = new Httppostaux();
        this.cCallback = cCallback;
        this.contacto = contacto;
    }

    // region ciclos asynctask
    protected Integer doInBackground(String... params)
    {
        return autenticacion();
    }


    protected void onPostExecute(Integer result)
    {
        ContactoUser contactoUser = new ContactoUser(result, description);
        cCallback.onContacto(contactoUser, this.contacto);
    }
    //endregion


    // función de fondo..
    private int autenticacion()
    {
        int status = 0;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("actionContacto"      , String.valueOf(contacto.getACTION())));
        nameValuePairs.add(new BasicNameValuePair("idUsuario"           , String.valueOf(Globals.getInfoMovil().getSPF1(IDSP1.IDUSUARIO))));
        nameValuePairs.add(new BasicNameValuePair("codContacto"         , contacto.getCodContacto()));

        nameValuePairs.add(new BasicNameValuePair("emailContacto"       , contacto.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("nombreContacto"      , contacto.getNombre()));
        nameValuePairs.add(new BasicNameValuePair("telefonoContacto"    , contacto.getTelefono()));

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