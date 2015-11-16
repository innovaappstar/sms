package innova.smsgps;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import innova.smsgps.beans.RegistroAlerta;
import innova.smsgps.managerhttp.Httppostaux;
import innova.smsgps.sqlite.ManagerSqlite;

/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityMenu extends Activity {
    Button   btnConfiguracion;
    /**
     * insertaremos registros manualmente..
     */
    RegistroAlerta registroAlerta = new RegistroAlerta();
    /**
     * Método para probar webServices
     */
    Httppostaux post;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_menu);

        btnConfiguracion     = (Button) findViewById(R.id.btnConfiguracion);
        iniciarServicio();
        post = new Httppostaux();
    }

    private void iniciarServicio()
    {
        if(!ServicioSms.isRunning())
            startService(new Intent(this, ServicioSms.class));
    }


    /**
     * Evento que se ejecutará al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */

    public void onClickConfig(View view) {
        switch (view.getId()) {
            case R.id.btnConfiguracion:
                // SALTAR A LA OTRA ACTIVIDAD
                Intent i = new Intent(this, ActivityConfigContactos.class);
                startActivity(i);
//                finish();
                break;
            case R.id.btnPostearUbicacion:
                //Coordenada coordenada = new Coordenada();
                //Globals.PostearSegundoPlano(coordenada);


                // EJECUTAMOS ASYNTASK PARA REGISTRAR EN WEBSERVICE
                registroAlerta.setIdFacebook("12345678910112");
                registroAlerta.setIdTipoAlerta(1);
                ManagerSqlite managerSqlite = new ManagerSqlite(getApplicationContext());
                if (managerSqlite.ejecutarConsulta(1, registroAlerta) == 1)
                {
                    imprimitToast("SE INSERTÓ CORRECTAMENTE");
                    new asynRegistroAlerta().execute();
                }
                break;
            case R.id.btnListarRegistrosAlertas:
                // SALTAR A LA OTRA ACTIVIDAD
                Intent i2 = new Intent(this, ActivityListaRegistroAlertas.class);
                startActivity(i2);
//                finish();
                break;
            case R.id.btnMostrarMap:
                // SALTAR A LA OTRA ACTIVIDAD
                Intent i3 = new Intent(this, ActivityMap.class);
                startActivity(i3);
//                finish();
                break;
            case R.id.btnPostDenuncia:
                startActivity(new Intent(this, ActivityPostDenuncia.class));
                break;
        }
    }
    private void imprimitToast(String data)
    {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    //region REGISTRAR EN POR WEB SERVICES
    private boolean registroCorrecto()
    {
        int status = -1;
        ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

        postparameters2send.add(new BasicNameValuePair("idFacebook"             ,registroAlerta.getIdFacebook()));
        postparameters2send.add(new BasicNameValuePair("idTipoAlerta"           ,registroAlerta.getIdTipoAlerta() + ""));
        postparameters2send.add(new BasicNameValuePair("lat"                    ,registroAlerta.getLatitud()));
        postparameters2send.add(new BasicNameValuePair("lng"                    ,registroAlerta.getLongitud()));
        postparameters2send.add(new BasicNameValuePair("fechaHora"              ,registroAlerta.getFechaHora()));
        postparameters2send.add(new BasicNameValuePair("IdRegistroAlertasMovil" ,registroAlerta.getIdRegistroAlertasMovil()));

        JSONArray jdata =   post.getserverdata(postparameters2send, URL_connect);

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

    String URL_connect="http://smsgps.comli.com/ws_android/ws_sms_gps/ws_registro_alerta.php";//ruta en donde estan nuestros archivos



    class asynRegistroAlerta extends AsyncTask< String, String, String > {

//        String user,pass;
        protected String doInBackground(String... params) {
            if (registroCorrecto() == true){
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
                ManagerSqlite managerSqlite = new ManagerSqlite(getApplicationContext());
                if (managerSqlite.ejecutarConsulta(21, registroAlerta) == 1)
                {
                    imprimitToast("SE actualizo");
                }
            }
            imprimitToast(result);
        }

    }
    //endregion


}


/**
 *
 ------------------------------------
 FUNCIONES DEL APP
 ------------------------------------
    tbRegistroalertas
 id autoincrement
 idfacebook
 idtipoAlerta
 lat
 lng
 fecha Hora
 Libre




 Sos

 -------

 mandar tu posicion en caso de asalto, emergencia y/o accidente a 4 contactos y facebook y graba en el mapa y conectar al servidor

 1. Boton del app para agregar hasta 4 contactos y cuenta de facebook

 2. Boton del app para cancelar emergencia

 3. Boton del app para configurar beeps, 1 bip robo, 2 bips accidente, 3 bips emergencia medica


 Denunciar

 ------------

 1. Va a poder grabar en un mapa el punto de denuncia con una foto y una descripcion y con categorias y publicarlo en  facebook y grabar en el mapa yconectar al servidor.

 Categorias:

 pandillaje

 Venta de droga

 Policia corrupta

 Robo vehiculo

 Violencia domestica

 Robo menor

 Otros

 -----------------------------------------------------------------
 FUNCIONES DEL BOTON ELECTRONICO
 -----------------------------------------------------------------

 Hasta 3 bips personalizables indicando el problema

 1 bip robo conecta al app y envia el mensaje a los 4 contactos y cuenta de facebook

 2 bips accidente  conecta al app y envia el mensaje a los 4 contactos y cuenta de facebook

 3 bip emergencia medica  conecta al app y envia el mensaje a los 4 contactos y cuenta de facebook


 **/

