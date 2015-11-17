package innova.smsgps.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import innova.smsgps.application.Globals;
import innova.smsgps.beans.HistorialRegistros;
import innova.smsgps.beans.ListRegistrosAlertas;
import innova.smsgps.beans.ListRegistrosDenuncias;
import innova.smsgps.beans.RegistroAlerta;
import innova.smsgps.beans.RegistroDenuncias;
import innova.smsgps.enums.IDSP2;
import innova.smsgps.interfaces.CRUD;
import innova.smsgps.interfaces.ISqlite;

/**
 * Created by innovaapps on 23/10/2015.
 * VALIDAR BIEN LAS TRANSACCIONES! REALIZAR PRUEBAS!! 28/10/2015 15:00:00
 */
public class Sqlite extends SQLiteOpenHelper implements ISqlite {

    /**
     * DETALLES DATABASE
     **/
    private static int mVersionBD   =   4;
    private static String mNombreBD = "BDSMSGPS";
    private static Context mContext = null;
    private String mDirectorioBD    = "BD";

    /**
     * NOMBRES DE TABLAS
     **/
    private String TbRegistroAlerta     =   "TbRegistroAlerta";
    private String TbRegistroDenuncia   =   "TbRegistroDenuncia";

    /**
     * Campos TbRegistroAlerta
     **/
    private String KeyIdRegistroAlertas = "IdRegistroAlertas";
    private String KeyIdFacebook        = "IdFacebook";
    private String KeyIdTipoAlerta      = "IdTipoAlerta";
    private String KeyLat               = "Latitud";
    private String KeyLng               = "Longitud";
    private String KeyFechaHora         = "FechaHora";
    private String KeyFlagServidor      = "Enviado";

    private String KeyIdRegistroDenuncias   = "IdRegistroDenuncias";
//    private String KeyLat                   = "Latitud";
//    private String KeyLng                   = "Longitud";
//    private String KeyFechaHora             = "FechaHora";
    private String KeyDescripcion           = "Descripcion";
    private String KeyIdTipoDenuncia        = "IdTipoDenuncia";
    private String KeyImagenDenuncia        = "Imagen";

//    private String KeyIdFacebook            = "IdFacebook";
//    private String KeyFlagServidor          = "Enviado";

    //region -------DEFINICIÓN DE TABLAS------
    private String mCrearTbRegistroAlertas  = "CREATE TABLE " + TbRegistroAlerta + "("
            + KeyIdRegistroAlertas      + " INTEGER PRIMARY KEY,"
            + KeyIdFacebook             + " TEXT NOT NULL,"
            + KeyIdTipoAlerta           + " INTEGER NOT NULL,"
            + KeyLat                    + " TEXT NOT NULL,"
            + KeyLng                    + " TEXT NOT NULL,"
            + KeyFechaHora              + " TEXT NOT NULL,"
            + KeyFlagServidor           + " INTEGER DEFAULT '0'"
            + ")";

    private String mCrearTbRegistroDenuncia  = "CREATE TABLE " + TbRegistroDenuncia + "("
            + KeyIdRegistroDenuncias    + " INTEGER PRIMARY KEY,"
            + KeyLat                    + " TEXT NOT NULL,"
            + KeyLng                    + " TEXT NOT NULL,"
            + KeyFechaHora              + " TEXT NOT NULL,"
            + KeyDescripcion            + " TEXT NOT NULL,"
            + KeyIdTipoDenuncia         + " INTEGER NOT NULL,"
            + KeyIdFacebook             + " TEXT NOT NULL,"
            + KeyImagenDenuncia         + " BLOB,"
            + KeyFlagServidor           + " INTEGER DEFAULT '0'"
            + ")";


    //endregion

