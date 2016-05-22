package innova.smsgps.entities;

import java.util.UUID;

/**
 * Created by USUARIO on 21/05/2016.
 * Dispositivos bluetooth
 */
public class Dispositivo
{
    String macAddress   = "";
    String nombre       = "";

    public Dispositivo(String macAddress)
    {
        this.macAddress = macAddress;
    }

    public Dispositivo(String macAddress, String nombre)
    {
        this.macAddress = macAddress;
        this.nombre = nombre;
    }

    public String getMacAddress() {
        return macAddress;
    }
    public String getNombre()
    {
        if (nombre == null)
            return "";

        return nombre;
    }

    //--------------------------------------------------------------------------------
    private static final String UUID_SERIAL = "00001101-0000-1000-8000-00805F9B34FB";

    public static java.util.UUID getUUID()
    {
        return UUID.fromString(UUID_SERIAL); //Standard SerialPortService ID
    }
    //--------------------------------------------------------------------------------
}
