package innova.smsgps.communication;

import android.content.Context;

import innova.smsgps.interfaces.IBridgeIPC;

/**
 * Created by innovaapps on 13/11/2015.
 */
public class ManagerBridgeIPC implements IBridgeIPC
{
    IBridgeIPC iBridgeIPC;

    public ManagerBridgeIPC(Context context)
    {
        iBridgeIPC = new BridgeIPC(context);
    }

    @Override
    public void enviarMensaje(int indice, String[] data)
    {
        iBridgeIPC.enviarMensaje(indice, data);
    }

    @Override
    public void DesconectarnosDelServicio() throws Throwable
    {
        iBridgeIPC.DesconectarnosDelServicio();
    }
}
