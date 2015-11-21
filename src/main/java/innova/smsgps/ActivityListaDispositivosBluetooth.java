package innova.smsgps;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import innova.smsgps.sqlite.ManagerSqlite;

/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityListaDispositivosBluetooth extends Activity {

    // Info
    ManagerSqlite managerSqlite;
    ListView listView;

    private ArrayList<String> mDeviceList = new ArrayList<String>();
    BluetoothAdapter bluetoothAdapter;

    ArrayAdapter<String> btArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.lista_dispositivos_bluetooth);

        managerSqlite = new ManagerSqlite(this);
        listView = (ListView) findViewById(R.id.listDispositivosBluetooth);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(btArrayAdapter);


        registerReceiver(ActionFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(ActionFoundReceiver);

        super.onDestroy();
    }
    /**
     * Evento que se ejecutarA al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */

    public void onClickConfig(View view) {
        switch (view.getId()) {
            case R.id.btnListarDispositivosBluetooth:
                btArrayAdapter.clear();
                if(bluetoothAdapter.isDiscovering())
                {
                    bluetoothAdapter.cancelDiscovery();
                }
                bluetoothAdapter.startDiscovery();
                break;
        }
    }
    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                btArrayAdapter.add(device.getAddress());
                btArrayAdapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String valor = (String)parent.getItemAtPosition(position);
                        imprimitToast(valor);
                        connectDevice(valor);
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
