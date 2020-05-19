package com.example.classapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;


import static com.example.classapp.Database.UserMaster.Users.TABLE_NAME;

public class DBHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME  = "UserInfo.db";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null,1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE "+ TABLE_NAME + " ("+
                UserMaster.Users._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UserMaster.Users.COLUMN_NAME_USERNAME + " TEXT, " +
                UserMaster.Users.COLUMN_NAME_PASSWORD + " TEXT)";

        db.execSQL(SQL_CREATE_ENTRIES);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);

    }

    public boolean addInfo(String userName, String password){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserMaster.Users.COLUMN_NAME_USERNAME, userName);
        values.put(UserMaster.Users.COLUMN_NAME_PASSWORD, password);

        long newRowId = db.insert(TABLE_NAME, null, values);
        if(newRowId == -1) return false;
        else return true;
    }

    public List readAllInfo(){
        SQLiteDatabase db = getReadableDatabase();
        String[] projection  = {
                UserMaster.Users._ID,
                UserMaster.Users.COLUMN_NAME_USERNAME,
                UserMaster.Users.COLUMN_NAME_PASSWORD
        };
        String sortOrder = UserMaster.Users.COLUMN_NAME_USERNAME + " DESC";

        Cursor cursor = db.query(TABLE_NAME,projection,null,
                null,
                null,
                null,
                sortOrder);

        List userNames = new ArrayList<>();
        List passwords = new ArrayList<>();

        while (cursor.moveToNext()){
            String userName = cursor.getString(cursor.getColumnIndexOrThrow(UserMaster.Users.COLUMN_NAME_USERNAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(UserMaster.Users.COLUMN_NAME_PASSWORD));
            userNames.add(userName);
            passwords.add(password);
        }
        cursor.close();
        return userNames;
    }

    public void updateInfo(String userName, String password){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserMaster.Users.COLUMN_NAME_PASSWORD, password);
        String selection = UserMaster.Users.COLUMN_NAME_USERNAME + " LIKE ?";
        String[] selectionArgs ={userName};
        int count = db.update(TABLE_NAME, values, selection,selectionArgs);

    }

    public void deleteInfo(String userName){
        SQLiteDatabase db = getReadableDatabase();
        String selection = UserMaster.Users.COLUMN_NAME_USERNAME + " LIKE ?";

        String[] selectionArgs = {userName};
        db.delete(TABLE_NAME, selection, selectionArgs);

    }

    public boolean readInfo(String userName, String password){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME+" WHERE "+ UserMaster.Users.COLUMN_NAME_USERNAME+" = '"+userName+"' and "+ UserMaster.Users.COLUMN_NAME_PASSWORD+" = '"+password+"'", null);
        cursor.moveToFirst();
        if((cursor.getString(0)== userName) && (cursor.getString(1) == password))
            return true;
        else
            return false;
    }
//    public Cursor readInfo() {
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
//        return cursor;
//
//    }
}
