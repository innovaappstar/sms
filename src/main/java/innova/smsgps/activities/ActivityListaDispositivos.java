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
import java.util.UUID;

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
    ThreadConnectBTdevice myThreadConnectBTdevice;
    private UUID myUUID;
    private final String UUID_STRING_WELL_KNOWN_SPP = "00001101-0000-1000-8000-00805F9B34FB";
    ThreadConnected myThreadConnected;
    private static boolean RUNING_THREAD_CONNECTED = true;

    TextView tvStatusConnectionDispositivo;
    //----------------------------------

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
                buscarDispositivos();
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
        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP); //Standard SerialPortService ID

        myThreadConnectBTdevice = new ThreadConnectBTdevice(device);
        myThreadConnectBTdevice.start();
    }


    //Called in ThreadConnectBTdevice once connect successed
    //to start ThreadConnected
    private void startThreadConnected(BluetoothSocket socket){

        myThreadConnected = new ThreadConnected(socket);
        myThreadConnected.start();
    }


    /*
       ThreadConnectBTdevice:
       Background Thread to handle BlueTooth connecting
       */
    private class ThreadConnectBTdevice extends Thread {

        private BluetoothSocket bluetoothSocket = null;
        private final BluetoothDevice bluetoothDevice;


        private ThreadConnectBTdevice(BluetoothDevice device) {
            bluetoothDevice = device;
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
                tvStatusConnectionDispositivo.setText("bluetoothSocket: \n" + bluetoothSocket);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            boolean success = false;
            try {
                bluetoothSocket.connect();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();

                final String eMessage = e.getMessage();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        tvStatusConnectionDispositivo.setText("something wrong bluetoothSocket.connect(): \n" + eMessage);
                    }
                });

                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if(success){
                //connect successful
                final String msgconnected = "connect successful:\n"
                        + "BluetoothSocket: " + bluetoothSocket + "\n"
                        + "BluetoothDevice: " + bluetoothDevice;

                runOnUiThread(new Runnable(){

                    @Override
                    public void run()
                    {
                        tvStatusConnectionDispositivo.setText(msgconnected);

//                        listViewPairedDevice.setVisibility(View.GONE);
//                        inputPane.setVisibility(View.VISIBLE);
                    }});
                RUNING_THREAD_CONNECTED = true;
                startThreadConnected(bluetoothSocket);
            }else{
                //fail
            }
        }

        public void cancel() {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(),
//                            "close bluetoothSocket",
//                            Toast.LENGTH_LONG).show();
//                }
//            });


            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    /*
   ThreadConnected:
   Background Thread to handle Bluetooth data communication
   after connected
    */
    private class ThreadConnected extends Thread {
        private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;

        public ThreadConnected(BluetoothSocket socket) {
            connectedBluetoothSocket = socket;
            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (RUNING_THREAD_CONNECTED) {
                try {
                    bytes = connectedInputStream.read(buffer);
                    String strReceived = new String(buffer, 0, bytes);
                    final String msgReceived = String.valueOf(bytes) +
                            " bytes received:\n"
                            + strReceived;

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {
                            tvStatusConnectionDispositivo.setText(msgReceived);
//                            managerUtils.imprimirToast(ActivityListaDispositivos.this, msgReceived);
                        }});

                } catch (IOException e) {
                    e.printStackTrace();


                    final String msgConnectionLost = "Connection perdida:\n"
                            + e.getMessage();
                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            tvStatusConnectionDispositivo.setText(msgConnectionLost);
                        }});
                    resetThread();
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                connectedBluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetThread()
    {
        myThreadConnected = null;
        myThreadConnectBTdevice = null;
        RUNING_THREAD_CONNECTED = false;
    }


}



