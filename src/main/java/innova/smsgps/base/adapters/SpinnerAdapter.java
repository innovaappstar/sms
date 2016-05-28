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
import innova.smsgps.entities.Idioma;

/**
 * Created by USUARIO on 21/05/2016.
 */
public class SpinnerAdapter extends BaseAdapter implements android.widget.SpinnerAdapter
{
    private Context context;
    private ArrayList<Idioma> alIdiomas = new ArrayList<Idioma>();

    private LayoutInflater lyInflater;
    public static int WHITE         = 1;
    public static int LIGHT_BLUE    = 2;
    private int iColorSp = LIGHT_BLUE;
    /**
     * @param context Context
     * @param alIdiomas ArrayList
     */
    public SpinnerAdapter(Context context, ArrayList<Idioma> alIdiomas, int iColorSp)
    {
        this.alIdiomas  = alIdiomas;
        this.context    = context;
        this.lyInflater = LayoutInflater.from(this.context);
        this.iColorSp   = iColorSp;
    }


    @Override
    public int getCount() {
        return alIdiomas.size();
    }

    @Override
    public Object getItem(int position) {
        return alIdiomas.get(position);
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

        View row                        = lyInflater.inflate(R.layout.item_spinner, parent, false); // celeste
        if (iColorSp == WHITE)
            row                         = lyInflater.inflate(R.layout.item_spinner_white, parent, false); // white

        TextView tvTextoItemSpinner     = (TextView) row.findViewById(R.id.tvTextoItemSpinner);
        tvTextoItemSpinner.setText(alIdiomas.get(position).getNombre());
        if (isTitulo)
            tvTextoItemSpinner.setGravity(Gravity.CENTER);
        return row;
    }
}
