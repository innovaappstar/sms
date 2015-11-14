package innova.smsgps;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

import innova.smsgps.application.Globals;
import innova.smsgps.beans.Coordenada;
import innova.smsgps.controlador.ControladorUbicacion;
import innova.smsgps.controlador.ControladorUbicacion.ControladorUbicacionCallback;
import innova.smsgps.enums.ACTIONS;
import innova.smsgps.utils.ManagerUtils;

/**
 * Created by USUARIO on 02/11/2015.
 */

public class ServicioSms extends IntentService implements TimerTarea.TimerTareaCallback, ControladorUbicacionCallback{

    /**
     * Instancias Singleton
     */
    private static ServicioSms instanciaServicio = null;
    // Instancias
    private LocationManager handle;
    private ControladorUbicacion controladorUbicacion;
    static Coordenada coordenada = new Coordenada();
    ManagerUtils managerUtils ;

    /**
     * Instancias bluetooth y objetos.
     */
    protected static final String MacAddress ="98:D3:31:20:0A:F5";// "10:F9:6F:61:CD:4C"; // 98:D3:31:20:0A:F5
    static BluetoothAdapter mBluetoothAdapter;
    static BluetoothSocket mmSocket;
    static BluetoothDevice mmDevice;
    static OutputStream mmOutputStream;
    static InputStream mmInputStream;
    private String Ordenes[]=new String[]{"A","B","C","D"};
    static Context mContext ;
    /** Manejador de recuperacion de datos del TAG*/
    static Handler mHandler = new Handler();


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    //region CICLOS DE VIDA DEL SERVICIO
    public ServicioSms()
    {
        super(ServicioSms.class.getName());
    }

    // LocalBinder, mBinder and onBind() allow other Activities to bind to this service.
    public class LocalBinder extends Binder {
    }

