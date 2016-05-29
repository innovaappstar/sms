package innova.smsgps.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import innova.smsgps.datacontractenum.ContactoDataContract;
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
			+ UserDataContract.FIRSTNAME.getValue() 	+ " TEXT DEFAULT '',"
			+ UserDataContract.LASTNAME.getValue()  	+ " TEXT DEFAULT '',"
			+ UserDataContract.LANGUAJE.getValue()  	+ " TEXT DEFAULT '1',"	// SPANISH
			+ UserDataContract.GENDER.getValue()    	+ " TEXT DEFAULT '',"
			+ UserDataContract.BIRTHDAY.getValue()  	+ " TEXT DEFAULT '',"
			+ UserDataContract.COUNTRY.getValue()  		+ " TEXT DEFAULT '',"
			+ UserDataContract.ID_FACEBOOK.getValue()  	+ " TEXT DEFAULT ''"
			+ ")";

	private String sCrearTablaContactos	= "CREATE TABLE " + ContactoDataContract.TABLA.getValue() + "("
			+ ContactoDataContract.ID.getValue()    		+ " INTEGER PRIMARY KEY,"
			+ ContactoDataContract.TELEFONO.getValue()     	+ " TEXT DEFAULT '',"
			+ ContactoDataContract.NOMBRE.getValue() 		+ " TEXT DEFAULT '',"
			+ ContactoDataContract.EMAIL.getValue() 		+ " TEXT DEFAULT '',"
			+ ContactoDataContract.CODCONTACTO.getValue()  	+ " TEXT DEFAULT ''"
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
		db.execSQL(sCrearTablaContactos);
	}

	/**
	 * Método que se ejecuta cuando la versión de la base de datos aumenta
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
