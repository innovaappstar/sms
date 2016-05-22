package innova.smsgps.activities;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import innova.smsgps.R;
import innova.smsgps.base.adapters.DispositivosAdapter;
import innova.smsgps.entities.Dispositivo;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityListaDispositivos extends BaseActivity implements ListView.OnItemClickListener
{
    LinearLayout llFuncionesActionBar;
    ListView lvDispositivos ;
    boolean isMostrarListaAbs = false;
    BluetoothAdapter bluetoothAdapter;
    ArrayList<Dispositivo> alDispositivo = new ArrayList<Dispositivo>();


    //-------------------------------
    TextView tvStatusConnectionDispositivo;
    //----------------------------------
    ThreadConectarConDispositivo threadConectarConDispositivo;
    ThreadIniciarComunicacion threadIniciarComunicacion;
    private static boolean RUNING_THREAD_COMUNICACION = false;
    //-------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_title, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        setContentView(R.layout.activity_lista_dispositivos);
        //region casting vistas
        tvStatusConnectionDispositivo   = (TextView)findViewById(R.id.tvStatusConnectionDispositivo);
        llFuncionesActionBar            = (LinearLayout)findViewById(R.id.llFuncionesActionBar);
        lvDispositivos                  = (ListView)findViewById(R.id.lvDispositivos);
        lvDispositivos.setOnItemClickListener(this);
        //endregion

        this.registerReceiver(broadcastReceiverBluetooth, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        bluetoothAdapter        = BluetoothAdapter.getDefaultAdapter();
        buscarDispositivos();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (bluetoothAdapter != null)
            bluetoothAdapter.cancelDiscovery();
        if (broadcastReceiverBluetooth != null)
            unregisterReceiver(broadcastReceiverBluetooth);
        // opcional
//        if(myThreadConnectBTdevice!=null)
//            myThreadConnectBTdevice.cancel();
    }

    @Override
    public void listenerTimer()
    {
    }


    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ivActionAbs:
                managerUtils.deslizadoVertical(llFuncionesActionBar, this, isMostrarListaAbs);
                isMostrarListaAbs = !isMostrarListaAbs;
                break;
            case R.id.tvActionConectar:

                break;
            default:
                break;
        }
    }

    /**
     * limpiamos arreglo de dispositivos y buscamos nuevamente..
     */
    private void buscarDispositivos()
    {
        alDispositivo = new ArrayList<Dispositivo>();
        lvDispositivos.setAdapter(null);
        if(bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.startDiscovery();
    }

    //region receiver bluetooth
    private final BroadcastReceiver broadcastReceiverBluetooth = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction()))
            {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (alDispositivo.size() > 0)
                {
                    for (int i = 0; i < alDispositivo.size(); i++)
                        if (alDispositivo.get(i).getMacAddress().equals(bluetoothDevice.getAddress()))
                            return;
                }
                alDispositivo.add(new Dispositivo(bluetoothDevice.getAddress()));
                lvDispositivos.setAdapter(new DispositivosAdapter(alDispositivo, ActivityListaDispositivos.this));
            }
        }
    };
    //endregion

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Dispositivo dispositivo = (Dispositivo)parent.getItemAtPosition(position);
        managerUtils.imprimirToast(this, dispositivo.getMacAddress());
        // obtenemos device..
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(dispositivo.getMacAddress());

        threadConectarConDispositivo = new ThreadConectarConDispositivo(device);
        threadConectarConDispositivo.start();
    }


    /**
     * método que iniciará el thread de comunicación , luego de que
     * nos hayamos conectado con el dispositivo bt.
     * @param socket BluetoothSocket
     */
    private void iniciarThreadComunicacion(BluetoothSocket socket){

        threadIniciarComunicacion = new ThreadIniciarComunicacion(socket);
        threadIniciarComunicacion.start();
    }

    /**
     * Thread de conexión con dispositivo...
     * Primero creará un objeto bluetooth Socket ..
     * Segundo , intentará conectarse en segundo plano...
     * Tercero, comprobará si la conexión fue un éxito o no para
     * poder iniciar el thread de comunicación..
     */
    private class ThreadConectarConDispositivo extends Thread
    {
        private BluetoothDevice bluetoothDevice ;
        private BluetoothSocket bluetoothSocket = null;

        private ThreadConectarConDispositivo(BluetoothDevice bluetoothDevice)
        {
            this.bluetoothDevice = bluetoothDevice;
            try
            {
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(Dispositivo.getUUID()); // creando bluetooth socket
            }catch (Exception e) {}
        }

        @Override
        public void run()
        {
            boolean success = false;
            try
            {
                bluetoothSocket.connect();
                success = true;
            }catch (IOException e)
            {
                final String eMessage = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // salió algo mal en la conexión... eMessage
                    }
                });

                try
                {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if(success)
            {
                final String msgconnected = "connect successful:\n" + "BluetoothSocket: " + bluetoothSocket + "\n" + "BluetoothDevice: " + bluetoothDevice;

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        tvStatusConnectionDispositivo.setText(msgconnected);
                        // conexión exitosa... msgconnected
                    }});
                RUNING_THREAD_COMUNICACION = true;
                iniciarThreadComunicacion(bluetoothSocket);
            }else
            {
                // error
            }
        }

        public void cancel()
        {
            try
            {
                bluetoothSocket.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }


    /**
     * Thread de comunicación con dispositivo bt. despues de conectarnos..
     * Primero, Seteamos el bluetoothSocket en el constructor y enseguida obtenemos los valores de entrada y salida del bluetoothSocket.. (I/O)
     * Segundo, Definimos un array byte[] para que contenga los datos recibidos que se leeran del inputStream en un segundo plano mientas RUNING_THREAD_CONNECTED sea true..
     * Tercero, Si la conexión se pierde, entonces resetearemos los thread's (connection , comunication)
     */
    private class ThreadIniciarComunicacion extends Thread
    {
        private BluetoothSocket bluetoothSocket;
        private InputStream inputStream = null;
        private OutputStream outputStream = null;

        public ThreadIniciarComunicacion(BluetoothSocket bluetoothSocket)
        {
            this.bluetoothSocket = bluetoothSocket;

            try // obtenemos objetos de entrada y salida..
            {
                inputStream     = this.bluetoothSocket.getInputStream();
                outputStream    = this.bluetoothSocket.getOutputStream();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void run()
        {
            byte[] buffer = new byte[1024];
            int bytes;

            while (RUNING_THREAD_COMUNICACION) // boolean constante - flag
            {
                try
                {
                    bytes = inputStream.read(buffer);
                    String dataRecibida = new String(buffer, 0, bytes);
                    final String message = String.valueOf(bytes) + " bytes received:\n" + dataRecibida;

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // message recibido.. message
                            managerUtils.imprimirToast(ActivityListaDispositivos.this, message);
                        }
                    });

                } catch (IOException e)
                {
                    final String msgConnectionLost = "Connection perdida:\n" + e.getMessage();
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // conexión perdida... msgConnectionLost
                        }
                    });
                    resetThread();  // reset
                }
            }
        }

        public void write(byte[] buffer)
        {
            try
            {
                outputStream.write(buffer);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        public void cancel()
        {
            try
            {
                bluetoothSocket.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }


    }

    /**
     * Simple método que reseteará los thread y detendra el de la
     * comunicación a la primera excepción..
     */
    private void resetThread()
    {
        threadConectarConDispositivo    = null;
        threadIniciarComunicacion       = null;
        RUNING_THREAD_COMUNICACION         = false;
    }


}