    private final LocalBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
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
        super.onDestroy();
    }

    //endregion

    // AGREGA LA FUNCION DE POSTEAR ---ME COMI UNA TILDE
    /**Enumrable para las acciones*/
    private static ACTIONS pendingAction = ACTIONS.NONE;
    private static final String PERMISOS = "publish_actions";
    //static innova.smsgps.beans.Session sessionbeans = new innova.smsgps.beans.Session();



    /**
     * Comprueba si la session esta activa.
     */
    private static void performPublish(ACTIONS action, boolean allowNoSession) {
        Globals.getSession();
        if (Globals.getSession() != null) {
            pendingAction = action;
            if (hasPublishPermission()) {
                // We can do the action right away.
                handlePendingAction();
                return;
            } else if (Globals.getSession().isOpened()) {
                // We need to get new permissions, then complete the action when we get called back.
                //session.requestNewPublishPermissions(new Session.NewPermissionsRequest(mContext, PERMISOS));
                //request.setPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_work_history"));
                Toast.makeText(mContext, "session is opened", Toast.LENGTH_SHORT).show();

                return;
            }
        }

            pendingAction = action;
            handlePendingAction();
    }
    /**Manejado para acciones pendientes.*/

    @SuppressWarnings("incomplete-switch")
    private static void handlePendingAction() {
        ACTIONS previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = ACTIONS.NONE;

        switch (previouslyPendingAction) {
            case POST_PHOTO:
                //postPhoto();
                break;
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }
    /**M?todo post status*/
    private static void postStatusUpdate() {

            Bundle params = new Bundle();
            params.putString("caption", coordenada._getLatitud() + "|" + coordenada._getLongitud());
            params.putString("message", "Alerta SMS GPS.");
            params.putString("link", "https://www.youtube.com/watch?v=1uQG9yc-raQ");
            params.putString("picture", "http://www.nvtl.com/files/8713/6210/9612/map_gps_NVTL.jpg");
            //params.putString("picture", "https://www.google.com.pe/maps/place/Independencia/@-11.9912498,-77.0621203,14.5z/data=!4m2!3m1!1s0x9105cfaeef4c292f:0xee8dfbf42a8ee7da?hl=es-419");

            Request request = new Request(Globals.getSession().getActiveSession(), "me/feed", params, HttpMethod.POST);
            request.setCallback(new Request.Callback() {
                @Override
                public void onCompleted(Response response) {
                    if (response.getError() == null) {
                        // Tell the user success!
                    } else if (response.getError().getErrorCode() == 2500)    // session null
                    {
                        Toast.makeText(mContext, response.getError().toString(), Toast.LENGTH_SHORT).show();
                        //managerUtils.showNotificacionSimple();
                    }
                }
            });
            request.executeAsync();

    }

    public static void PostearSegundoPlano()
    {
        performPublish(ACTIONS.POST_STATUS_UPDATE, false);
        Toast.makeText(mContext, "POST STATUS", Toast.LENGTH_SHORT).show();

    }
    /**Comprobamos si tenemos permiso de publicar */
    private static boolean hasPublishPermission() {
        return Globals.getSession() != null && Globals.getSession().getPermissions().contains(PERMISOS);
    }




    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
        TimerTarea objTimer = new TimerTarea(this);
        managerUtils    = new ManagerUtils();
        mContext = getApplicationContext();
        instanciaServicio = this;
        IniciarLocalizacion();
    }

    @Override
    public void getCoordenada(Coordenada coordenada) {
        //Toast.makeText(mContext, coordenada._getLatitud() + "|" + coordenada._getLongitud(), Toast.LENGTH_SHORT).show();
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
            handle.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, controladorUbicacion);
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



    @Override
    public void TimerTareaExecute()
    {
        //Log.i("smsgps", "de Nuevo");
    }


    /**
     * Funciones Bluetooth
     * findBT --> Habilita Bluetooth
     * openBT --> Busca Dispositivos y se conecta a ello
     */

    /************* Funciones Bluetooth ***************/

    //region BLUETOOTH
    public static void findBT(String MacAddress)
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        { // return;
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.enable();
        }
        mmDevice = mBluetoothAdapter.getRemoteDevice(MacAddress);

    }
    public static void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
    }

    static BufferedReader r = null;
    static String mSalida="";

    static void escuchar()
    {
        try
        {
            findBT(MacAddress);
            openBT();
        }catch (Exception e)
        {

        }
        /**
         *  Correr funcin de lectura con las claves obtenidas
         **/
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Get key map from glob. variable. 'SparseArray<String[]>'
                /** Obtendremos el valor de los bloques*/
                while (true)
                {
                    //mmOutputStream.write(params[0].getBytes());
                    Log.i("smsgps", "1");
                    r = new BufferedReader(new InputStreamReader(mmInputStream));
                    Log.i("smsgps", "2");
                    try {
                        mSalida = r.readLine().toString();
                        mHandler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(mContext, mSalida, Toast.LENGTH_SHORT).show();
                                SmsActivity.Contador = 0;
                            }
                        });
                    }catch (Exception e)
                    {
                        Log.i("smsgps", e.getMessage());

                    }

                }


            }
        }).start();
    }

    static class AsynTaskOrden extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params)  {

            BufferedReader r = null;
            String mSalida="";
            try {
                mmOutputStream.write(params[0].getBytes());
                Log.i("smsgps", "1");
                r = new BufferedReader(new InputStreamReader(mmInputStream));
                Log.i("smsgps", "2");
                mSalida = r.readLine().toString();
                Log.i("smsgps", "3");
    	   		/*
    	   		while ((mSalida = r.readLine()) != null) {
    	   			Log.i("Socket Bluetooth ", "Respuesta Bluetoth "+r.readLine());
                }
    	   		*/
            }
            catch (Exception e) {
                Log.i("smsgps ", "Error "+e);
            }
            return mSalida;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals(""))
            {
                try {
                    findBT(MacAddress);
                    openBT();
                } catch (Exception e) {}
            }
            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            SmsActivity.Contador = 0;
            super.onPostExecute(result);
        }

    }
    //endregion
}
