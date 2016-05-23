package innova.smsgps.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import innova.smsgps.datacontractenum.UserDataContract;
import innova.smsgps.enums.SqliteEnum;


/**
 * Clase que extiende SQLiteOpenHelper que se encarga de la conexión a la base
 * de datos
 * 
 */
public class DBHelper extends SQLiteOpenHelper
{
	//region definición de tablas...
	private String sCrearTablaUser	= "CREATE TABLE " + UserDataContract.TABLA.getValue() + "("
			+ UserDataContract.ID.getValue()    		+ " INTEGER PRIMARY KEY,"
			+ UserDataContract.EMAIL.getValue()     	+ " TEXT NOT NULL,"
			+ UserDataContract.PASSWORD.getValue() 		+ " TEXT NOT NULL,"
			+ UserDataContract.FIRSTNAME.getValue() 	+ " TEXT,"
			+ UserDataContract.LASTNAME.getValue()  	+ " TEXT,"
			+ UserDataContract.LANGUAJE.getValue()  	+ " TEXT,"
			+ UserDataContract.GENDER.getValue()    	+ " TEXT,"
			+ UserDataContract.BIRTHDAY.getValue()  	+ " TEXT,"
			+ UserDataContract.COUNTRY.getValue()  		+ " TEXT,"
			+ UserDataContract.ID_FACEBOOK.getValue()  	+ " TEXT"
			+ ")";

	//endregion

	public DBHelper(Context context)
	{
		super(context, SqliteEnum.DATABASE_NAME.getValue(), null, Integer.parseInt(SqliteEnum.DATABASE_VERSION.getValue()));
	}

	/**
	 * Método que se ejecuta cuando no existe la base de datos
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(sCrearTablaUser);
	}

	/**
	 * Método que se ejecuta cuando la versión de la base de datos aumenta
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
