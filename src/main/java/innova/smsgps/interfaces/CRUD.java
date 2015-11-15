package innova.smsgps.interfaces;

/**
 * Created by innovaapps on 29/10/2015.
 * ENUMERABLES PARA FUNCIONES REUSABLES
 **/
public enum CRUD
{
    INSERT      (1),
    UPDATE      (2),
    DELETE      (3),
    SELECT      (4);

    private int n;

    CRUD(int n)
    {
        this.n = n;
    }
    public int getInt()
    {
        return n;
    }
}
