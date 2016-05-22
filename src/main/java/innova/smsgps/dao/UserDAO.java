package innova.smsgps.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.sql.SQLException;

import innova.smsgps.datacontractenum.UserDataContract;
import innova.smsgps.entities.User;

/**
 * Created by USUARIO on 22/05/2016.
 */
public class UserDAO
{
    /**
     * función para  convertir entidad en content values..
     * @param user User
     * @return ContentValues
     */
    private ContentValues transformarEntidadEnContentValues(User user)
    {
        ContentValues cv = new ContentValues();
        cv.put(UserDataContract.FIRSTNAME.getValue(), user.getFirstName());
        cv.put(UserDataContract.EMAIL.getValue(), user.getEmail());
        cv.put(UserDataContract.LASTNAME.getValue(), user.getLastName());
        cv.put(UserDataContract.GENDER.getValue(), user.getGender());
        cv.put(UserDataContract.PASSWORD.getValue(), user.getPassword());
        cv.put(UserDataContract.LANGUAJE.getValue(), user.getLanguaje());
//        cv.put(UserDataContract.BIRTHDAY.getValue(), user);
//        cv.put(UserDataContract.COUNTRY.getValue(), user);
        return cv;
    }

    /**
     * crud insert - update
     * @param context Context
     * @param user User
     * @return boolean
     */
    public boolean insertUser(Context context, User user) throws SQLException
    {
        Cursor cursorUser = getCursorObtenerUser(context);
        if (cursorUser != null && cursorUser.getCount() > 0)
        {
            user.set_id(cursorUser.getString(cursorUser.getColumnIndexOrThrow(UserDataContract.ID.getValue())));
            Log.i("SQLite", "_id ::: " + user.get_id());
            return this.updateUser(context, user);
        }
        long row = DBHelperSingleton.getDatabase(context).insert(UserDataContract.TABLA.getValue(), null, transformarEntidadEnContentValues(user));
        Log.i("SQLite", "row insert: " + row);
        return true;
    }

    /**
     * Simple función para actualizar usuario..
     * @param context Context
     * @param user User
     * @return
     * @throws SQLException
     */
    private boolean updateUser(Context context, User user) throws SQLException
    {
//            db.update(TABLE_PROFILE_SETTINGS_FOR_WEEKLY, values,Profile_Name_for_weekly +" = ?", new String[] {profilename});
        long row = DBHelperSingleton.getDatabase(context).update(UserDataContract.TABLA.getValue(), transformarEntidadEnContentValues(user), UserDataContract.ID.getValue() + " = ?", new String[]{user.get_id()});
        Log.i("SQLite", "row update:: " + user.getFirstName());
        return true;
    }

    /**
     * Cursor de entidad
     * @param context Context
     * @return Context activity
     * @throws SQLException SQLException
     */
    public Cursor getCursorObtenerUser(Context context) throws SQLException
    {
        String[] columnas = new String[]{UserDataContract.ID.getValue(), UserDataContract.EMAIL.getValue()} ;
        Cursor c = DBHelperSingleton.getDatabase(context).query(true, UserDataContract.TABLA.getValue(), columnas, null, null, null, null, null, null);
        if (c != null)
            c.moveToFirst();

        return c;
    }
}
