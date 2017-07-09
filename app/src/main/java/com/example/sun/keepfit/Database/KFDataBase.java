package com.example.sun.keepfit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016/12/24.
 */
public class KFDataBase extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "steps";
    private static final String TABLE_NAME = "KFStep";
    private static final int VERSION = 1;

    private static KFDataBase instance;
    private static final AtomicInteger openCounter = new AtomicInteger();

    //建表语句
    private static final String CREATE_TABLE="create table " + TABLE_NAME + " (date INTEGER, steps INTEGER)";

    public  KFDataBase(Context context){
        super(context, DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor query(final String[] columns, final String selection,
                        final String[] selectionArgs, final String groupBy, final String having,
                        final String orderBy, final String limit) {
        return getReadableDatabase()
                .query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public static synchronized KFDataBase getInstance(final Context c) {
        if (instance == null) {
            instance = new KFDataBase(c.getApplicationContext());
        }
        openCounter.incrementAndGet();
        return instance;
    }

    /*
    * 根据日期获取步数
    */
    public int getSteps(final long date) {
        Log.d("1","getsteps方法");
        Cursor c = getReadableDatabase().query(TABLE_NAME, new String[]{"steps"}, "date = ?",
                new String[]{String.valueOf(date)}, null, null, null);
        c.moveToFirst();
        int re;
        if (c.getCount() == 0) re = Integer.MIN_VALUE;
        else re = c.getInt(0);
        c.close();
        return re;
    }

    public int getCurrentSteps() {
        int re = getSteps(-1);
        return re == Integer.MIN_VALUE ? 0 : re;
    }

    public void saveCurrentSteps(int steps) {

        ContentValues values = new ContentValues();
        values.put("steps", steps);
        if (getWritableDatabase().update(TABLE_NAME, values, "date = -1", null) == 0) {
            Log.d("test1", "进来了?==0???");
            values.put("date", -1);
            getWritableDatabase().insert(TABLE_NAME, null, values);
        }else{
            Log.d("test1", "写入了: ");
        }
    }

    public void insertNewDay(long date, int steps) {
        getWritableDatabase().beginTransaction();
        try {
            Cursor c = getReadableDatabase().query(TABLE_NAME, new String[]{"date"}, "date = ?",
                    new String[]{String.valueOf(date)}, null, null, null);
            if (c.getCount() == 0 && steps >= 0) {
                ContentValues values = new ContentValues();
                values.put("date", date);
                // use the negative steps as offset
                values.put("steps", -steps);
                getWritableDatabase().insert(TABLE_NAME, null, values);

                // add 'steps' to yesterdays count
                Calendar yesterday = Calendar.getInstance();
                yesterday.setTimeInMillis(date);
                yesterday.add(Calendar.DAY_OF_YEAR, -1);
                updateSteps(yesterday.getTimeInMillis(), steps);
            }
            c.close();
            getWritableDatabase().setTransactionSuccessful();
        } finally {
            getWritableDatabase().endTransaction();
        }
    }

    public void updateSteps(final long date, int steps) {
        getWritableDatabase().execSQL(
                "UPDATE " + TABLE_NAME + " SET steps = steps + " + steps + " WHERE date = " + date);
    }
}
