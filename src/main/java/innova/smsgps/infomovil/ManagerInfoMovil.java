package innova.smsgps.infomovil;

import android.content.Context;

import innova.smsgps.enums.IDSP1;
import innova.smsgps.enums.IDSP2;
import innova.smsgps.interfaces.IInfoMovil;

/**
 * Created by innovaapps on 29/10/2015.
 */
public class ManagerInfoMovil implements IInfoMovil
{
    public static IInfoMovil infoMovil;

    public ManagerInfoMovil(Context context)
    {
        infoMovil = new InfoMovil(context);
    }

    @Override
    public void setSpf1(IDSP1 idsp1, int i) {
        infoMovil.setSpf1(idsp1, i);
    }

    @Override
    public int getSPF1(IDSP1 idsp1) {
        return infoMovil.getSPF1(idsp1);
    }

    @Override
    public void setSpf2(IDSP2 idsp2, String s) {
        infoMovil.setSpf2(idsp2, s);
    }

    @Override
    public String getSPF2(IDSP2 idsp2) {
        return infoMovil.getSPF2(idsp2);
    }
}
