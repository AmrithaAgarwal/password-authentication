package com.example.admin.fuzzauthentication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="details";
    public static final String user_table_1 = "reg_info";
    public static final String col_10 = "Sr_no";
    public static final String col_11 = "UID";
    public static final String col_12 = "Email_ID";
    public static final String col_13 = "Password";
    SQLiteDatabase db;
    private static final String TABLE_CREATE="create table "+user_table_1+"(" +col_10+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+col_11+" VARCHAR not null, "+col_12+" VARCHAR not null,"+col_13+" VARCHAR not null);";


    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_CREATE);
        this.db=db;

    }

    public void insertData(Contact c)
    {
        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(col_11,c.getUid());
        values.put(col_12,c.getEmail());
        values.put(col_13,c.getPass());

        db.insert(user_table_1,null,values);
        db.close();
    }

    public String searchPass(String uid)
    {
        db=this.getReadableDatabase();
        String query="select * from "+user_table_1+";";
        Cursor cursor=db.rawQuery(query,null);
        String a,b;
        b="not found";
        if (cursor.moveToFirst())
        {

            do {
                a=cursor.getString(1);

                if(a.equals(uid))
                {

                    b=cursor.getString(3);
                    break;

                }

            }
            while (cursor.moveToNext());
        }


        return b;


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS "+ user_table_1);
        this.onCreate(db);


    }
}


