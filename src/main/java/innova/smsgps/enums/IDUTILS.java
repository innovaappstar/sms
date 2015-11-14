package innova.smsgps.enums;

/**
 * Created by innovaapps on 30/10/2015.
 */
public enum IDUTILS
{
    HHMM     (1),
    DDHHMMSS   (2),
    DDHHMMSSSS (3);

    private int n;

    IDUTILS(int n)
    {
        this.n = n;
    }
    public int getInt()
    {
        return n;
    }
}

