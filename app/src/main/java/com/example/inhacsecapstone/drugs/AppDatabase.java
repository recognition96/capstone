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
    private static String databaseName = "app_database";
    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    private AppDatabase(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databaseName, factory, version);
    }

    public static AppDatabase getDataBase(Context context, SQLiteDatabase.CursorFactory factory, int version)
    {
        if(INSTANCE == null)
        {
            synchronized (AppDatabase.class){
                INSTANCE = new AppDatabase(context, factory, version);
            }
        }
        return INSTANCE;
    }
    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE medicine_list (code INTEGER, " +
                "name TEXT , "+
                "amount INTEGER,"+
                "image TEXT,"+
                "des TEXT,"+
                "category INTEGER,"+
                "single_dose TEXT,"+
                "daily_dose INTEGER,"+
                "number_of_day_takens INTEGER,"+
                "warning INTEGER)");
        db.execSQL("CREATE TABLE takes ("+"" +
                "code INTEGER, " +
                "day TEXT , "+
                "time TEXT)");
    }
    public void init() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM medicine_list");
        db.execSQL("DELETE FROM takes");

        Medicine medi = new Medicine(11111111, "포크랄시럽", 30, "https://www.health.kr/images/ext_images/pack_img/P_A11AGGGGA5864_01.jpg","불면증, 수술 전 진정"
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

        db.execSQL("INSERT INTO medicine_list VALUES("+ medicine.getCode() + ", '"+
                medicine.getName() + "',"+
                medicine.getAmount() + ",'" +
                medicine.getImage() + "','" +
                medicine.getDesc() + "'," +
                medicine.getCategory() + ",'" +
                medicine.getSingleDose() + "'," +
                medicine.getDailyDose() + "," +
                medicine.getNumberOfDayTakens() + "," +
                medicine.getWarning()+")");
        db.close();
    }
    public void insert(Takes take) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO takes VALUES("+ take.getCode() + ",'" + take.getDay() + "','" + take.getTime() + "')");
        db.close();
    }
   /* public void update(String item, int price) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE MONEYBOOK SET price=" + price + " WHERE item='" + item + "';");
        db.close();
    }*/

   /* public void delete(String item) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM MONEYBOOK WHERE item='" + item + "';");
        db.close();
    } */

    public ArrayList<Medicine> getAllMedicine() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Medicine> result = new ArrayList<Medicine>();

        try{
            Cursor cursor = db.rawQuery("SELECT * FROM medicine_list", null);
            while (cursor.moveToNext()) {
                Medicine current = new Medicine(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getInt(8),
                        cursor.getInt(9));
                result.add(current);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
    public ArrayList<Takes> getAllTakes() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Takes> result = new ArrayList<Takes>();
        try{
            Cursor cursor = db.rawQuery("SELECT * FROM takes", null);
            while (cursor.moveToNext()) {
                Takes current = new Takes(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2));
                result.add(current);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<Takes> gettakesAtDay(String day) {
            SQLiteDatabase db = getReadableDatabase();
            ArrayList<Takes> result = new ArrayList<Takes>();

            try{
                Cursor cursor = db.rawQuery("SELECT * FROM takes WHERE day = '" + day + "'", null);
                while (cursor.moveToNext()) {
                    Takes current = new Takes(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2));
                    result.add(current);
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return result;
    }
    public ArrayList<Medicine> getMedicineAtDay(String day) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Medicine> result = new ArrayList<Medicine>();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM medicine_list INNER JOIN takes ON medicine_list.code = takes.code WHERE day = '" + day + "'", null);
            while (cursor.moveToNext()) {
                Medicine current = new Medicine(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getInt(8),
                        cursor.getInt(9));
                result.add(current);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return result;
    }
}

