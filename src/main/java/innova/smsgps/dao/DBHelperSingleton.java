package innova.smsgps.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Clase singleton que se encarga de manejar lo relacionado a la base de datos
 * 
 * @author Diego Antonio Eduardo Rodriguez Valdivia
 * 
 */
public class DBHelperSingleton
{

	private static DBHelper mDBHelper;
	private static boolean isOpened;
	private static SQLiteDatabase mDatabase;

	/**
	 * método que obtiene una instacia de esta clase
	 * @param context Context
	 * @return Devuelve una instacia de esta clase
	 */
	private static DBHelper getInstance(Context context)
	{
		if (mDBHelper == null)
		{
			mDBHelper = new DBHelper(context);
			isOpened = false;
		}
		return mDBHelper;
	}


	public static void open(Context context)
	{
		if (mDBHelper != null && !isOpened)
		{
			try
			{
				mDatabase = mDBHelper.getWritableDatabase();
				isOpened = true;
			}catch (Exception e)
			{
				Log.e("EXCEPTION SQLITE", e.getMessage());
			}
		}
	}

	public static void close()
	{
		if (mDatabase != null && isOpened) {
			mDatabase.close();
			isOpened = false;
		}
	}

	/**
	 * objeto db para todas las transacciones..
	 * @param context Context
	 * @return SQLiteDatabase
	 */
	public static SQLiteDatabase getDatabase(Context context)
	{
		getInstance(context);
		open(context);
		return mDatabase;
	}

}
