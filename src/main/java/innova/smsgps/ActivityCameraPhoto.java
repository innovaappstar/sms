package innova.smsgps;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import innova.libraryui.SurfaceViewCustom;
import innova.libraryui.SurfaceViewCustom.PhotoCallback;

/**
 * Created by USUARIO on 19/12/2015.
 */
public class ActivityCameraPhoto  extends BaseActivity implements PhotoCallback
{
    SurfaceViewCustom surfaceViewCustom;
    ImageView imgFoto ;
    RelativeLayout contenedorMostrarFoto;
    RelativeLayout contenedorSurface;
    ProgressBar pbarCargando;
    private Handler mHandler = new Handler();
    Bitmap bitmap   = null;


    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera_apolo);

        surfaceViewCustom       = (SurfaceViewCustom)findViewById(R.id.surfaceView);
        imgFoto                 = (ImageView)findViewById(R.id.imgFotoCapturada);
        contenedorMostrarFoto   = (RelativeLayout)findViewById(R.id.contenedorMostrarFoto);
        contenedorSurface       = (RelativeLayout)findViewById(R.id.contenedorSurface);
        pbarCargando            = (ProgressBar)findViewById(R.id.pbarCargando);
        surfaceViewCustom.setInstancias(this);
        surfaceViewCustom.photoCallback(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        surfaceViewCustom.detenerCamara();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        surfaceViewCustom.destruirCamara();
    }

    @Override
    public void listenerTimer() {

    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.contenedorCapturePhoto:
//                managerUtils.imprimirToast(this, "Capturando foto - Prueba...");
                surfaceViewCustom.tomarFoto();
//                capture();
                break;
            case R.id.imgButtonCameraReverse:
                if (camaraFrontal() == 0)
                {
                    surfaceViewCustom.cambiarCamara();
                }else
                {
                    managerUtils.imprimirToast(this, "No cuenta con otra cámara.");
                }
                //surfaceViewCustom.tomarFoto();
                break;
            case R.id.imgCancelarFoto:
                contenedorSurface.setVisibility(View.VISIBLE);
                contenedorMostrarFoto.setVisibility(View.GONE);
                break;
            case R.id.imgGuardarFoto:
                surfaceViewCustom.guardarFoto();
                //managerUtils.imprimirToast(this, "Guardando foto ...");
                break;


        }
    }


    public int camaraFrontal()
    {
        return surfaceViewCustom.comprobarExistenciaCamaraFrontal();
    }


    @Override
    public void PhotoCapture(final byte[] data)
    {
        try
        {
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                    mHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (bitmap != null)
                            {
                                pbarCargando.setVisibility(View.GONE);
                                imgFoto.setImageBitmap(bitmap);
                                contenedorMostrarFoto.setVisibility(View.VISIBLE);
                                contenedorSurface.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }).start();
        }catch (Exception e)
        {

        }

    }

    @Override
    public void PhotoGuardada(int resultado)
    {
        if (resultado == 0)
        {
            managerUtils.imprimirToast(getApplicationContext(), "Se guardó correctamente");
            finish();
        }else
        {
            managerUtils.imprimirToast(getApplicationContext(), "Ocurrió un error al guardar la imágen.");
        }
    }

    @Override
    public void PhotoConvert()
    {
        pbarCargando.setVisibility(View.VISIBLE);
    }
}
