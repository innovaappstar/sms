package innova.smsgps.base.adapters;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import innova.smsgps.R;
import innova.smsgps.beans.DispositivoBluetooth;
import innova.smsgps.utils.ManagerUtils;

/**
 * Created by USUARIO on 23/04/2016.
 */
public class DispositivosBluetoothAdapter extends BaseAdapter
{
    // mapeo de beans boletos...
    SparseArray<DispositivoBluetooth> sparseDispositivos = new SparseArray<DispositivoBluetooth>();
    LayoutInflater inflater;
    Context context;
    ManagerUtils managerUtils           = new ManagerUtils();
    /**
     * Constructor Adapter
     * @param context Context
     * @param sparseDispositivos SparseArray<DispositivoBluetooth>
     */
    public DispositivosBluetoothAdapter(Context context, SparseArray<DispositivoBluetooth> sparseDispositivos)
    {
        this.sparseDispositivos = sparseDispositivos;
        this.context            = context;
        this.inflater           = LayoutInflater.from(this.context);
    }


    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHolder;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.items_list_bluetooth, parent, false);
            viewHolder  = new ViewHolder(convertView);
            convertView.setTag(viewHolder); // tag
        }else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        try
        {
            final DispositivoBluetooth dispositivoBluetooth = getItem(position);
            if (dispositivoBluetooth != null)
                viewHolder.txtTextoMacAddress.setText(dispositivoBluetooth.getMacAddress());

//            if (viewHolder.txtTextoMacAddress.toString().length() == 0)
//                viewHolder.relativeLayoutBluetooth.setVisibility(View.GONE);

        }catch (Exception e)
        {

        }



        return convertView;
    }



    private class ViewHolder
    {
        TextView txtTextoMacAddress;
        RelativeLayout relativeLayoutBluetooth;

        public ViewHolder(View item)
        {
            // -----
            txtTextoMacAddress      = (TextView) item.findViewById(R.id.txtTextoMacAddress);
            relativeLayoutBluetooth = (RelativeLayout) item.findViewById(R.id.relativeLayoutItemBluetooth);
        }
    }

    @Override
    public int getCount() {
        return sparseDispositivos.size();
    }
    @Override
    public DispositivoBluetooth getItem(int position) {
        return sparseDispositivos.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
}
