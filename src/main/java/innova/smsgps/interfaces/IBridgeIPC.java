package innova.smsgps.interfaces;

/**
 * Created by innovaapps on 13/11/2015.
 */
public interface IBridgeIPC
{
    public void enviarMensaje(int indice, String[] data);
    public void DesconectarnosDelServicio()  throws Throwable;
}