    public Sqlite(Context context) {
        super(context, mNombreBD, null, mVersionBD);
        this.mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(mCrearTbRegistroAlertas);
        db.execSQL(mCrearTbRegistroDenuncia);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TbRegistroAlerta);
        db.execSQL("DROP TABLE IF EXISTS " + TbRegistroDenuncia);
        onCreate(db);
    }

    /**
     * #Indice int para los siguientes tipos de consulta (CRUD): <br />
     * <ul>
     * <li> 00 - 20 - INSERT    </li>
     * <li> 21 - 40 - UPDATE    </li>
     * <li> 41 - 60 - DELETE    </li>
     * <li> 61 - 80 - SELECT    </li>
     * </ul>
     *
     * @return Los códigos de retorno son : <br />
     * <ul>
     * <li> 1 - TodO_ ha ido muy bien.</li>
     * <li> -1 - Error mientras se realizaba la transacción.</li>
     * </ul>
     **/
    @Override
    public int ejecutarConsulta(int Indice, RegistroAlerta registroAlerta, RegistroDenuncias registroDenuncias)
    {
        SQLiteDatabase  db          = this.getWritableDatabase();   // ABRIENDO CONEXIÓN
        SQLiteStatement stmt        = null;                         // DECLARACIÓN PREPARADA
        ContentValues   parametros  = new ContentValues();
        int result = -1;
        String sql = null;

        switch (Indice)
        {

            case 1 :    // INSERTAR APERTURA DE CAJA GESTIÓN CONDUCTOR
                try
                {

                    sql = getStmtSql(CRUD.INSERT, new String[]{KeyIdFacebook, KeyIdTipoAlerta, KeyLat, KeyLng, KeyFechaHora}, TbRegistroAlerta);

                    // INICIAMOS TRANSACCIÓN Y COMPILAMOS CONSULTA
                    db.beginTransactionNonExclusive();
                    stmt = db.compileStatement(sql);

                    stmt.bindString(1, registroAlerta.getIdFacebook());
                    stmt.bindLong(2, registroAlerta.getIdTipoAlerta());
                    stmt.bindString(3, registroAlerta.getLatitud());
                    stmt.bindString(4, registroAlerta.getLongitud());
                    stmt.bindString(5, registroAlerta.getFechaHora());
                    // EJECUTAMOS Y LIMPIAMOS LA DECLARACIÓN/SENTENCIA PREPARADA (ANALIZAR clearBindings)
                    stmt.execute();
                    stmt.clearBindings();
                    // SI LA INSTRUCCIÓN LLEGA HASTA AQUI , INDICAMOS QUE FUE UN ÉXITO
                    db.setTransactionSuccessful();
                    Globals.getInfoMovil().setSpf2(IDSP2.IDREGISTROALERTASMOVIL, ObtenerRegistro(1));
                    BackUpDataBase();
                    result = 1;
                }finally
                {
                    db.endTransaction();    // CERRAMOS TRANSACCIÓN
                    db.close();
                    return result;
                }
            case 2 :    // INSERTAR REGISTRO DE DENUNCIA
                try
                {
                    sql = getStmtSql(CRUD.INSERT, new String[]{KeyLat, KeyLng, KeyFechaHora, KeyDescripcion, KeyIdTipoDenuncia, KeyImagenDenuncia, KeyIdFacebook}, TbRegistroDenuncia);

                    // INICIAMOS TRANSACCIÓN Y COMPILAMOS CONSULTA ........................ aqui
                    db.beginTransactionNonExclusive();
                    stmt = db.compileStatement(sql);

                    stmt.bindString (1, registroDenuncias.getLatitud());
                    stmt.bindString (2, registroDenuncias.getLongitud());
                    stmt.bindString (3, registroDenuncias.getFechaHora());
                    stmt.bindString (4, registroDenuncias.getDescripcion());
                    stmt.bindLong(5, Integer.valueOf(registroDenuncias.getIdTipoDenuncia()));
                    stmt.bindBlob(6, registroDenuncias.getImgDenuncia());
                    stmt.bindString (7, registroDenuncias.getIdFacebook());
                    stmt.execute();
                    stmt.clearBindings();
                    // SI LA INSTRUCCIÓN LLEGA HASTA AQUI , INDICAMOS QUE FUE UN ÉXITO
                    db.setTransactionSuccessful();
                    BackUpDataBase();
                    result = 1;
                }finally
                {
                    db.endTransaction();    // CERRAMOS TRANSACCIÓN
                    db.close();
                    return result;
                }
            case 3: // INSERTAR EN CONSUMO ELECTRÓNICO

                break;
            case 21 :   // ACTUALIZAR FLAG SERVIDOR
                try
                {

                    sql = getStmtSql(CRUD.UPDATE, new String[]{KeyFlagServidor, KeyIdRegistroAlertas}, TbRegistroAlerta);

                    db.beginTransactionNonExclusive();
                    stmt = db.compileStatement(sql);

                    stmt.bindLong (1  , 1);     // ACTUALIZAMOS FLAG SERVIDOR
                    stmt.bindLong (2  , Integer.valueOf(Globals.getInfoMovil().getSPF2(IDSP2.IDREGISTROALERTASMOVIL)));

                    stmt.execute();
                    stmt.clearBindings();
                    db.setTransactionSuccessful();
                    BackUpDataBase();
                    result = 1;
                }finally
                {
                    db.endTransaction();
                    db.close();
                    return result;
                }
            case 61:    // LISTA REGISTROS PARA MOSTRARLOS EN UN LISTVIEW
                try
                {
                    if(ObtenerRegistro(2).equals("1"))
                    {
                        result = 1;
                    }
                }finally
                {
                    return result;
                }
            case 62:    // LISTA REGISTROS LOCALES PARA MOSTRARLOS EN EL MAPA
                try
                {
                    if(ObtenerRegistro(3).equals("1"))
                    {
                        result = 1;
                    }
                }finally
                {
                    return result;
                }
            case 63:    // LISTA REGISTROS PARA MOSTRARLOS EN UN LISTADO
                try
                {
                    if(ObtenerRegistro(4).equals("1"))
                    {
                        result = 1;
                    }
                }finally
                {
                    return result;
                }
        }
        return result;
    }

    public String ObtenerRegistro(int IndiceConsulta)
    {
        SQLiteDatabase db   = this.getWritableDatabase();
        String sql          = null;
        String result       = "-1";
        Cursor cursor       = null;
        switch (IndiceConsulta)
        {
            case 1 :
                try
                {
                    sql = "SELECT COUNT(*) FROM " + TbRegistroAlerta;
                    cursor = db.rawQuery(sql, null);
                    if(cursor.getCount() > 0)
                    {
                        cursor.moveToFirst();
                        result = String.valueOf(cursor.getInt(0));
                        cursor.close();
                    }
                }finally
                {
                    return result;
                }
            case 2:
                // COLOCAR EN UN ASYNTASK
                try
                {
                    ListRegistrosAlertas listRegistrosAlertas = new ListRegistrosAlertas();
                    ArrayList<RegistroAlerta> list = new ArrayList<RegistroAlerta>();


                    sql = "SELECT " + KeyIdTipoAlerta + "," + KeyFechaHora + "," + KeyFlagServidor + " FROM " + TbRegistroAlerta;
                    cursor = db.rawQuery(sql, null);
                    if(cursor.getCount() > 0)
                    {
                        int NumeroFila = 0;
                        cursor.moveToFirst();
                        while ((!cursor.isAfterLast()) && NumeroFila < cursor.getCount())
                        {
                            NumeroFila++;
                            RegistroAlerta registroAlerta = new RegistroAlerta();               // BAD PRACTICE
                            registroAlerta.setIdTipoAlerta(Integer.valueOf(cursor.getString(0)));
                            registroAlerta.setFechaHoraSqlite(cursor.getString(1));
                            registroAlerta.setFlagServidorSqlite(cursor.getString(2));
                            list.add(registroAlerta);
                            cursor.moveToNext();
                        }
                        cursor.close();
                        result = "1";
                        // ENVIAMOS ARRAYLIST AL BEAN , PARA OBTENERLO DESDE OTRAS CLASES
                        listRegistrosAlertas.setListRegistrosAlertas(list);
                    }
                }finally
                {
                    return result;
                }
            case 3:
                try
                {
                    ListRegistrosAlertas listRegistrosAlertas   = new ListRegistrosAlertas();
                    ArrayList<HistorialRegistros> list          = new ArrayList<HistorialRegistros>();

                    //sql = "SELECT " + KeyIdFacebook + "," + KeyIdTipoAlerta + "," + KeyLat + "," + KeyLng + "," + KeyFechaHora + " FROM " + TbRegistroAlerta;
                    sql = getStmtSql(CRUD.SELECT, new String[]{KeyIdFacebook, KeyIdTipoAlerta, KeyLat, KeyLng, KeyFechaHora}, TbRegistroAlerta);
                    cursor = db.rawQuery(sql, null);
                    if(cursor.getCount() > 0)
                    {
                        int NumeroFila = 0;
                        cursor.moveToFirst();
                        while ((!cursor.isAfterLast()) && NumeroFila < cursor.getCount())
                        {
                            NumeroFila++;
                            HistorialRegistros historial = new HistorialRegistros();               // BAD PRACTICE
                            historial.setIdFacebook(cursor.getString(0));
                            historial.setIdTipoAlerta(cursor.getString(1));
                            historial.setLatitud(cursor.getString(2));
                            historial.setLongitud(cursor.getString(3));
                            historial.setFechaHora(cursor.getString(4));
                            list.add(historial);
                            cursor.moveToNext();
                        }
                        cursor.close();
                        result = "1";
                        // ENVIAMOS ARRAYLIST AL BEAN , PARA OBTENERLO DESDE OTRAS CLASES
                        listRegistrosAlertas.setListHistorial(list);
                    }
                }finally
                {
                    return result;
                }

            case 4: // LISTA REGISTROS DE LA TABLA DENUNCIA PARA MOSTRARLOS EN UN LISTADO
                try
                {
                    ListRegistrosDenuncias listRegistrosDenuncias   = new ListRegistrosDenuncias();
                    ArrayList<RegistroDenuncias> list              = new ArrayList<RegistroDenuncias>();

                    sql = getStmtSql(CRUD.SELECT, new String[]{KeyFechaHora, KeyDescripcion, KeyIdTipoDenuncia, KeyIdFacebook, KeyImagenDenuncia, KeyFlagServidor}, TbRegistroDenuncia);
                    cursor = db.rawQuery(sql, null);
                    if(cursor.getCount() > 0)
                    {
                        int NumeroFila = 0;
                        cursor.moveToFirst();
                        while ((!cursor.isAfterLast()) && NumeroFila < cursor.getCount())
                        {
                            NumeroFila++;
                            RegistroDenuncias registroDenuncias = new RegistroDenuncias();               // BAD PRACTICE
                            registroDenuncias.setFechaHoraSqlite(cursor.getString(0));
                            registroDenuncias.setDescripcion(cursor.getString(1));
                            registroDenuncias.setIdTipoDenuncia(cursor.getString(2));
                            registroDenuncias.setIdFacebookSqlite(cursor.getString(3));
                            registroDenuncias.setImgDenuncia(cursor.getBlob(4));    // byte
                            registroDenuncias.setFlagServidorSqlite(cursor.getString(5));
                            list.add(registroDenuncias);
                            cursor.moveToNext();
                        }
                        cursor.close();
                        result = "1";
                        // ENVIAMOS ARRAYLIST AL BEAN , PARA OBTENERLO DESDE OTRAS CLASES
                        listRegistrosDenuncias.setListRegistrosDenuncias(list);
                    }
                }finally
                {
                    return result;
                }
        }
        return result;
    }



    /**
     * FUNCIÓN PARA RETORNAR CADENA SERIALIZADA DE UNA SENTENCIA SQLITE
     * #indice  int indice de que tipo de sentencia se debe construir (INSERT/UPDATE/DELETE)
     * <ul>
     *     <li> 1 - INSERT </li>
     *     <li> 2 - UPDATE </li>
     *     <li> 3 - DELETE </li>
     *     <li> 4 - SELECT </li>
     * </ul>
     * #params  String[] que contendra los parametros de Inserción o Actualización.
     * #tabla   String nombre de la tabla
     * #return  String sentencia Sqlite
     **/

    private String getStmtSql(CRUD indiceCRUD, String[] params, String tabla)
    {
        // SENTENCIA QUE SE RETORNARÁ
        String sql = null ;
        // CADENA QUE CONTENDRA EL NUMERO DE COLUMNAS A INSERTAR
        String columnasInsert = "";

        if(indiceCRUD == indiceCRUD.INSERT)
            sql = "INSERT OR REPLACE INTO " + tabla + "( ";
        if(indiceCRUD == indiceCRUD.UPDATE)
            sql = "UPDATE " + tabla + " SET ";
        if(indiceCRUD == indiceCRUD.SELECT)
            sql = "SELECT ";

        for (int i= 0; i < params.length ; i++)
        {

            if (indiceCRUD == indiceCRUD.INSERT)
            {
                columnasInsert = columnasInsert + " ?," ;
                if (i < params.length -1)       //  REGISTRO COMÚN
                {
                    sql = sql + params[i] + ", ";
                }else if(i == params.length -1) //  ÚLTIMO REGISTRO
                {
                    sql = sql + params[i] + ") VALUES (" + columnasInsert.substring(0, columnasInsert.length() -1 ) + ")";
                }
            }else if(indiceCRUD == indiceCRUD.UPDATE)
            {
                if (i < params.length -1)       //  REGISTRO COMÚN
                {
                    sql = sql + params[i] + " = ?,";
                }
                else if(i == params.length -1)//  ÚLTIMO REGISTRO
                {
                    sql = sql.substring(0, sql.length() -1) + " WHERE " + params[i] + " = ?" ;
                }
            }else if(indiceCRUD == indiceCRUD.SELECT)
            {
                if (i < params.length -1)       //  REGISTRO COMÚN
                {
                    sql = sql + params[i] + ",";
                }
                else if(i == params.length -1)  //  ÚLTIMO REGISTRO
                {
                    //sql = sql.substring(0, sql.length() -1) + " FROM " + tabla ;
                    sql = sql + params[i] + " FROM " + tabla ;
                }
            }

        }
        return sql;
    }


    /**
     * BackUpDataBase EXPORTA BASE DE DATOS EN LA MEMORIA SD DEL CELULAR
     * #mNombreBD String NOMBRE DE LA BASE DE DATOS.
     * #mContext Context PARA OBTENER EL NOMBRE DEL PACKAGE.
     * #mDirectorioBD String NOMBRE DEL DIRECTORIO DONDE SE ALMACENARA EL ARCHIVO.
     **/
    //region -------FUNCIONES BÁSICAS Y/O COMPLEMENTARIAS--------
    private void BackUpDataBase() throws IOException
    {
        File file = new File(Environment.getExternalStorageDirectory(), mDirectorioBD);
        if(!file.exists())
            file.mkdirs();

        String inFileName = "/data/data/" + mContext.getPackageName() + "/databases/" + mNombreBD;
        file = new File(inFileName);
        FileInputStream fis = new FileInputStream(file);
        String outFileName = Environment.getExternalStorageDirectory() + "/"+ mDirectorioBD + "/" + mNombreBD + ".sqlite";
        OutputStream output = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0)
        {
            output.write(buffer, 0, length);
        }
        output.flush();
        output.close();
        fis.close();
    }


    //endregion
}