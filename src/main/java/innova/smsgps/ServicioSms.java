package innova.smsgps;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

import innova.smsgps.beans.Coordenada;
import innova.smsgps.controlador.ControladorUbicacion;
import innova.smsgps.controlador.ControladorUbicacion.ControladorUbicacionCallback;
import innova.smsgps.task.UpAlerta;
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
    ManagerUtils managerUtils ;

    /**
     * Instancias bluetooth y objetos.
     */
//    protected static final String MacAddress ="98:D3:31:20:0A:F5";// "10:F9:6F:61:CD:4C"; // 98:D3:31:20:0A:F5
    protected static final String MacAddress ="";// "10:F9:6F:61:CD:4C"; // 98:D3:31:20:0A:F5
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




    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
        TimerTarea objTimer = new TimerTarea(this);
        managerUtils        = new ManagerUtils();
        mContext            = getApplicationContext();
        instanciaServicio   = this;
        IniciarLocalizacion();
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
    public static void connectDevice(String address){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
            BluetoothSocket mmSocket = device.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            escuchar();
        }
        catch (  IOException e) {
            e.printStackTrace();
        }
    }



    //region BLUETOOTH
//    public static void findBT(String MacAddress)
//    {
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if(mBluetoothAdapter == null)
//        { // return;
//        }
//
//        if(!mBluetoothAdapter.isEnabled())
//        {
//            mBluetoothAdapter.enable();
//        }
//        mmDevice = mBluetoothAdapter.getRemoteDevice(MacAddress);
//
//    }
//    public static void openBT() throws IOException
//    {
//        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
//        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
//        mmSocket.connect();
//        mmOutputStream = mmSocket.getOutputStream();
//        mmInputStream = mmSocket.getInputStream();
//    }

    static BufferedReader r = null;
    static String mSalida="";

    static void escuchar()
    {
//        try
//        {
//            findBT(MacAddress);
//            openBT();
//        }catch (Exception e)
//        {
//
//        }
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
                    //Log.i("smsgps", "1");
//                    if (r != null)
//                    {
                        r = new BufferedReader(new InputStreamReader(mmInputStream));
                        Log.i("smsgps", "2");
                        try {
                            mSalida = r.readLine().toString();
                            mHandler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (mSalida.equals("1"))
                                    {
                                        new UpAlerta(mContext);
                                    }
                                    Toast.makeText(mContext, mSalida, Toast.LENGTH_SHORT).show();
//                                    SmsActivity.Contador = 0;
                                }
                            });
                        }catch (Exception e)
                        {
                            Log.i("smsgps", e.getMessage());

                        }
//                    }


                }


            }
        }).start();
    }


    //endregion
}
