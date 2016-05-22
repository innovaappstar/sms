package innova.smsgps.entities;

/**
 * Created by USUARIO on 21/05/2016.
 * Dispositivos bluetooth
 */
public class Dispositivo
{
    public Dispositivo(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    String macAddress = "";

}
