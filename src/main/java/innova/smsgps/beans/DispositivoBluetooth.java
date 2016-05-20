package innova.smsgps.beans;

/**
 * Created by USUARIO on 23/04/2016.
 */
public class DispositivoBluetooth
{


    public String macAddress = "";

    public DispositivoBluetooth(String macAddress)
    {
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }
}
