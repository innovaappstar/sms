package innova.smsgps.base.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import innova.smsgps.R;
import innova.smsgps.entities.Dispositivo;

/**
 * Created by USUARIO on 21/05/2016.
 */
public class DispositivosAdapter extends BaseAdapter
{
    ArrayList<Dispositivo> alDispositivo = new ArrayList<Dispositivo>();
    LayoutInflater inflater;
    Context context;

    public DispositivosAdapter(ArrayList<Dispositivo> alDispositivo, Context context) {
        this.alDispositivo = alDispositivo;
        this.context = context;
        inflater            = LayoutInflater.from(this.context);
    }
    @Override
    public int getCount() {
        return alDispositivo.size();
    }

    @Override
    public Dispositivo getItem(int position)
    {
        return alDispositivo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_list_dispositivo, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Dispositivo dispositivo = getItem(position);
        viewHolder.tvNombre.setText(dispositivo.getNombre());
        viewHolder.tvMacAddress.setText(dispositivo.getMacAddress());
        return convertView;
    }

    private class ViewHolder
    {
        TextView tvMacAddress, tvNombre;

        public ViewHolder(View item)
        {
            tvNombre        = (TextView) item.findViewById(R.id.tvNombre);
            tvMacAddress    = (TextView) item.findViewById(R.id.tvMacAddress);
        }
    }
}

