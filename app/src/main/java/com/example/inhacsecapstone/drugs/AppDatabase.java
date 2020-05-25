package com.example.inhacsecapstone.drugs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;

import java.util.ArrayList;

public class AppDatabase extends SQLiteOpenHelper {
    private static AppDatabase INSTANCE;
    private static String databaseName = "app_database2";

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    private AppDatabase(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databaseName, factory, version);
    }

    public static AppDatabase getDataBase(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = new AppDatabase(context, factory, version);
            }
        }
        return INSTANCE;
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE medicine_list (code INTEGER PRIMARY KEY, " +
                "name TEXT , " +
                "amount INTEGER," +
                "image TEXT," +
                "effct TEXT," +
                "usage TEXT," +
                "category INTEGER," +
                "single_dose TEXT," +
                "daily_dose INTEGER," +
                "number_of_day_takens INTEGER," +
                "warning INTEGER)");
        db.execSQL("CREATE TABLE taked (" + "" +
                "code INTEGER, " +
                "day TEXT, " +
                "time TEXT," +
                "PRIMARY KEY (code, day, time))");
        db.execSQL("CREATE TABLE will_take (" + "" +
                "code INTEGER, " +
                "date TEXT," +
                "time TEXT," +
                "PRIMARY KEY (code, time))");
    }

    public void init() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM medicine_list");
        db.execSQL("DELETE FROM taked");
        db.execSQL("DELETE FROM will_take");

        Medicine medi = new Medicine(11111111, "포크랄시럽", 30, "https://www.health.kr/images/ext_images/pack_img/P_A11AGGGGA5864_01.jpg", "불면증, 수술 전 진정", "1일 1회 복용"
                , 0, "3개", 10, 0, 0);
        Takes take = new Takes(11111111, "2020.5.9", "12:10");
        insert(medi);
        insert(take);

    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(Medicine medicine) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO medicine_list VALUES(" + medicine.getCode() + ", '" +
                medicine.getName() + "'," +
                medicine.getAmount() + ",'" +
                medicine.getImage() + "','" +
                medicine.getEffect() + "','" +
                medicine.getUsage() + "'," +
                medicine.getCategory() + ",'" +
                medicine.getSingleDose() + "'," +
                medicine.getDailyDose() + "," +
                medicine.getNumberOfDayTakens() + "," +
                medicine.getWarning() + ")");
        db.close();
    }
    public void insert_will_take(int code, String date, ArrayList<String> will_take){
        SQLiteDatabase db = getWritableDatabase();
        for(String elem : will_take)
            db.execSQL("INSERT INTO will_take VALUES(" + code + ",  '" + date + "', '" + elem + "')");
    }
    public void insert(Takes take) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO taked VALUES(" + take.getCode() + ",'" + take.getDay() + "','" + take.getTime() + "')");
        db.close();
    }
    public void update(Takes take, String prevTime){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE taked SET day ='" + take.getDay() + "', time = '"+ take.getTime() + "' WHERE code = " + take.getCode() +
                " AND day='" + take.getDay() + "'AND time='" + prevTime+ "'");
    }

    public ArrayList<Medicine> getAllMedicine() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Medicine> result = new ArrayList<Medicine>();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM medicine_list", null);
            while (cursor.moveToNext()) {
                Medicine current = new Medicine(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getString(7),
                        cursor.getInt(8),
                        cursor.getInt(9),
                        cursor.getInt(10));
                result.add(current);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<Takes> getAllTakes() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Takes> result = new ArrayList<Takes>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM taked", null);
            while (cursor.moveToNext()) {
                Takes current = new Takes(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2));
                result.add(current);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<Takes> gettakesAtDay(String day) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Takes> result = new ArrayList<Takes>();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM taked WHERE day = '" + day + "'", null);
            while (cursor.moveToNext()) {
                Takes current = new Takes(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2));
                result.add(current);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<Medicine> getMedicineAtDay(String day) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Medicine> result = new ArrayList<Medicine>();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM medicine_list INNER JOIN taked ON medicine_list.code = taked.code WHERE day = '" + day + "'", null);
            while (cursor.moveToNext()) {
                Medicine current = new Medicine(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getString(7),
                        cursor.getInt(8),
                        cursor.getInt(9),
                        cursor.getInt(10));
                result.add(current);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}

