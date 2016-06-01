package innova.smsgps.enums;

/**
 * Created by innovaapps on 29/10/2015.
 */
public enum IDSP1
{
//    NUMERO01    (1),
//    NUMERO02    (2),
//    NUMERO03    (3),
//    NUMERO04    (4),
    BEEP1         (1),
    BEEP2         (2),
    BEEP3         (3),

    FLAGSPLASH  (5),

    TA1         (11),
    TA2         (12),
    TA3         (13),
    TA4         (14),
    TA5         (15),
    TA6         (16),
    TA7         (17),
    TA8         (18),
    TA9         (19),
    TA10        (20),


    IDIOMA      (30),
    IDUSUARIO   (31),
    TRACKING    (32),   // 3 ESTADOS (INACTIVO/ALERTA/SEGUIMIENTO)
    LOGUEADO    (33),   // 2 ESTADOS (0/1)
    SINVALOR    (99);

    private int n;

    IDSP1(int n)
    {
        this.n = n;
    }
    public int getInt()
    {
        return n;
    }

    /**
     * Simple función que recorrerá los valores de
     * los enumerables y retornará un idsp1
     * @param idsp1 int que buscara coincidencias de valores
     * <ul>
     *     <li> IDSP1       : NC..  </li>
     *     <li> SINVALOR    : -1    </li>
     * </ul>
     **/
    public int getInt(IDSP1 idsp1)
    {
        for(IDSP1 idsp : IDSP1.values())
        {
            if (idsp1 == idsp)
            {
                int value = idsp1.getInt();
                int rango = 0xA;
                // restamos una decima
                return value - rango;

            }
        }
        return -1;
    }

}
