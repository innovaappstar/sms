package innova.smsgps.enums;

/**
 * Created by innovaapps on 29/10/2015.
 */
public enum IDSP1
{
    NUMERO01    (1),
    NUMERO02    (2),
    NUMERO03    (3),
    NUMERO04    (4),
    FLAGSPLASH  (5);

    private int n;

    IDSP1(int n)
    {
        this.n = n;
    }
    public int getInt()
    {
        return n;
    }
}
