package com.example.rutbiton.zeeksrorertest;

        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.database.sqlite.SQLiteStatement;

        import java.util.Date;
        import java.util.StringTokenizer;

/**
 * Created by Quoc Nguyen on 13-Dec-16.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String store, String sum, String date, byte[] image,String category, String isCredit, String dueDate){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO INVOICE VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, store);
        statement.bindString(2, sum);
        statement.bindBlob(3, image);
        statement.bindString(4, date);
        statement.bindString(5,category);
        statement.bindString(6, isCredit);
        statement.bindString(7, dueDate);
        statement.executeInsert();
    }

    public void updateData(String store, String sum, String date, byte[] image,String category, String isCredit, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE INVOICE SET store = ?, sum = ?, image = ?, date = ?, category = ?, isCredit = ?  WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, store);
        statement.bindString(2, sum);
        statement.bindBlob(3, image);
        statement.bindString(4, date);
        statement.bindString(5, category);
        statement.bindString(6, isCredit);
        statement.bindDouble(7, (double)id);

        statement.execute();
        database.close();
    }

    public  void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM INVOICE WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }
    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public Cursor getDataWithParams(String sql, String[] arguments){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, arguments);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
