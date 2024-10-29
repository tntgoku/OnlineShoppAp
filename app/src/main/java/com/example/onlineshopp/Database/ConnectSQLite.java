package com.example.onlineshopp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class ConnectSQLite extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="btlapp.db";
    public static String TABLE="ACCOUNT";
    public static String TABLE_1="PRODUCT";
    public static String TABLE_2="CATEGORY";
    public static String TABLE_3="CART";
    public static String TABLE_31="CARTDAIL";
    public static String TABLE_4="ORDERR";
    public static String TABLE_5="ORDERRDETAIL";
    public static String TABLE_6="CUSTOMER";
    public  static final int  DATABASE_VERSION=10;

    public ConnectSQLite(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }


    public ConnectSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS " + TABLE +
                "(ID_Customer TEXT , " +
                "Nameuser TEXT,"+
                "Password TEXT," +
                "Roleid INTEGER," +
                "UpdateAt TEXT," +
                "PRIMARY KEY (ID_Customer) ) ";

        String CREATE_TABLE1 = "CREATE TABLE IF NOT EXISTS " + TABLE_1 +
                " (ID_product INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Product_name TEXT, " +
                "ProductTypeID INTEGER , " +
                "Inventory INTEGER default null, " +
                "PriceOriginal REAL, " +
                "DiscountPrice INTEGER default null,"+
                "Images TEXT ,"+
                "Descr TEXT)";
        String CREATE_TABLE2="CREATE TABLE IF NOT EXISTS " + TABLE_2 +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID_Cate INTEGER UNIQUE, " +
                "Name_Cate TEXT ) ";
        String CREATE_TABLE3="CREATE TABLE IF NOT EXISTS " + TABLE_3 +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID_cart  TEXT,  "+
                "ID_Customer TEXT)";
        String CREATE_TABLE31="CREATE TABLE IF NOT EXISTS " + TABLE_31 +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID_cart  TEXT,"+
                "ID_product INTEGER,"+
                "Quantity INTEGER)";

        String CREATE_TABLE4="CREATE TABLE IF NOT EXISTS " + TABLE_4 +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID_ORDERR  TEXT,  "+
                "ID_customer TEXT,"+
                "Payment TEXT,"+
                "TOTAL INTEGER," +
                "DATETIMER TEXT )";
        String CREATE_TABLE5="CREATE TABLE IF NOT EXISTS " + TABLE_5 +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID_ORDER  TEXT,  "+
                "ID_PRODUCT INTEGER,"+
                "Quantity INTEGER)";
        String CREATE_TABLE6= "CREATE TABLE IF NOT EXISTS "+TABLE_6+
                "(ID_Customer TEXT PRIMARY KEY,"+
                "Name_cus TEXT,"+
                "dateBirth Text,"+
                "Phone TEXT,"+
                "Address TEXT,"+
                "Email TEXT,"+
                "updateDate TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE1);
        sqLiteDatabase.execSQL(CREATE_TABLE2);
        sqLiteDatabase.execSQL(CREATE_TABLE3);
        sqLiteDatabase.execSQL(CREATE_TABLE31);
        sqLiteDatabase.execSQL(CREATE_TABLE4);
        sqLiteDatabase.execSQL(CREATE_TABLE5);
        sqLiteDatabase.execSQL(CREATE_TABLE6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            onCreate(sqLiteDatabase);
            if(i1==DATABASE_VERSION){
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE);
                onCreate(sqLiteDatabase);
//                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_1);
//                onCreate(sqLiteDatabase);
//                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_2);
//                onCreate(sqLiteDatabase);
//                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TABLE_3);
//                onCreate(sqLiteDatabase);
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TABLE_6);
                onCreate(sqLiteDatabase);
                Log.v("ConnectSQLite","Update thanh cong SQLite");
            }
    }

    public String ID_cut(){
        SQLiteDatabase db=getReadableDatabase();
        StringBuilder stringBuilder=new StringBuilder();
        String query="SELECT SUBSTR(ID_Customer,3,LENGTH(ID_Customer)) FROM "+TABLE_6;
        Cursor cursor=db.rawQuery(query,null);
                int i=0;
        if (cursor.getCount()>0){
                while (cursor.moveToNext()){
                    stringBuilder.append(cursor.getString(0)).append("\t");
                    i++;
                }
        }
        return  stringBuilder.toString();
    }
    public String generateCustomerId(String nameID) {
        String result=null;
        String[] rows=ID_cut().split("\t");
        int max=Integer.parseInt(rows[0]);
        for (String row:rows){
            int min=Integer.parseInt(row.trim());
            if(min> max){
                max=min;
            }
        }
        max=max+1;
        if(max<10){
            result=nameID+"000"+String.valueOf(max);
        }else if(max<100) {
            result=nameID+"00"+String.valueOf(max);
        }
        else if(max<1000){
            result=nameID+"0"+String.valueOf(max);
        }else{
            result=nameID+String.valueOf(max);
        }
        return result;
    }


}
