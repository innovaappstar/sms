package innova.smsgps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;

import innova.libraryui.SurfaceViewCustomVideo;
import innova.smsgps.communication.BridgeIPC;

public class ActivityGrabarVideo extends BaseActivity {

	TextView txtTiempo;
	private SurfaceViewCustomVideo surfaceView;

	// sentido de orientacion de pantalla para la cámara...
	private boolean isGrabandoVideo			= false;
	int minuto = 0;
	int segundo= 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_test_record_video);
		surfaceView = (SurfaceViewCustomVideo) findViewById(R.id.surface_camera);
		txtTiempo	= (TextView)findViewById(R.id.txtTiempo);
		surfaceView.setInstancias(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, 0, 0, "StartRecording");
		menu.add(0, 1, 0, "StopRecording");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		surfaceView.releaseCamera();
	}


	@Override
	public void listenerTimer()
	{

		if (isGrabandoVideo)
		{
			if (txtTiempo != null)
			{
				txtTiempo.setText("Video : " + getTiempos());
			}
		}else
		{
			resetTiempos();
		}

	}

	public void resetTiempos()
	{
		minuto = 0;
		segundo= 0;
	}
	public String getTiempos()
	{
		String tiempo = "";
		String minutoTemp = "00:";
		String segundoTemp = "";
		segundo ++;

		if (segundo >= 60)
		{
			minuto++;
			segundo = 0;
		}
		minutoTemp = minuto + ":";
		if (minuto < 10)
		{
			minutoTemp = "0" + minuto + ":";
		}
		segundoTemp = segundo + "";
		if (segundo < 10)
		{
			segundoTemp = "0" + segundo ;
		}


		tiempo = minutoTemp + segundoTemp;

		return tiempo;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		surfaceView.iniciarCamara();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		surfaceView.destruirCamara();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case 0:
				try {
					startRecording();
				} catch (Exception e) {
				}
				break;

			case 1: //GoToAllNotes
				surfaceView.stopRecording();
				//surfaceView.releaseMediaRecorder();
//				surfaceView.releaseCamera();
				break;

			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void startRecording() throws IOException
	{
		surfaceView.startRecording();
	}
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.imgButtonCameraReverse:
				if (camaraFrontal() == 0)
				{
					surfaceView.cambiarCamara();
				}else
				{
					managerUtils.imprimirToast(this, "No cuenta con otra cámara.");
				}
				//surfaceViewCustom.tomarFoto();
				break;
			case R.id.imgButtonTakePhoto:
				startActivity(new Intent(this, ActivityCameraPhoto.class));
				finish();
				break;
			case R.id.contenedorCapturePhoto:
				enviarMensajeIPC(BridgeIPC.INDICE_SELFIE_ANDROID, new String[]{"3|2", "VIDEO"});
				break;
		}
	}




	private boolean isHorizontal()
	{
		final int rotation = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
		switch (rotation)
		{
			case Surface.ROTATION_0:
				return false;
			case Surface.ROTATION_90:
				return true;
		}
		return false;
	}


	public int camaraFrontal()
	{
		return surfaceView.comprobarExistenciaCamaraFrontal();
	}

	@Override
	public void RecepcionMensaje(int activity, int tipo) {
		if (activity == 2 && tipo == 2)
		{
			try
			{
				if (!isGrabandoVideo)
				{
					startRecording();
					isGrabandoVideo = true;
				}else
				{
					surfaceView.stopRecording();
					managerUtils.imprimirToast(this, "Video guardado con éxito.");
					isGrabandoVideo = false;
				}
			} catch (Exception e) {
				isGrabandoVideo = false;
			}
		}
	}



}