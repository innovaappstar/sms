package innova.smsgps;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.telephony.TelephonyManager;
import android.util.SparseArray;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import innova.smsgps.application.Globals;
import innova.smsgps.beans.Coordenada;
import innova.smsgps.communication.IncomingIPC;
import innova.smsgps.controlador.ControladorUbicacion;
import innova.smsgps.enums.IDSP2;
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
     * Sparse Array que contendra la posición y el nombre
     * de las canciones..
     */
    SparseArray<String> sparseArrayCanciones = new SparseArray<String>();
    int ContadorCancion = 0;
    int TotalCanciones  = 0;
    MediaPlayer mediaPlayer;
    boolean isMedia     = false;

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
        instanciarMedia();
//        if (MacAddress.length() > 1)
    }
    private void instanciarMedia()
    {
        mediaPlayer = new  MediaPlayer();
        isMedia = true;
    }
    private void destruirMedia()
    {
        mediaPlayer.release();
        mediaPlayer     = null;
        isMedia         = false;
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

    /**
     * Simple método para hacer un switch del
     * led de la cámara..
     * @param isPause
     */
    public int IniciarMusica(boolean isPause) {

        if (validarExistenciaArchivos() != 0)
            return -1;
        // AQUI YA SUPONEMOS QUE SE CARGARON LAS CANCIONES CORRECTAMENTE .. PROCEDEMOS A REPRODUCIR/PAUSAR
        reproducirMusica(ContadorCancion, isPause);
        return 0;
    }

    /**
     * Esta función validará si el sparse array ya contiene las canciones
     * ó realizará su primera carga si aún no lo ha hecho..
     * @return 0 OK , -1 ocurrió un error..
     */
    private int validarExistenciaArchivos()
    {
        TotalCanciones = getTotalCanciones();

        if (TotalCanciones < 1)
        {
            if (cargarMusica() != 0)
                return -1;  // No se pudieron cargar las canciones..
        }
        return 0;
    }


    public int CambiarCancion(boolean isNext)
    {
        if (validarExistenciaArchivos() != 0)
            return -1;

        if (isNext && ContadorCancion < getTotalCanciones())
        {
            ContadorCancion++;
            // Mandamos a reproducir las canciones.. previa validación
            reproducirMusica(ContadorCancion, false);
        }
        else if (!isNext && ContadorCancion >= 1)
        {
            ContadorCancion--;
            // Mandamos a reproducir las canciones.. previa validación
            reproducirMusica(ContadorCancion, false);
        }

        return 0;
    }

    private void reproducirMusica(int position, boolean isPause)
    {
        System.gc();
        try
        {
            if (mediaPlayer != null && isMedia && isPause)
            {
                destruirMedia();
                return; // SETEAMOS NULL Y DETENEMOS MÉTODO
            }

            if (mediaPlayer == null && !isMedia)
                instanciarMedia();

            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.reset();
            }
            String filePath = getPathDirectory() + sparseArrayCanciones.get(position);
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    destruirMedia();
                }
            });
            managerUtils.mostrarNotificacionCustomizada(this, sparseArrayCanciones.get(position).substring(0, sparseArrayCanciones.get(position).length() - 4));

        }
        catch (Exception e) {}
    }

    private String getPathDirectory()
    {
//        String filePath = Environment.getExternalStorageDirectory()+"/" + getNombreDirectorio() + "/DePlasticoVerde  Adivinanzas  [ VideoClip Oficial ].mp3";
        return Environment.getExternalStorageDirectory() + "/" + Globals.getInfoMovil().getSPF2(IDSP2.DIRECTORIOMUSIC) + "/";
    }
    private int cargarMusica()
    {
        sparseArrayCanciones = managerUtils.getCanciones();
        TotalCanciones = getTotalCanciones();
        if (TotalCanciones < 1)
            return -1;
        return 0;
    }

    private int getTotalCanciones()
    {
        return sparseArrayCanciones.size() -1;
    }



    int contador =0;
    @Override
    public void TimerTareaExecute()
    {
        //Log.i("smsgps", "de Nuevo");
//        if (contador == 20)
//        {
//            Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
//            buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
//            sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
//            managerUtils.imprimirToast(this, "UP");
////            contador = 0;
//        }

        contador ++;
    }

    /**
     * Simple función para aceptar llamada..
     */
    public void aceptarLLamada()
    {
        Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
        buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
        sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
    }


    /**
     * Simple método para colgar una llamada...
     */
    public void colgarLLamada()
    {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Class clazz = null;
        try {
            clazz = Class.forName(telephonyManager.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method method = null;
        try {
            method = clazz.getDeclaredMethod("getITelephony");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        ITelephony telephonyService = null;
        try {
            telephonyService = (ITelephony) method.invoke(telephonyManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        telephonyService.endCall();
    }

    /**
     * Simple función para intercambiar parlante / audífono
     * @param isAltavoz boolean
     */
    public void toogleAltavoz (boolean isAltavoz)
    {
        AudioManager audioManager =  (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setSpeakerphoneOn(isAltavoz);
    }

    /** Open another app.
     * @param context current Context, like Activity, App, or Service
     * @return true if likely successful, false if unsuccessful
     */
    public static void openApp(Context context)
    {

//        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", 37.827500, -122.481670, "Where the party is at");
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}