package innova.smsgps.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import innova.smsgps.FragmentConfiguracionFinalizada;
import innova.smsgps.FragmentPrimerPaso;
import innova.smsgps.FragmentSegundoPaso;
import innova.smsgps.FragmentTercerPaso;

/**
 * Created by innovaapps on 30/10/2015.
 */
public class AdaptadorPaginasConfiguracion extends FragmentStatePagerAdapter
{
    private String TITULOS[] = new String[] { "Paso1", "Paso2", "Paso3", "Finalizado" };
    Fragment[] FRAGMENTS = new Fragment[]{new FragmentPrimerPaso(),
            new FragmentSegundoPaso(),
            new FragmentTercerPaso(),
            new FragmentConfiguracionFinalizada()};

    // CONSTRUCTOR
    public AdaptadorPaginasConfiguracion(FragmentManager fm)
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

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return TITULOS[position];
    }
}