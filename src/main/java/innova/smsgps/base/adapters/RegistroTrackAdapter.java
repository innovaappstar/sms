package innova.smsgps.base.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import innova.smsgps.R;
import innova.smsgps.entities.RegistroTrack;

/**
 * Created by USUARIO on 21/05/2016.
 */
public class RegistroTrackAdapter extends BaseAdapter
{
    ArrayList<RegistroTrack> alRegistroTrack = new ArrayList<RegistroTrack>();
    LayoutInflater inflater;
    Context context;

    public RegistroTrackAdapter(ArrayList<RegistroTrack> alRegistroTrack, Context context) {
        this.alRegistroTrack = alRegistroTrack;
        this.context = context;
        inflater            = LayoutInflater.from(this.context);
    }
    @Override
    public int getCount() {
        return alRegistroTrack.size();
    }

    @Override
    public RegistroTrack getItem(int position)
    {
        return alRegistroTrack.get(position);
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
            convertView = inflater.inflate(R.layout.item_list_tracker, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RegistroTrack registroTrack = getItem(position);
        String tipo = (registroTrack.getACTION() == RegistroTrack.TIPOALERTA) ? "Alerta" : "Seguimiento";
        viewHolder.tvTipo.setText(tipo);
        viewHolder.tvDetalle.setText(registroTrack.getVelocidad() + " - " + registroTrack.getBateria());
        viewHolder.tvFechaHora.setText(registroTrack.getFechaHora());
        return convertView;
    }

    private class ViewHolder
    {
        TextView tvTipo, tvDetalle, tvFechaHora;

        public ViewHolder(View item)
        {
            tvTipo          = (TextView) item.findViewById(R.id.tvTipo);
            tvDetalle       = (TextView) item.findViewById(R.id.tvDetalle);
            tvFechaHora     = (TextView) item.findViewById(R.id.tvFechaHora);
        }
    }
}

