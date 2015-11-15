package innova.smsgps.enums;

/**
 * Created by innovaapps on 29/10/2015.
 */
public enum IDSP2
{
    NUMERO01    (1),
    NUMERO02    (2),
    NUMERO03    (3),
    NUMERO04    (4),
    IDFACEBOOK  (5),
    IDREGISTROALERTASMOVIL  (6);

    private int n;

    IDSP2(int n)
    {
        this.n = n;
    }
    public int getInt()
    {
        return n;
    }
}
