package com.helpfooter.steve.petcure.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by steve on 2016/5/4.
 */
public class DBUtil {

    private static final String TAG = "DBUtil";
    private static final String DATABASE_NAME = "PetCure160505_2.DB";
    private static final int DATABASE_VERSION = 1;
    private static Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private boolean hasOpened=false;
    private boolean inTransaction=false;


    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME,null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("db on create?", "yes");
            db.beginTransaction();
            try {


                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }

        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public DBUtil(Context ctx){
        this.mCtx=ctx;
    }

    public void recreate(){
        open();
        this.mDbHelper.onCreate(mDb);
        close();
    }

    public DBUtil open() throws SQLException {
        if(hasOpened==false){
            mDbHelper=new DatabaseHelper(mCtx);
            mDb=mDbHelper.getWritableDatabase();
            hasOpened=true;
        }
        return this;
    }

    public void beginTransaction() throws SQLException{
        mDb.beginTransaction();
        inTransaction=true;
    }

    public void endTransaction() throws SQLException{
        mDb.endTransaction();
        inTransaction=false;
    }

    public void setTransactionSuccessful() throws SQLException{
        mDb.setTransactionSuccessful();
        inTransaction=false;
    }

    public void close(){
        if(inTransaction==false){
            mDbHelper.close();
        }
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) throws SQLException{
        return mDb.rawQuery(sql, selectionArgs);
    }

    public void execSQL(String sql, Object[] bindArgs) throws SQLException{
        mDb.execSQL(sql, bindArgs);
    }


}
