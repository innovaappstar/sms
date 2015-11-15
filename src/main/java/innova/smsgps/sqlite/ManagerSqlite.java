package innova.smsgps.sqlite;

import android.content.Context;

import innova.smsgps.beans.RegistroAlerta;
import innova.smsgps.interfaces.ISqlite;

/**
 * Created by innovaapps on 26/10/2015.
 * #Indice int para los siguientes tipos de consulta (CRUD): <br />
 * <ul>
 * <li> 00 - 20 - INSERT    </li>
 * <li> 21 - 40 - UPDATE    </li>
 * <li> 41 - 60 - DELETE    </li>
 * <li> 61 - 80 - SELECT    </li>
 * </ul>
 *
 * @return Los cÃ³digos de retorno son : <br />
 * <ul>
 * <li> 1 - TodO_ ha ido muy bien.</li>
 * <li> -1 - Error mientras se realizaba la transacciÃ³n.</li>
 * </ul>
 **/
public class ManagerSqlite implements ISqlite
{
    ISqlite managerSqlite;

    public ManagerSqlite(Context context)
    {
        managerSqlite = new Sqlite(context);
    }


    @Override
    public int ejecutarConsulta(int Indice, RegistroAlerta registroAlerta)
    {
        return managerSqlite.ejecutarConsulta(Indice, registroAlerta);
    }
}
