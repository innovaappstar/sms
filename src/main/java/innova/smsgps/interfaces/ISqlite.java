package innova.smsgps.interfaces;

import innova.smsgps.beans.RegistroAlerta;
import innova.smsgps.beans.RegistroDenuncias;

/**
 * Created by innovaapps on 26/10/2015.
 */
public interface ISqlite {
    public int ejecutarConsulta(int Indice, RegistroAlerta registroAlerta, RegistroDenuncias registroDenuncias);
}
