package innova.smsgps.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

public class UpFotoComprimida extends AsyncTask<Void, Void, String> {
	String NombreArchivo="";
	Bitmap bm;
	Context context; 
	String estado="";
	public UpFotoComprimida(Bitmap bm, String NombreArchivo, Context context)
	{
		this.bm=bm;
		this.NombreArchivo=NombreArchivo;
		this.context=context; 
	}
	ProgressDialog pDialog; 
 

	protected void onPreExecute() { 
		super.onPreExecute();
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Subiendo la imagen, espere.");
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setCancelable(true);
		//pDialog.show();
		
	}

	protected void onPostExecute(String result) {
		//pDialog.dismiss(); 
		Integer i = Integer.parseInt(result.replaceAll("[^0-9.]",""));
		//result= result.substring (0,1);
		super.onPostExecute(result);
		// Cuando haya subido la img ... enviar el mensaje al otro usuario
		// url mensaje -- tipo sms --
		//pDialog.dismiss();
	}

	@Override
	protected String doInBackground(Void... params) {
		 
		// miFoto = params[0];
      try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(CompressFormat.JPEG,75, bos); // Compresion
            byte[] data = bos.toByteArray();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://smsgps.comli.com/ws_android/ws_sms_gps/ws_upload_img.php");
	                //    "http://innovaappstar.com/innova_apps/WS_Chat/ws_upload_final.php"); 
            
            //ByteArrayBody bab = new ByteArrayBody(data, name_file);
            ByteArrayBody bab = new ByteArrayBody(data, NombreArchivo);
            // File file= new File("/mnt/sdcard/forest.png");
            // FileBody bin = new FileBody(file);
            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("image", bab);
            //reqEntity.addPart("filename", new StringBody("imgeee.jpg"));
            reqEntity.addPart("datoextra", new StringBody("androids"));
            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();
 
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);  
            } 
            estado=s.toString();
        } catch (Exception e) {
            // handle exception here
            Log.e(e.getClass().getName(), e.getMessage());
        }
      return estado; 
	} 
 

}