package innova.smsgps;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import innova.smsgps.beans.Coordenada;
import innova.smsgps.communication.BridgeIPC;
import innova.smsgps.communication.IncomingIPC;
import innova.smsgps.communication.IncomingIPC.IncomingIpcCallback;
import innova.smsgps.controlador.ControladorUbicacion;
import innova.smsgps.controlador.ControladorUbicacion.ControladorUbicacionCallback;
import innova.smsgps.enums.IDSP1;
import innova.smsgps.task.UpAlerta;
import innova.smsgps.utils.ManagerUtils;

/**
 * Created by USUARIO on 02/11/2015.
 */

public class ServicioSms extends IntentService implements TimerTarea.TimerTareaCallback, ControladorUbicacionCallback,
        IncomingIpcCallback{

    /**
     * Instancias Singleton
     */
    private static ServicioSms instanciaServicio = null;
    // Instancias
    private LocationManager handle;
    private ControladorUbicacion controladorUbicacion;
    static ManagerUtils managerUtils ;
    TimerTarea objTimer;
    /**
     * Instancias bluetooth y objetos.
     */
    static OutputStream mmOutputStream;
    static InputStream mmInputStream;
//    private String Ordenes[]=new String[]{"A","B","C","D"};
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

    // Obejto messenger usado para clientes que envien mensajes
    Messenger mMessenger = null;
    public static final int MESSAGE = 1;

    // INDICES IPC
    public static final int MSG_REGISTRAR_CLIENTE	= 1;
    public static final int MSG_ELIMINAR_CLIENTE	= 2;
    public static final int MSG_SET_INT_VALOR		= 3;
    public static final int MSG_SET_STRING_VALOR	= 4;
    @Override
    public void IncomingIPC(Message message)
    {
        switch (message.what)
        {
            case MSG_REGISTRAR_CLIENTE:
//                mClientes.add(message.replyTo);
                //Toast.makeText(getApplicationContext(), "Nuevo cliente conectado..", Toast.LENGTH_SHORT).show();
                break;
            case MSG_ELIMINAR_CLIENTE:
//                mClientes.remove(message.replyTo);
                break;
            case MSG_SET_STRING_VALOR:
                // COMUNICACIÓN DESDE BASE ACTIVITY - CLIENTE
                if (message.arg1 == BridgeIPC.INDICE_WEBSOCKET)
                {
                    Bundle bundle = message.getData();
                    if (bundle != null)
                    {
                        String[] data = bundle.getStringArray(BridgeIPC.NOMBRE_BUNDLE);
//                        managerUtils.imprimirToast(this, data[0]);

                        // SÓLO PARA PRUEBAS DE DESARROLLO REGISTRAREMOS ALERTAS DESDE AQUÍ
                        if (data[0].equals("1"))
                        {
                            new UpAlerta(mContext, IDSP1.TA1);
                            postStatusUpdate("Prueba integrada ... " + (new Date().toString()));
                        }

                        // ENVIAMOS MENSAJE AL SERVIDOR SIN DISTINGUIR SI ES BOLETO O ELECTRÓNICO - DESARROLLO
//                        enviarMensajeAlServidorWebSocket(data[1]);
                        //Toast.makeText(getApplicationContext(), data[1]  + " <S>\n", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case MSG_SET_INT_VALOR:
                Toast.makeText(getApplicationContext(), message.arg2 + " <I>", Toast.LENGTH_SHORT).show();
                break;
            //default:
            //IncomingIPC(message);
        }
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
        mContext            = getApplicationContext();
        instanciaServicio   = this;
        IniciarLocalizacion();
        mMessenger =  new Messenger(new IncomingIPC(this));

//        if (MacAddress.length() > 1)
    }

    @Override
    public void getCoordenada(Coordenada coordenada) {
        Toast.makeText(mContext, coordenada._getLatitud() + "|" + coordenada._getLongitud(), Toast.LENGTH_SHORT).show();
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
                                        new UpAlerta(mContext, IDSP1.TA1);
                                        postStatusUpdate("Prueba integrada ... " + (new Date().toString()));
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

    /**
     * Comprueba si la session esta activa.
     */
    static innova.smsgps.beans.Session sessionbeans = new innova.smsgps.beans.Session();


    private static void postStatusUpdate(final String message) {
        if (sessionbeans != null) {
            Request request = Request
                    .newStatusUpdateRequest(sessionbeans.getSession().getActiveSession(), message, new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            if (response.getError() != null)
                            {
                                managerUtils.imprimirToast(mContext, response.getGraphObject() + "|" + response.getError());
                            }else
                            {
                                managerUtils.imprimirToast(mContext, "Se actualizo correctamente");
//                                managerUtils.imprimirToast(mContext, response.toString());
                            }

                        }
                    });
            request.executeAsync();
        }
    }


    //endregion
}
