package com.reviewmerce.shipping.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by onebuy on 2015-09-07.
 */
public class DbBasicLab {
    static String mTag = "BasicDataDB";
    public static int DATABASE_VERSION = 1;
    protected DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private final Object syncObj1= new Object();
    private Context mAppContext=null;
    ////////////////////////////////////////////////////////////////////////////
    protected boolean mDbisOpened=false;
    protected String mDbTable="";
    protected String mDbName="";
    public DbBasicLab(Context context) {
        mDbisOpened = false;
        mAppContext = context;
    }
    public void setDbTable(String sTable)
    {
        mDbTable = sTable;
    }
    public String getDbName()
    {
        return mDbName;
    }
    public void setDbName(String sName)
    {
        mDbName = sName;
    }
    public String getDbTable()
    {
        return mDbTable;
    }
    public void setTag(String sTag)
    {
        mTag = sTag;
    }
    public String getTag()
    {
        return mTag;
    }
    ///////////////////////////////////////////////////////////////////////////////////////


    protected Cursor rawQuery(String SQL) throws Exception{
        synchronized (syncObj1)
        {
            Log.i(mTag, "\nexecuteQuery called.\n");
            Cursor c1 = null;
            c1 = db.rawQuery(SQL, null);
            return c1;
        }
    }

    protected void execSQL(String SQL) throws Exception{
        synchronized (syncObj1) {
            db.execSQL(SQL);
        }
    }

    public void openDatabase() throws Exception {
        try {
            dbHelper = new DatabaseHelper(mAppContext);
            db = dbHelper.getWritableDatabase();
            mDbisOpened= true;
            Log.i(mTag, "basic database is open.");
        }catch (Exception e)
        {
            mDbisOpened = false;
            Log.i(mTag, "basic database is not open.");
            throw e;
        }
    }

    public void createTable(String sql) throws Exception{
        db.execSQL(sql);
    }

    public void closeDatabase() {
        Log.i(mTag, "closing database [" + mDbName + "].");
        db.close();
    }

    public Cursor loadDatabase(String loadSql) throws Exception{
        synchronized (syncObj1) {
            if (mDbisOpened == false) {
                openDatabase();
            }
            Cursor cursor = rawQuery(loadSql);
            return cursor;
        }

    }
    public void saveDatabase(String sTable, ContentValues cv)throws Exception{
        synchronized (syncObj1) {
            //insert into USD values ("2012-07-12",	1171, 	0.65);
            if (mDbisOpened== false) {
                openDatabase();
            }
            try{

                db.insertWithOnConflict(sTable,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
            }
            catch (Exception e) {
                throw e;
            }
        }
    }

    public void deleteCreateTable(String sTable,String sql) throws Exception{
        db.execSQL("drop table if exists " + sTable);
        createTable(sql);
    }
    public boolean isExistTable(String sTable)
    {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name ='"+sTable+"'";
        try {
            Cursor cursor = rawQuery(sql);
        }catch (Exception e)
        {
            return false;
        }
        return true;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        //private static final String PACKAGE_DIR = "/data/data/com.reviewmerce.exchange/databases";

        public DatabaseHelper(Context context) {
            super(context, mDbName, null, DATABASE_VERSION);
            initialize(context);
        }

        public void initialize(Context ctx) {

        }

        public void onCreate(SQLiteDatabase db) {
        }

        public void onOpen(SQLiteDatabase db) {
            Log.i(mTag,"opened database [" + mDbName + "].");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            Log.i(mTag,"Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        }
    }

}
