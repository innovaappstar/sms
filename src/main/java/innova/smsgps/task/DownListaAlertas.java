package innova.smsgps.task;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import innova.smsgps.beans.HistorialRegistros;
import innova.smsgps.beans.ListRegistrosAlertas;
import innova.smsgps.constantes.CONSTANT;
import innova.smsgps.managerhttp.Httppostaux;

/**
 * Created by USUARIO on 28/11/2015.
 */
public class DownListaAlertas extends AsyncTask< String, Integer, Integer > {


    String URL = CONSTANT.PATH_WS + CONSTANT.WS_LISTAR_ALERTAS;


    /**
     * CALLBACK QUE ACTUARAAA DE LISTENER
     **/
    DownListaAlertasCallback listaCallback;

    /**
     * INTERFAZ QUE CONTENDR EL MTODO CALLBACK
     **/
    public interface DownListaAlertasCallback
    {
        void DownListaExecute(int resultado, ListRegistrosAlertas listRegistrosAlertas);
    }


    /**
     * M?todo para probar webServices
     */
    Httppostaux post;

    public DownListaAlertas(DownListaAlertasCallback listaCallback)
    {
        post                = new Httppostaux();
        this.listaCallback  = listaCallback;
    }


    //        String user,pass;
    protected Integer doInBackground(String... params)
    {
        return listarAlertas(params);
    }

    /*Una vez terminado doInBackground segun lo que halla ocurrido
    pasamos a la sig. activity
    o mostramos error*/
    protected void onPostExecute(Integer result)
    {
    }


    private int listarAlertas(String[] parametros)
    {
        int status = -1;
        ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

        postparameters2send.add(new BasicNameValuePair("limite"       ,parametros[0]));
//        postparameters2send.add(new BasicNameValuePair("nickUsuario"       ,parametros[1]));
        JSONArray jdata =   post.getserverdata(postparameters2send, URL);

        if (jdata != null && jdata.length() > 0)
        {
            JSONObject json_data;
            try {
                json_data   = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                status      = json_data.getInt("status");


                if (status  ==  0)  // [{"status":"0"}]
                {
                    return 0;
                }
                else                // [{"status":"1"}] ACTUALIZAMOS FLAG
                {
                    // Armamos arreglo
                    JSONObject jsonObject;
                    ListRegistrosAlertas listRegistrosAlertas   = new ListRegistrosAlertas();
                    ArrayList<HistorialRegistros> list          = new ArrayList<HistorialRegistros>();

                    for (int i = 1; i < jdata.length(); i++)
                    {
                        try {
                            jsonObject = jdata.getJSONObject(i);
                            HistorialRegistros historial = new HistorialRegistros();               // BAD PRACTICE
                            historial.setIdFacebook(jsonObject.getString("NickUsuario"));
                            historial.setIdTipoAlerta(jsonObject.getString("IdTipoAlerta"));
                            historial.setLatitud(jsonObject.getString("Lat"));
                            historial.setLongitud(jsonObject.getString("Lng"));
                            historial.setFechaHora(jsonObject.getString("FechaHora"));
                            list.add(historial);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    listRegistrosAlertas.setListaAlertas(list);
                    listaCallback.DownListaExecute(1, listRegistrosAlertas);


                    return 1;
                }
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