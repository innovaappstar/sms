package innova.smsgps;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import innova.smsgps.beans.RegistroAlerta;
import innova.smsgps.managerhttp.Httppostaux;

/**
 * Created by innovaapps on 30/10/2015.
 */
public class FragmentDenunciasRegistro extends BaseFragment
{
    ViewGroup rootView;
    private static  final int REQUEST_GALERIA    = 1;
    private static  final int REQUEST_FOTO      = 2;

    public static String ID_PATH = "ID_PATH";

    String imgPath, fileName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // SETEANDO ELEMENTOS
    private void IniciandoVistas()
    {
        ((Button)rootView.findViewById(R.id.btnAceptar)).setOnClickListener(this);
        ((ImageView)rootView.findViewById(R.id.img_gps)).setOnClickListener(this);
        iniciarServicio();
        post = new Httppostaux();
    }
    /**
     * Simple m�todo para iniciar servicio..
     **/
    private void iniciarServicio()
    {
        if(!ServicioSms.isRunning())
            getActivity().startService(new Intent(getActivity(), ServicioSms.class));
    }

    //endregion Fragments Fin

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = (ViewGroup)inflater.inflate(R.layout.activity_denuncias_registro, container, false);
        super.IniciarInstancias();
        IniciandoVistas();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == REQUEST_GALERIA && resultCode == getActivity().RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();

                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
            }else if (requestCode == REQUEST_FOTO && resultCode == getActivity().RESULT_OK)
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                RegistroDenuncias.ImgDenunciabmp = photo;
//                imageView.setImageBitmap(photo);
                Uri tempUri = managerUtils.getImageUri(getActivity(), photo);

                imgPath = managerUtils.getRealPathFromURI(getActivity(), tempUri);
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];

                Intent intent = new Intent(getActivity(), ActivityDenunciaRegistroUpload.class);
                intent.putExtra(ID_PATH, imgPath);
                startActivity(intent);

            }else
            {
                managerUtils.imprimirToast(getActivity(), "You haven't picked Image");
            }
        } catch (Exception e) {
            managerUtils.imprimirToast(getActivity(), "Something went wrong");
        }

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnAceptar:
                managerUtils.imprimirToast(getActivity(), "registrando ..");
                // ABRIMOS C�MARA PARA CAPTURAR IM�GEN
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_FOTO);

                // EJECUTAMOS ASYNTASK PARA REGISTRAR EN WEBSERVICE
//                registroAlerta.setIdTipoAlerta(1);
//                if (managerSqlite.ejecutarConsulta(1, registroAlerta, null , null) == 1)   // NULL = BAD PRACTICE
//                {
//                    managerUtils.imprimirToast(getActivity(), "SE INSERT� CORRECTAMENTE");
//                    new asynRegistroAlerta().execute();
//                }
                break;
            case R.id.img_gps:
                // EJECUTAMOS ASYNTASK PARA REGISTRAR EN WEBSERVICE
//                registroAlerta.setIdTipoAlerta(1);
//                if (managerSqlite.ejecutarConsulta(1, registroAlerta, null , null) == 1)   // NULL = BAD PRACTICE
//                {
//                    new asynRegistroAlerta().execute();
//                }
                break;
        }
    }

    @Override
    public void listenerTimer() {

    }


    /**
     * REGISTRAR ALERTA
     **/
    RegistroAlerta registroAlerta = new RegistroAlerta();
    /**
     * M�todo para probar webServices
     */
    Httppostaux post;

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
                if (managerSqlite.ejecutarConsulta(21, registroAlerta, null , null) == 1) // BAD PRACTICE
                {
                    managerUtils.imprimirToast(getActivity(), "Se actualizo");
                }
            }
            managerUtils.imprimirToast(getActivity(), result);
        }

    }
    //endregion

    String URL_connect="http://smsgps.comli.com/ws_android/ws_sms_gps/ws_registro_alerta.php";//ruta en donde estan nuestros archivos

    private boolean registroCorrecto()
    {
        int status = -1;
        ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

        postparameters2send.add(new BasicNameValuePair("NickUsuario"            ,registroAlerta.getNickUsuario()));
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




}
