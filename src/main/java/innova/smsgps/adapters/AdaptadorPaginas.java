package innova.smsgps.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import innova.smsgps.FragmentAlertas;
import innova.smsgps.FragmentContactos;
import innova.smsgps.FragmentDenuncias;
import innova.smsgps.FragmentDenunciasRegistro;

/**
 * Created by innovaapps on 30/10/2015.
 */
public class AdaptadorPaginas extends FragmentStatePagerAdapter
{
    Fragment[] FRAGMENTS = new Fragment[]{new FragmentContactos(),
            new FragmentDenunciasRegistro(),
            new FragmentAlertas(),
            new FragmentDenuncias()};

    // CONSTRUCTOR
    public AdaptadorPaginas(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return FRAGMENTS[position];
    }

    @Override
    public int getCount() {
        return FRAGMENTS.length;
    }
}
