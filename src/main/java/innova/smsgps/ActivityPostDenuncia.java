package innova.smsgps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
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

import innova.smsgps.beans.RegistroDenuncias;

/**
 * Created by USUARIO on 01/11/2015.
 */
public class ActivityPostDenuncia extends BaseActivity
{


    private static final int SELECT_FILE1   = 1;
    private static final int SELECT_FILE2   = 2;
    private static final int SHOW_LIST      = 3;
    String selectedPath1 = "NONE";
    String selectedPath2 = "NONE";
    HttpEntity resEntity;

    private ImageView imageView;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_post_denuncia);
        imageView = (ImageView) findViewById(R.id.imgView);

    }


    private byte[] getImagenComprimida(String path)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Bitmap bm = BitmapFactory.decodeFile(path);
        bm.compress(Bitmap.CompressFormat.JPEG,75, bos); // Compresion
        byte[] data = bos.toByteArray();
        return data;
    }

    public void onClickConfig(View view)
    {
        switch (view.getId())
        {
            case R.id.buttonPhoto:
                openGallery(SELECT_FILE1);
                break;
            case R.id.buttonLoadPicture:
                //openGallery(SELECT_FILE2);
                if(!(selectedPath1.trim().equalsIgnoreCase("NONE")))
                {
                    /*
                    RegistroDenuncias registroDenuncias = new RegistroDenuncias();
                    registroDenuncias.setDescripcion("Intento de robo en pro");
                    registroDenuncias.setIdTipoDenuncia("1");
                    registroDenuncias.setImgDenuncia(getImagenComprimida(selectedPath1));
                    if (managerSqlite.ejecutarConsulta(2, null, registroDenuncias) == 1)
                    {
                        managerUtils.imprimirToast(this, "Se insertó cortrectamente en la tbDenuncias :) ");
                    }
                    */

                    if( managerSqlite.ejecutarConsulta(63, null, null) == 1)
                    {
                        Intent intent=new Intent(ActivityPostDenuncia.this, ActivityListaRegistroDenuncias.class);
                        startActivityForResult(intent, SHOW_LIST);
                    }
                }
                break;
            case R.id.btnSubir:


                //if(!(selectedPath1.trim().equalsIgnoreCase("NONE")) && !(selectedPath2.trim().equalsIgnoreCase("NONE"))){
                    if(!(selectedPath1.trim().equalsIgnoreCase("NONE")))
                    {

                        RegistroDenuncias registroDenuncias = new RegistroDenuncias();
                        registroDenuncias.setDescripcion("Intento de robo en pro");
                        registroDenuncias.setIdTipoDenuncia("1");
                        registroDenuncias.setImgDenuncia(getImagenComprimida(selectedPath1));
                        if (managerSqlite.ejecutarConsulta(2, null, registroDenuncias) == 1)
                        {
                            managerUtils.imprimirToast(this, "Se insertó cortrectamente en la tbDenuncias :) ");
                        }

                        /*
                        progressDialog = ProgressDialog.show(ActivityPostDenuncia.this, "", "Uploading files to server.....", false);
                    Thread thread=new Thread(new Runnable(){
                        public void run(){
                            doFileUpload();
                            runOnUiThread(new Runnable(){
                                public void run() {
                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();
                                }
                            });
                        }
                    });
                    thread.start();
                        */
                }
                break;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (requestCode == SELECT_FILE1)
            {
                selectedPath1 = getPath(selectedImageUri);
                System.out.println("selectedPath1 : " + selectedPath1);

            }
            if (requestCode == SELECT_FILE2)
            {
                selectedPath2 = getPath(selectedImageUri);
                System.out.println("selectedPath2 : " + selectedPath2);
            }
            managerUtils.imprimirToast(this, "Selected File paths : " + selectedPath1 + "," + selectedPath2);
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void doFileUpload()
    {
        String urlString = "http://smsgps.comli.com/ws_android/ws_sms_gps/ws_upload_img.php";
        //String urlString = "http://smd407.comxa.com/ws_android/ws_sms_gps/ws_upload_img.php";
        try
        {
            //ByteArrayBody bab = new ByteArrayBody(data, name_file);
            ByteArrayBody bab = new ByteArrayBody(getImagenComprimida(selectedPath1), "HOLACOMOESTAS75.JPG");

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("uploadedfile1", bab);
            reqEntity.addPart("Lat", new StringBody("Lat"));
            reqEntity.addPart("Lng", new StringBody("Lng"));
            reqEntity.addPart("FechaHora", new StringBody("FechaHora"));
            reqEntity.addPart("IdRegistroAlertasMovil", new StringBody("IdRegistroAlertasMovil"));
            reqEntity.addPart("Descripcion", new StringBody("Descripcion"));
            reqEntity.addPart("IdTipoDenuncia", new StringBody("IdTipoDenuncia"));
            reqEntity.addPart("IdFacebook", new StringBody("IdFacebook"));
            reqEntity.addPart("ImgPath", new StringBody("ImgPath"));

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
                            }else
                            {
                                result = "Ocurrio un error al subir el archivo.";
                            }
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
    public void openGallery(int req_code){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, req_code);
    }


    @Override
    public void listenerTimer()
    {

    }




    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
