package innova.smsgps;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import innova.smsgps.base.adapters.DispositivosBluetoothAdapter;
import innova.smsgps.beans.DispositivoBluetooth;
import innova.smsgps.communication.BridgeIPC;
import innova.smsgps.enums.IDSP2;
import innova.smsgps.sqlite.ManagerSqlite;

/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityListaDispositivosBluetooth extends BaseActivity{

    // Info
    ManagerSqlite managerSqlite;
    ListView listView;

    // sparseDispositivosBluetooth
    SparseArray<DispositivoBluetooth> sparseDispositivos = new SparseArray<DispositivoBluetooth>();

    private ArrayList<String> mDeviceList = new ArrayList<String>();
    BluetoothAdapter bluetoothAdapter;

    ArrayAdapter<String> btArrayAdapter;

    DispositivosBluetoothAdapter dispositivosBluetoothAdapter ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.lista_dispositivos_bluetooth);

        managerSqlite = new ManagerSqlite(this);
        listView = (ListView) findViewById(R.id.listDispositivosBluetooth);

        bluetoothAdapter                = BluetoothAdapter.getDefaultAdapter();

//        btArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
//        listView.setAdapter(btArrayAdapter);


        registerReceiver(ActionFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

    }


    @Override
    public void onDestroy() {
        unregisterReceiver(ActionFoundReceiver);
        super.onDestroy();
    }

    @Override
    public void listenerTimer() {

    }

    /**
     * Evento que se ejecutarA al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnListarDispositivosBluetooth:
                keyContador = 0;
                sparseDispositivos              = new SparseArray<DispositivoBluetooth>();
//                btArrayAdapter.clear();
                if(bluetoothAdapter.isDiscovering())
                {
                    bluetoothAdapter.cancelDiscovery();
                }
                bluetoothAdapter.startDiscovery();
                break;
        }
    }


    int keyContador = 0;
    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent)
        {

            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                managerUtils.imprimirLog(device.getAddress());
                keyContador ++;
                sparseDispositivos.put(keyContador, new DispositivoBluetooth(device.getAddress()));

                dispositivosBluetoothAdapter        = new DispositivosBluetoothAdapter(getApplicationContext(), sparseDispositivos);
                listView.setAdapter(dispositivosBluetoothAdapter);
                //                btArrayAdapter.add(device.getAddress());
//                btArrayAdapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // obtenemos el codColor del item presionado...
                        String macAddress = ((TextView) view.findViewById(R.id.txtTextoMacAddress)).getText().toString();
                        managerInfoMovil.setSpf2(IDSP2.MACADDRESS , macAddress);
                        enviarMensajeIPC(BridgeIPC.INDICE_CONECTARSE_BLUETOOTH, new String[]{"9|1", "Conectarse con dispositivo bluetooth--"});

//                        ServicioSms.connectDevice();
//                    ServicioSms.connectDevice(valor);
                        imprimitToast(macAddress);
//                        connectDevice(valor);


                    }
                });

            }




        }};
    private void connectDevice(String address){
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
            BluetoothSocket mmSocket = device.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
        }
        catch (  IOException e) {
            e.printStackTrace();
        }
    }
    private void imprimitToast(String data)
    {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}
