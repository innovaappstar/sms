package innova.smsgps.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.sql.SQLException;

import innova.smsgps.datacontractenum.ContactoDataContract;
import innova.smsgps.entities.Contacto;

/**
 * Created by USUARIO on 22/05/2016.
 */
public class ContactoDAO
{
    /**
     * función para  convertir entidad en content values..
     * @param contacto Contacto
     * @return ContentValues
     */
    private ContentValues transformarEntidadEnContentValues(Contacto contacto)
    {
        ContentValues cv = new ContentValues();
        cv.put(ContactoDataContract.TELEFONO.getValue(), contacto.getTelefono());
        cv.put(ContactoDataContract.NOMBRE.getValue(),contacto.getNombre());
        cv.put(ContactoDataContract.EMAIL.getValue(), contacto.getEmail());
        cv.put(ContactoDataContract.CODCONTACTO.getValue(), contacto.getCodContacto());
        return cv;
    }

    public boolean insertContacto(Context context, Contacto contacto) throws SQLException
    {
        Cursor cursorUser = getCursorObtenerContacto(context, contacto);
        if (cursorUser != null && cursorUser.getCount() > 0)
        {
            contacto.set_id(cursorUser.getString(cursorUser.getColumnIndexOrThrow(ContactoDataContract.ID.getValue())));
            return this.updateContacto(context, contacto);
        }
        long row = DBHelperSingleton.getDatabase(context).insert(ContactoDataContract.TABLA.getValue(), null, transformarEntidadEnContentValues(contacto));
        Log.i("SQLite", "row insert: " + row);
        return true;
    }

    /**
     * @param context Context
     * @param contacto Contacto
     * @return boolean
     * @throws SQLException
     */
    public boolean deleteContacto(Context context, Contacto contacto) throws SQLException
    {
        Cursor cursorUser = getCursorObtenerContacto(context, contacto);
        if (cursorUser != null && cursorUser.getCount() > 0)
        {
            contacto.set_id(cursorUser.getString(cursorUser.getColumnIndexOrThrow(ContactoDataContract.ID.getValue())));
            int row = DBHelperSingleton.getDatabase(context).delete(ContactoDataContract.TABLA.getValue(), ContactoDataContract.ID.getValue() + " = ?", new String[]{contacto.get_id()});
            return (row == 1);  // eliminó almenos un registro..
        }
        return false;
    }

    /**
     * Simple función para actualizar usuario..
     * @param context Context
     * @param contacto Contacto
     * @return
     * @throws SQLException
     */
    private boolean updateContacto(Context context, Contacto contacto) throws SQLException
    {
        long row = DBHelperSingleton.getDatabase(context).update(ContactoDataContract.TABLA.getValue(), transformarEntidadEnContentValues(contacto), ContactoDataContract.ID.getValue() + " = ?", new String[]{contacto.get_id()});
        Log.i("SQLite", "row update:: " + contacto.getTelefono());
        return true;
    }

    /**
     * Cursor de entidad
     * @param context Context
     * @return Context activity
     * @throws SQLException SQLException
     */
    public Cursor getCursorObtenerContacto(Context context, Contacto contacto) throws SQLException
    {
        String[] columnas = new String[]
                {
                ContactoDataContract.ID.getValue(), ContactoDataContract.TELEFONO.getValue() ,
                ContactoDataContract.NOMBRE.getValue(), ContactoDataContract.EMAIL.getValue(), ContactoDataContract.CODCONTACTO.getValue()
                };
        String whereClause  = ContactoDataContract.CODCONTACTO.getValue() + " = ? ";
        String[] whereArgs = new String[]{contacto.getCodContacto()};

        Cursor c = DBHelperSingleton.getDatabase(context).query(true, ContactoDataContract.TABLA.getValue(), columnas, whereClause, whereArgs, null, null, null, null);

        if (c != null)
            c.moveToFirst();

        return c;
    }

    /**
     * @param contacto Contacto
     * @return Contacto
     */
    public Contacto getContacto(Context context, Contacto contacto)
    {
        Contacto contactoB = new Contacto();
        Cursor cursorUser = null;
        try
        {
            cursorUser = getCursorObtenerContacto(context, contacto);
            if (cursorUser != null && cursorUser.getCount() > 0)
            {
                String telefono     = cursorUser.getString(cursorUser.getColumnIndexOrThrow(ContactoDataContract.TELEFONO.getValue()));
                String nombre       = cursorUser.getString(cursorUser.getColumnIndexOrThrow(ContactoDataContract.NOMBRE.getValue()));
                String email        = cursorUser.getString(cursorUser.getColumnIndexOrThrow(ContactoDataContract.EMAIL.getValue()));
                String codContacto  = cursorUser.getString(cursorUser.getColumnIndexOrThrow(ContactoDataContract.CODCONTACTO.getValue()));
                //    public Contacto(int ACTION, String telefono, String nombre, String email, String codContacto) {
                contactoB = new Contacto(telefono, nombre, email, codContacto);
                return contactoB;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactoB;
    }

}
