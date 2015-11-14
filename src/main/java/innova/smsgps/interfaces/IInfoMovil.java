package innova.smsgps.interfaces;


import innova.smsgps.enums.IDSP1;
import innova.smsgps.enums.IDSP2;

/**
 * Created by innovaapps on 29/10/2015.
 */
public interface IInfoMovil
{
    public void setSpf1(IDSP1 idsp1, int i);
    public int getSPF1(IDSP1 idsp1);

    public void setSpf2(IDSP2 idsp2, String s);

    public String getSPF2(IDSP2 idsp2);
}
