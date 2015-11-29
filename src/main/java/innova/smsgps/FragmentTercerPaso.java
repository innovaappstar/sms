package innova.smsgps;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by innovaapps on 30/10/2015.
 */
public class FragmentTercerPaso extends BaseFragment
{
    ViewGroup rootView;
    ListView listView;

    private ArrayList<String> mDeviceList = new ArrayList<String>();
    BluetoothAdapter bluetoothAdapter;

    ArrayAdapter<String> btArrayAdapter;

    private void IniciandoVistas()
    {
        ((Button)rootView.findViewById(R.id.btnAceptar)).setOnClickListener(this);
        listView = (ListView) rootView.findViewById(R.id.list);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.items_list_bluetooth);
        listView.setAdapter(btArrayAdapter);


        getActivity().registerReceiver(ActionFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(ActionFoundReceiver);

        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = (ViewGroup)inflater.inflate(R.layout.activity_config_paso_3, container, false);
        super.IniciarInstancias();
        IniciandoVistas();
        return rootView;
    }

    @Override
    public void listenerTimer() {

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
                        ServicioSms.connectDevice(valor);
//                        imprimitToast(valor);
//                        connectDevice(valor);
                    }
                });
            }
        }};
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnAceptar:
                btArrayAdapter.clear();
                if(bluetoothAdapter.isDiscovering())
                {
                    bluetoothAdapter.cancelDiscovery();
                }
                bluetoothAdapter.startDiscovery();
                managerUtils.imprimirToast(getActivity(), "BUSCANDO..");
                break;
        }
    }
}
