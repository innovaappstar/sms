package innova.smsgps;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import innova.smsgps.beans.Coordenada;
import innova.smsgps.communication.IncomingIPC;
import innova.smsgps.controlador.ControladorUbicacion;
import innova.smsgps.infomovil.ManagerInfoMovil;
import innova.smsgps.listener.TimerTarea;
import innova.smsgps.utils.ManagerUtils;

/**
 * Created by USUARIO on 09/01/2016.
 */
public class BaseServicio extends IntentService implements TimerTarea.TimerTareaCallback, ControladorUbicacion.ControladorUbicacionCallback, IncomingIPC.IncomingIpcCallback
        {

    // INDICES IPC
    public static final int MSG_REGISTRAR_CLIENTE	= 1;
    public static final int MSG_ELIMINAR_CLIENTE	= 2;
    public static final int MSG_SET_INT_VALOR		= 3;
    public static final int MSG_SET_STRING_VALOR	= 4;

    /**
     * Instancias Singleton
     */
    private static BaseServicio instanciaServicio = null;
    // Instancias
    private LocationManager handle;
    private ControladorUbicacion controladorUbicacion;
    static ManagerUtils managerUtils ;

    public ManagerInfoMovil managerInfoMovil;

    TimerTarea objTimer;
    static Context mContext ;
    Messenger mMessenger = null;
    Ringtone ringtone;
    Camera camera = null;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    //region CICLOS DE VIDA DEL SERVICIO
    public BaseServicio()
    {
        super(BaseServicio.class.getName());
    }
    /**
     * Comprueba si la session esta activa.
     */
    static innova.smsgps.beans.Session sessionbeans = new innova.smsgps.beans.Session();

    @Override
    public void IncomingIPC(Message message) {

    }


            // LocalBinder, mBinder and onBind() allow other Activities to bind to this service.
    public class LocalBinder extends Binder {
    }

    private final LocalBinder mBinder = new LocalBinder();
    /**
     * Retornar a nuestra interfaz Messenger para enviar mensajes al servicio
     * por los clientes..
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
    @Override
    public boolean onUnbind(Intent intent)
    {
        return false;
    }


    @Override
    protected void onHandleIntent(Intent workIntent) {

    }

    /**
     * Cumple la funcion de que al destruirse el activity o el servicio se
     * autoinicie nuevamente.
     * @return START_STICKY
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }


    public static boolean isRunning() {
        return instanciaServicio != null;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        DetenerLocalizacion();
        try
        {
            objTimer = null;
            ToogleLed(true);    // APagamos led al destruir
        }catch (Exception e)
        {
        }
        super.onDestroy();
    }

    //endregion





    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
        objTimer = new TimerTarea(this);
        managerUtils        = new ManagerUtils();
        managerInfoMovil    = new ManagerInfoMovil(this);
        mContext            = getApplicationContext();
        instanciaServicio   = this;
        // RINGTONE
        Uri notification    = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone            = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        IniciarLocalizacion();
        mMessenger =  new Messenger(new IncomingIPC(this));

//        if (MacAddress.length() > 1)
    }

    @Override
    public void getCoordenada(Coordenada coordenada) {
//        Toast.makeText(mContext, coordenada._getLatitud() + "|" + coordenada._getLongitud(), Toast.LENGTH_SHORT).show();
    }


    /**
     * Simple mEEEtodo para inicializar el manejador de la
     * clase Location_Service
     */
    //region Localizaciooom
    public void IniciarLocalizacion()
    {
        if(handle == null)
        {
            handle = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            controladorUbicacion = new ControladorUbicacion(this);
            handle.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, controladorUbicacion);
        }
    }

    /**
     * Detenemos el manejador si es que es diferente de null
     */
    public void DetenerLocalizacion()
    {
        if (handle != null)
        {
            handle.removeUpdates(controladorUbicacion);
            handle = null;
        }
    }
    //endregion

    /**
     * Inicia el ringtone, pero antes detiene alguno si
     * estuviese reproduciendose..
    */
    public void IniciarRingtone()
    {
        DetenerRingtone();
        ringtone.play();
    }

    /**
     * Detiene el ringtone siempre y cuando este reproduciendose...
     */
    public void DetenerRingtone()
    {
        if (ringtone.isPlaying())
            ringtone.stop();
    }

    /**
     * Simple método para hacer un switch del
     * led de la cámara..
     * @param isApagarLed
     */
    public void ToogleLed(boolean isApagarLed)
    {
        if ( camera == null)
        {
            camera = Camera.open();
        }
        Camera.Parameters p = camera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(p);
        if (isApagarLed)
        {
            camera.stopPreview();
            camera.release();
            camera = null;
        }else
        {
            camera.startPreview();
        }
    }



    @Override
    public void TimerTareaExecute()
    {
        //Log.i("smsgps", "de Nuevo");
    }


}