package innova.smsgps.base.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import innova.smsgps.R;
import innova.smsgps.entities.Gender;

/**
 * Created by USUARIO on 21/05/2016.
 */
public class SpinnerGenderAdapter extends BaseAdapter implements android.widget.SpinnerAdapter
{
    private Context context;
    private ArrayList<Gender> alGenders = new ArrayList<Gender>();

    private LayoutInflater lyInflater;

    /**
     * @param context Context
     * @param alGenders ArrayList
     */
    public SpinnerGenderAdapter(Context context, ArrayList<Gender> alGenders)
    {
        this.alGenders  = alGenders;
        this.context    = context;
        this.lyInflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return alGenders.size();
    }

    @Override
    public Object getItem(int position) {
        return alGenders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, true);
    }

    // función inflar view
    public View getCustomView(int position, View convertView, ViewGroup parent, boolean isTitulo) {

        View row                        = lyInflater.inflate(R.layout.item_spinner, parent, false);
        TextView tvTextoItemSpinner     = (TextView) row.findViewById(R.id.tvTextoItemSpinner);
        tvTextoItemSpinner.setText(alGenders.get(position).getNombre());
        if (isTitulo)
            tvTextoItemSpinner.setGravity(Gravity.CENTER);
        return row;
    }
}
