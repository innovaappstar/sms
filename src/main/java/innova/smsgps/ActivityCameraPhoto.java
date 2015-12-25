package innova.smsgps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.File;

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
//        surfaceViewCustom.detenerCamara();
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
                surfaceViewCustom.eliminarFoto();
                break;
            case R.id.imgGuardarFoto:
                ocultarFotoTomada();
//                surfaceViewCustom.guardarFoto();
                //managerUtils.imprimirToast(this, "Guardando foto ...");
                break;
            case R.id.imgButtonRecordVideo:
//                surfaceViewCustom.detenerCamara();
                surfaceViewCustom.destruirCamara();
                startActivity(new Intent(this, ActivityGrabarVideo.class));
                finish();
                break;



        }
    }


    public int camaraFrontal()
    {
        return surfaceViewCustom.comprobarExistenciaCamaraFrontal();
    }

    File imgFile = null;
    @Override
    public void PhotoCapture(final byte[] data)
    {

    }


    public Bitmap getBitmap(byte[] data)
    {
        BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
        options.inPurgeable = true; // inPurgeable is used to free up memory while required
        Bitmap songImage1 = BitmapFactory.decodeByteArray(data,0, data.length,options);//Decode image, "thumbnail" is the object of image file
        Bitmap songImage = Bitmap.createScaledBitmap(songImage1, 50 , 50 , true);// convert decoded bitmap into well scalled Bitmap format.
        return songImage;
    }

    @Override
    public void PhotoGuardada(int resultado,final String filepath)
    {
        if (resultado == 0)
        {
            try
            {
                new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        imgFile = new  File(filepath);

                        mHandler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if(imgFile.exists())
                                {
                                    imgFoto.setImageURI(Uri.fromFile(imgFile));
                                    pbarCargando.setVisibility(View.GONE);
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

            //managerUtils.imprimirToast(getApplicationContext(), "Se guardó correctamente");
            //finish();
        }else
        {
            managerUtils.imprimirToast(getApplicationContext(), "Ocurrió un error al guardar la imágen.");
        }
    }

    @Override
    public void PhotoGuardando()
    {
        pbarCargando.setVisibility(View.VISIBLE);

    }

    /**
     * Simple callback que retornará un booleano
     * indicando si se elimino o no el archivo.
     * Si no se pudierá eliminar el archivo sería
     * un error inesperado, por permisos u otra razón.
     * */
    @Override
    public void PhotoEliminado(boolean eliminado)
    {
        if (!eliminado)
        {
            managerUtils.imprimirToast(this, "No se pudo eliminar el archivo.");
        }else
        {
            managerUtils.imprimirToast(this, "Cancelado");
        }
        ocultarFotoTomada();
    }

    /**
     * Método que ocultara la ventana de visualización
     * de las fotos que se toman.
     * será llamado por el callback y el boton check de fotos.
     **/
    private void ocultarFotoTomada()
    {
        contenedorSurface.setVisibility(View.VISIBLE);
        contenedorMostrarFoto.setVisibility(View.GONE);
    }

}
