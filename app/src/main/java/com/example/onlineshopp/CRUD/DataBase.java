package com.example.shoppe_food;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Date;

public class DataBase extends SQLiteOpenHelper {
    private Context context;

    private static final String DB_NAME = "QL_NEWS";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "News";
    private static final String ID_NEWS = "id_News";
    private static final String IMAGE_URL = "Image_News";
    private static final String NAME_NEWS = "Name_News";
    private static final String DESC_NEWS = "Desc_News";
    private static final String DATE_POST = "Date_Post";




    public DataBase(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }
    public DataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    // Them sua xoa
    public void ExecuteSQL(String sql){
        SQLiteDatabase  sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
    }
    // Doc du lieu
    public Cursor SelectData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;
        if (sqLiteDatabase  != null) {
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_table = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID_NEWS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IMAGE_URL + " TEXT, " +
                NAME_NEWS + " TEXT NOT NULL, " +
                DESC_NEWS + " TEXT," +
                DATE_POST + " TEXT " +
                ")";
        sqLiteDatabase.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addNews(String Image_News, String Name_News, String Date_Post, String Desc_News){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMAGE_URL, Image_News);
        values.put(NAME_NEWS, Name_News);
        values.put(DATE_POST, Date_Post);
        values.put(DESC_NEWS, Desc_News);

        long result = sqLiteDatabase.insert(TABLE_NAME, null, values);
        if(result == -1){
            Log.i("aaa", " that bai");
        }
        else{
            Log.i("aaa", " thanh cong");
        }
    }

    public void UpdateNews(Integer id_News, String Image_News, String Name_News, String Date_Post, String Desc_News){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_NEWS, id_News);
        values.put(IMAGE_URL, Image_News);
        values.put(NAME_NEWS, Name_News);
        values.put(DESC_NEWS, Desc_News);
        values.put(DATE_POST, Date_Post);
        long result = sqLiteDatabase.update(TABLE_NAME,values,ID_NEWS + "=?", new String[]{String.valueOf(id_News)});
        if (result == -1) {
            Log.i("Database", "Không thể cập nhật với ID: " + id_News);
        } else {
            Log.i("Database", "Cập nhật thành công với ID: " + id_News);
        }
    }
    public void DeleteNews(Integer id_News){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long result = sqLiteDatabase.delete(TABLE_NAME, ID_NEWS + "=?",  new String[]{String.valueOf(id_News)});
        if (result == -1) {
            Log.i("Database", "Xoá thất bại với ID: " + id_News);
        } else {
            Log.i("Database", "Xoá thành công với ID: " + id_News);
        }
    }

}
