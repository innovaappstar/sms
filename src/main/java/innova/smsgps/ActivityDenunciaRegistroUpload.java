package innova.smsgps;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import innova.smsgps.ActivityConfigBeeps.IndicesBeep;
import innova.smsgps.application.Globals;
import innova.smsgps.beans.Coordenada;
import innova.smsgps.beans.RegistroDenuncias;
import innova.smsgps.communication.BridgeIPC;
import innova.smsgps.constantes.CONSTANT;
import innova.smsgps.enums.IDSP2;
import innova.smsgps.enums.IDUTILS;

/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityDenunciaRegistroUpload extends BaseActivity {

    ImageView imgDenuncia;
    Spinner spinner;
    EditText editDescripcion;
    String PATH = "";
    boolean isExistenCoordenadas = false;
    List<String> listCategorias = new ArrayList<String>();
    HttpEntity resEntity;
    Coordenada coordenada = new Coordenada();
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_denuncia_registro_upload_wzp);
        imgDenuncia = (ImageView)findViewById(R.id.imgDenuncia);
        spinner     = (Spinner)findViewById(R.id.spinnerCategorias);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            PATH = extras.getString(ActivityCameraDenuncia.ID_PATH);
            cargarImagen(PATH);
        }else
        {
            managerUtils.imprimirToast(this, "NO SE ENCONTRÓ IMÁGEN");
            finish();   // ERROR , NO SE RECIBIO NINGUNA IMÁGEN
        }
        cargarCategorias();
    }

    private void cargarCategorias()
    {
        listCategorias.add(IndicesBeep.BEEP1.descripcion);
        listCategorias.add(IndicesBeep.BEEP2.descripcion);
        listCategorias.add(IndicesBeep.BEEP3.descripcion);
        ArrayAdapter<String> datosSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCategorias);
        datosSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(datosSpinner);

    }


    private void cargarImagen(String path)
    {
        imageLoader.displayImage(path, imgDenuncia);
        editDescripcion =(EditText)findViewById(R.id.editDescripcionDenuncia);
    }

    public int ContadorLocalizacion = 0;
    @Override
    public void listenerTimer() {

        if (ContadorLocalizacion == 2)
        {
            enviarMensajeIPC(BridgeIPC.INDICE_DENUNCIA_ANDROID, new String[]{"7|1", "DENUNCIA"});
//            managerUtils.imprimirToast(this, "Obteniendo localización...");
        }
        ContadorLocalizacion ++;
    }
    /**
     * Devolverá el código del beep en tipo de dato Int
     * @param spinner View
     * @return int
     */
    public int getCodigoBeep(Spinner spinner)
    {
        IndicesBeep indicesBeep = IndicesBeep.SINVALOR;
        return indicesBeep.getCodigo(spinner.getSelectedItem().toString());
    }
    /**
     * Evento que se ejecutará al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnAceptar:
                if ( ! isExistenCoordenadas )
                {
                    managerUtils.imprimirToast(this, "No hay coordenadas gps..");
                    enviarMensajeIPC(BridgeIPC.INDICE_DENUNCIA_ANDROID, new String[]{"7|1", "DENUNCIA"});
                    return;
                }
//                managerUtils.imprimirToast(this, getCodigoBeep(spinner) + " << Código beep");

//                imprimitToast("Upload..");
                if (!PATH.equals("") && editDescripcion.getText().toString().length() > 5)
                {
                    RegistroDenuncias registroDenuncias = new RegistroDenuncias();
                    registroDenuncias.setDescripcion(editDescripcion.getText().toString());
                    registroDenuncias.setIdTipoDenuncia(getCodigoBeep(spinner) + "");       // 1 -2 -3
                    registroDenuncias.setImgDenuncia(PATH);
                    if (managerSqlite.ejecutarConsulta(2, null, registroDenuncias, null) == 1)
                    {
                        progressDialog = ProgressDialog.show(ActivityDenunciaRegistroUpload.this, "", "Cargando..", false);
                        Thread thread=new Thread(new Runnable(){
                            public void run()
                            {
                                postDenuncia();
                                runOnUiThread(new Runnable(){
                                    public void run()
                                    {
                                        if(progressDialog.isShowing())
                                            progressDialog.dismiss();
                                    }
                                });
                            }
                        });
                        thread.start();
                    }else
                    {
                        managerUtils.imprimirToast(this, "Ocurrió un error al ejecutar el proc SQLite");
                    }
                }
                break;
        }
    }

    /**
     * Función que devuelve objeto byte comprimido de una imágen...
     **/
    public byte[] getImagenComprimida(Bitmap bitmap)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Bitmap bm = bitmap;
        bm.compress(Bitmap.CompressFormat.JPEG, 75, bos); // Compresion
        byte[] data = bos.toByteArray();
        return data;
    }
    private void postDenuncia()
    {
//        String urlString = "http://smsgps.comli.com/ws_android/ws_sms_gps/ws_upload_img.php";
        String URL = CONSTANT.PATH_WS + CONSTANT.WS_REGISTRO_DENUNCIA;
        //String urlString = "http://smd407.comxa.com/ws_android/ws_sms_gps/ws_upload_img.php";
        try
        {
            //ByteArrayBody bab = new ByteArrayBody(data, name_file);
//            ByteArrayBody bab = new ByteArrayBody(managerUtils.getImagenComprimida(PATH), PATH);
            String fileNameSegments[]   = PATH.split("/");
            String nombreArchivo        = fileNameSegments[fileNameSegments.length - 1];
            ByteArrayBody bab = new ByteArrayBody(getImagenComprimida(imageLoader.loadImageSync(PATH)), nombreArchivo);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(URL);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("uploadedfile1"           , bab);
            reqEntity.addPart("Lat"                     , new StringBody(coordenada._getLatitud()));
            reqEntity.addPart("Lng"                     , new StringBody(coordenada._getLongitud()));
            reqEntity.addPart("FechaHora"               , new StringBody(managerUtils.getFechaHora(IDUTILS.DDHHMMSSSS)));
            reqEntity.addPart("IdRegistroAlertasMovil"  , new StringBody(Globals.getInfoMovil().getSPF2(IDSP2.IDREGISTRODENUNCIAMOVIL))); // # REGISTRO SQLITE
            reqEntity.addPart("Descripcion"             , new StringBody(editDescripcion.getText().toString()));
            reqEntity.addPart("IdTipoDenuncia"          , new StringBody(getCodigoBeep(spinner) + ""));
            reqEntity.addPart("IdFacebook"              , new StringBody(Globals.getInfoMovil().getSPF2(IDSP2.IDFACEBOOK)));
            reqEntity.addPart("ImgPath"                 , new StringBody(nombreArchivo));

            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();
            final String response_str = EntityUtils.toString(resEntity);
            if (resEntity != null) {
                Log.i("RESPONSE", response_str);
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        try {
                            JSONArray jdata = new JSONArray(response_str);
                            JSONObject jsonData =   jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                            String result = "";
                            if (jsonData.getInt("status") == 1)
                            {
                                result = "Subida de archivo exitosa.";
                                // ACTUALIZAMOS FLAG DE TBDENUNCIAS
                                managerSqlite.ejecutarConsulta(23 , null, null, null);
                            }else
                            {
                                result = "Ocurrio un error al subir el archivo.";
                            }
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                            finish();

                        } catch (JSONException e)
                        {
                            managerUtils.imprimirToast(getApplicationContext(), e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        catch (Exception ex){
            managerUtils.imprimirToast(this, ex.getMessage());
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void RecepcionCoordenadas(int activity, Coordenada coordenada)
    {
        if (activity == 3)
        {
            isExistenCoordenadas = true;
//            managerUtils.imprimirToast(this, "Se recibieron coordenadas : " + coordenada._getLatitud());
        }
    }

}



