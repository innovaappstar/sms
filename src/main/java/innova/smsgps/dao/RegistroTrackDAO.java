package innova.smsgps.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

import innova.smsgps.datacontractenum.RegistroTrackDataContract;
import innova.smsgps.entities.RegistroTrack;

/**
 * Created by USUARIO on 22/05/2016.
 */
public class RegistroTrackDAO
{
    /**
     * función para  convertir entidad en content values..
     * @param registroTrack RegistroTrack
     * @return ContentValues
     */
    private ContentValues transformarEntidadEnContentValues(RegistroTrack registroTrack)
    {
        ContentValues cv = new ContentValues();
        cv.put(RegistroTrackDataContract.LATITUD.getValue(),    registroTrack.getLatitud());
        cv.put(RegistroTrackDataContract.LONGITUD.getValue(),   registroTrack.getLongitud());
        cv.put(RegistroTrackDataContract.VELOCIDAD.getValue(),  registroTrack.getVelocidad());
        cv.put(RegistroTrackDataContract.BATERIA.getValue(),    registroTrack.getBateria());
        cv.put(RegistroTrackDataContract.FECHAHORA.getValue(),  registroTrack.getFechaHora());
        cv.put(RegistroTrackDataContract.IDUSUARIO.getValue(),  registroTrack.getIdUsuario());
        cv.put(RegistroTrackDataContract.TIPOTRACK.getValue(), registroTrack.getACTION());
        return cv;
    }

    /**
     * crud insert
     * @param context Context
     * @param registroTrack RegistroTrack
     * @return boolean
     */
    public boolean insertRegistroTrack(Context context, RegistroTrack registroTrack) throws SQLException
    {
        long row = DBHelperSingleton.getDatabase(context).insert(RegistroTrackDataContract.TABLA.getValue(), null, transformarEntidadEnContentValues(registroTrack));
        Log.i("SQLite", "row insert: " + row);
        return true;
    }


    /**
     public Cursor query(boolean distinct, String table, String[] columns,String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
     */
    /**
     * Cursor de entidad
     * @param context Context
     * @return Context activity
     * @throws SQLException SQLException
     */
    public Cursor getCursorObtenerTracking(Context context, String[] whereArgs) throws SQLException
    {
        String[] columnas = new String[]
                {
                        RegistroTrackDataContract.ID.getValue(), RegistroTrackDataContract.LATITUD.getValue() ,
                        RegistroTrackDataContract.LONGITUD.getValue(), RegistroTrackDataContract.VELOCIDAD.getValue() ,
                        RegistroTrackDataContract.BATERIA.getValue(), RegistroTrackDataContract.FECHAHORA.getValue() ,
                        RegistroTrackDataContract.IDUSUARIO.getValue(), RegistroTrackDataContract.TIPOTRACK.getValue()
                };
        String whereClause = RegistroTrackDataContract.TIPOTRACK.getValue() + " = ?";
        Cursor c = DBHelperSingleton.getDatabase(context).query(true, RegistroTrackDataContract.TABLA.getValue(), columnas, whereClause, whereArgs, null, null, null, null);
        if (c != null)
            c.moveToFirst();

        return c;
    }


    public ArrayList<RegistroTrack> getalRegistroTrack(Context context, String[] whereArgs) throws SQLException
    {
        Cursor cursor = getCursorObtenerTracking(context, whereArgs);
        ArrayList<RegistroTrack> alRegistroTrack = new ArrayList<RegistroTrack>();

        if (cursor.moveToFirst())
        {
            do
            {
                String latitud   = cursor.getString(cursor.getColumnIndexOrThrow(RegistroTrackDataContract.LATITUD.getValue()));
                String longitud  = cursor.getString(cursor.getColumnIndexOrThrow(RegistroTrackDataContract.LONGITUD.getValue()));
                String fechaHora = cursor.getString(cursor.getColumnIndexOrThrow(RegistroTrackDataContract.FECHAHORA.getValue()));
                String velocidad = cursor.getString(cursor.getColumnIndexOrThrow(RegistroTrackDataContract.VELOCIDAD.getValue()));
                String bateria   = cursor.getString(cursor.getColumnIndexOrThrow(RegistroTrackDataContract.BATERIA.getValue()));
                alRegistroTrack.add(new RegistroTrack(latitud, longitud, fechaHora, velocidad, bateria));
                // do what you need with the cursor here
            } while (cursor.moveToNext());
        }
        cursor.close();
        return alRegistroTrack;
    }

    /**
     * @param context Context
     * @param whereArgs String[] tipoTrack
     * @return boolean
     * @throws SQLException
     */
    public boolean deleteRegistroTrack(Context context, String[] whereArgs) throws SQLException
    {
        DBHelperSingleton.getDatabase(context).delete(RegistroTrackDataContract.TABLA.getValue(), RegistroTrackDataContract.TIPOTRACK.getValue() + " = ?", whereArgs);
        return true;
    }


}
