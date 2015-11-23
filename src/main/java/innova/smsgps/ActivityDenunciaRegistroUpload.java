package innova.smsgps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import java.util.ArrayList;
import java.util.List;

import innova.smsgps.application.Globals;
import innova.smsgps.beans.Coordenada;
import innova.smsgps.beans.RegistroDenuncias;
import innova.smsgps.enums.IDSP2;
import innova.smsgps.enums.IDUTILS;

/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityDenunciaRegistroUpload extends BaseActivity {

    ImageView imgDenuncia;
    Spinner spinner;
    EditText editDescripcion;
    String tipoDenuncia = "";
    String PATH = "";
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
            PATH = extras.getString(FragmentDenunciasRegistro.ID_PATH);
            cargarImagen();
        }
        cargarCategorias();
    }

    private void cargarCategorias()
    {
        listCategorias.add("Asalto");
        listCategorias.add("Accidente");
        listCategorias.add("Emergencia");
        ArrayAdapter<String> datosSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listCategorias);
        datosSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(datosSpinner);
    }


    private void cargarImagen()
    {
        imgDenuncia.setImageBitmap(managerUtils.getBitmap(PATH));
        editDescripcion =(EditText)findViewById(R.id.editDescripcionDenuncia);
//        imgDenuncia.setImageBitmap(RegistroDenuncias.ImgDenunciabmp);
    }

    @Override
    public void listenerTimer() {

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
//                imprimitToast("Upload..");
                if (!PATH.equals("") && editDescripcion.getText().toString().length() > 5)
                {
                    String getSpinner = spinner.getSelectedItem().toString();
                    tipoDenuncia = (getSpinner.equals(listCategorias.get(0)))? "1": (getSpinner.equals(listCategorias.get(1)))? "2" : "3";

                    RegistroDenuncias registroDenuncias = new RegistroDenuncias();
                    registroDenuncias.setDescripcion(editDescripcion.getText().toString());
                    registroDenuncias.setIdTipoDenuncia(tipoDenuncia);
                    registroDenuncias.setImgDenuncia(managerUtils.getImagenComprimida(PATH));
                    if (managerSqlite.ejecutarConsulta(2, null, registroDenuncias, null) == 1)
                    {
                        progressDialog = ProgressDialog.show(ActivityDenunciaRegistroUpload.this, "", "Cargando..", false);
                        Thread thread=new Thread(new Runnable(){
                            public void run(){
                                doFileUpload();
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
                    }
                }
                break;
        }
    }


    private void doFileUpload()
    {
        String urlString = "http://smsgps.comli.com/ws_android/ws_sms_gps/ws_upload_img.php";
        //String urlString = "http://smd407.comxa.com/ws_android/ws_sms_gps/ws_upload_img.php";
        try
        {
            //ByteArrayBody bab = new ByteArrayBody(data, name_file);
            ByteArrayBody bab = new ByteArrayBody(managerUtils.getImagenComprimida(PATH), PATH);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("uploadedfile1", bab);
            reqEntity.addPart("Lat", new StringBody(coordenada._getLatitud()));
            reqEntity.addPart("Lng", new StringBody(coordenada._getLongitud()));
            reqEntity.addPart("FechaHora", new StringBody(managerUtils.getFechaHora(IDUTILS.DDHHMMSSSS)));
            reqEntity.addPart("IdRegistroAlertasMovil", new StringBody(Globals.getInfoMovil().getSPF2(IDSP2.IDREGISTRODENUNCIAMOVIL)));
            reqEntity.addPart("Descripcion", new StringBody(editDescripcion.getText().toString()));
            reqEntity.addPart("IdTipoDenuncia", new StringBody(tipoDenuncia));
            reqEntity.addPart("IdFacebook", new StringBody(Globals.getInfoMovil().getSPF2(IDSP2.IDFACEBOOK)));
            reqEntity.addPart("ImgPath", new StringBody(PATH));

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
                            Intent intent = new Intent(ActivityDenunciaRegistroUpload.this, ManagerFragmentsSms.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        catch (Exception ex){
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
    }

}



