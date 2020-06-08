package com.example.inhacsecapstone.drugs;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;
import com.example.inhacsecapstone.cameras.CameraActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AppDatabase extends SQLiteOpenHelper {
    private static AppDatabase INSTANCE;
    private static String databaseName = "app_database";
    private static SQLiteDatabase.CursorFactory factory = null;
    private static int version = 1;

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    private AppDatabase(Context context) {
        super(context, databaseName, factory, version);
    }

    public static AppDatabase getDataBase(Context context) {
                if (INSTANCE == null) {
                        synchronized (AppDatabase.class) {
                            INSTANCE = new AppDatabase(context);
                        }
        }
        return INSTANCE;
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE medicine_list (code INTEGER PRIMARY KEY, " +
                "name TEXT , " +
                "image TEXT," +
                "effect TEXT," +
                "usage TEXT," +
                "category INTEGER," +
                "single_dose TEXT," +
                "daily_dose INTEGER," +
                "number_of_day_takens INTEGER," +
                "warning INTEGER, "+
                "start_day TEXT)");
        db.execSQL("CREATE TABLE taked (" + "" +
                "code INTEGER, " +
                "day TEXT, " +
                "time TEXT," +
                "PRIMARY KEY (code, day, time))");
        db.execSQL("CREATE TABLE will_take (" +
                "code INTEGER, " +
                "time TEXT)");
        db.execSQL("CREATE TABLE temp_time (" +
                "code INTEGER," +
                "day TEXT,"+
                "time TEXT)");
    }

    public void init() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM medicine_list");
        db.execSQL("DELETE FROM taked");
        db.execSQL("DELETE FROM will_take");
        db.execSQL("DELETE FROM temp_time");

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int date = calendar.get(Calendar.DATE);

        String cur = Integer.toString(year) + "." + Integer.toString(month) + "." + Integer.toString(date);

        Medicine medi = new Medicine(11111111, "포크랄시럽", "https://www.health.kr/images/ext_images/pack_img/P_A11AGGGGA5864_01.jpg", "불면증, 수술 전 진정", "1일 1회 복용"
                , 0, "1개", 3, 10, 0, cur);
        Takes take = new Takes(11111111, "2020.5.9", "12:10");
        insert(medi);
        insert(take);
        insertWillTake(11111111, "14:32");
        insertWillTake(11111111, "19:10");
        insertTempTake(11111111, "14:32");
        insertTempTake(11111111, "19:10");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // Medicine 관련 함수들
    public void insert(Medicine medicine) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO medicine_list VALUES(" + medicine.getCode() + ", '" +
                    medicine.getName() + "','" +
                    medicine.getImage() + "','" +
                    medicine.getEffect() + "','" +
                    medicine.getUsage() + "'," +
                    medicine.getCategory() + ",'" +
                    medicine.getSingleDose() + "'," +
                    medicine.getDailyDose() + "," +
                    medicine.getNumberOfDayTakens() + "," +
                    medicine.getWarning() + ",'" +
                    medicine.getStartDay() + "')");
        db.close();
    }
    public void deleteAllForCode(int code){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM medicine_list WHERE code = " + code);
        db.execSQL("DELETE FROM taked WHERE code = " + code);
        db.execSQL("DELETE FROM will_take WHERE code = " + code);
        db.execSQL("DELETE FROM temp_time WHERE code = " + code);
        db.close();
    }
    public ArrayList<Medicine> getAllMedicine() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Medicine> result = new ArrayList<Medicine>();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM medicine_list", null);
            while (cursor.moveToNext()) {
                Medicine current = new Medicine(cursor.getInt(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("image")),
                        cursor.getString(cursor.getColumnIndex("effect")),
                        cursor.getString(cursor.getColumnIndex("usage")),
                        cursor.getInt(cursor.getColumnIndex("category")),
                        cursor.getString(cursor.getColumnIndex("single_dose")),
                        cursor.getInt(cursor.getColumnIndex("daily_dose")),
                        cursor.getInt(cursor.getColumnIndex("number_of_day_takens")),
                        cursor.getInt(cursor.getColumnIndex("warning")),
                        cursor.getString(cursor.getColumnIndex("start_day")));
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
                Medicine current = new Medicine(cursor.getInt(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("image")),
                        cursor.getString(cursor.getColumnIndex("effect")),
                        cursor.getString(cursor.getColumnIndex("usage")),
                        cursor.getInt(cursor.getColumnIndex("category")),
                        cursor.getString(cursor.getColumnIndex("single_dose")),
                        cursor.getInt(cursor.getColumnIndex("daily_dose")),
                        cursor.getInt(cursor.getColumnIndex("number_of_day_takens")),
                        cursor.getInt(cursor.getColumnIndex("warning")),
                        cursor.getString(cursor.getColumnIndex("start_day")));
                result.add(current);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    public Medicine getMedicine(int code){
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM medicine_list WHERE code = " + code + "", null);
            while (cursor.moveToNext()) {
                Medicine current = new Medicine(cursor.getInt(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("image")),
                        cursor.getString(cursor.getColumnIndex("effect")),
                        cursor.getString(cursor.getColumnIndex("usage")),
                        cursor.getInt(cursor.getColumnIndex("category")),
                        cursor.getString(cursor.getColumnIndex("single_dose")),
                        cursor.getInt(cursor.getColumnIndex("daily_dose")),
                        cursor.getInt(cursor.getColumnIndex("number_of_day_takens")),
                        cursor.getInt(cursor.getColumnIndex("warning")),
                        cursor.getString(cursor.getColumnIndex("start_day")));
                return current;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // take 관련 함수들
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
    public ArrayList<Takes> getAllTakes() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Takes> result = new ArrayList<Takes>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM taked", null);
            while (cursor.moveToNext()) {
                Takes current = new Takes(cursor.getInt(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("day")),
                        cursor.getString(cursor.getColumnIndex("time")));
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
                Takes current = new Takes(cursor.getInt(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("day")),
                        cursor.getString(cursor.getColumnIndex("time")));
                result.add(current);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    // will_take 관련 함수들
    public ArrayList<String> getWillTakeAtMedi(int code)
    {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> result = new ArrayList<String>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM will_take WHERE code=" + code + " ORDER BY time ASC", null);
            while (cursor.moveToNext()) {
                result.add(cursor.getString(cursor.getColumnIndex("time")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    public void insertWillTake(int code, String time){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO will_take VALUES("+ code + ", '" + time  +"')");
        db.close();
    }
    public void updateWillTake(int code, String time, String pre){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE will_take SET time = '" + time + "' WHERE code = " + code + " AND time = '" + pre +"'");
        db.close();
    }
    public void deleteWillTake(int code, String time){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM will_take WHERE code = " + code + " AND time = '" + time + "'");
        db.close();
    }


    public void insertTempTake(int code, String time){
        SQLiteDatabase db = getWritableDatabase();
        Calendar calendar = Calendar.getInstance();

        String day = Integer.toString(calendar.get(Calendar.YEAR)) + "." + Integer.toString(calendar.get(Calendar.MONTH) + 1) + "." + Integer.toString(calendar.get(Calendar.DATE));
        db.execSQL("INSERT INTO temp_time VALUES("+ code + ", '" + day + "', '" + time  +"')");
        db.close();
    }
    public void updateTempTake(int code, String time, String pre){
        SQLiteDatabase db = getWritableDatabase();
        Calendar calendar = Calendar.getInstance();

        String day = Integer.toString(calendar.get(Calendar.YEAR)) + "." + Integer.toString(calendar.get(Calendar.MONTH) + 1) + "." + Integer.toString(calendar.get(Calendar.DATE));
        db.execSQL("UPDATE temp_time SET time = '"+ time + "' WHERE code = " + code + " AND time = '" + pre + "' AND day = '" + day + "'");
        db.close();
    }

    public void deleteTempTake(int code, String time){
        SQLiteDatabase db = getWritableDatabase();
        Calendar calendar = Calendar.getInstance();

        String day = Integer.toString(calendar.get(Calendar.YEAR)) + "." + Integer.toString(calendar.get(Calendar.MONTH) + 1) + "." + Integer.toString(calendar.get(Calendar.DATE));
        db.execSQL("DELETE FROM temp_time WHERE code = " + code + " AND time = '" + time + "' AND day = '" + day + "'");
        db.close();
    }

    public void setTempTime()
    {
        SQLiteDatabase dbwrite = getWritableDatabase();
        SQLiteDatabase dbread = getReadableDatabase();
        Calendar current = Calendar.getInstance();
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH)+1;
        int date = current.get(Calendar.DATE);

        String str = Integer.toString(year) + "." + Integer.toString(month) + "." + Integer.toString(date);
        try{
            dbwrite.execSQL("DELETE FROM temp_time");
            Cursor cursor = dbread.rawQuery("SELECT * FROM medicine_list A INNER JOIN will_take B ON A.code = B.code", null);
            while(cursor.moveToNext())
            {
                int code = cursor.getInt(cursor.getColumnIndex("code"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                int number_of_takens = cursor.getInt(cursor.getColumnIndex("number_of_day_takens"));
                String cursor_day = cursor.getString(cursor.getColumnIndex("start_day"));
                String cursor_day_str[] = cursor_day.split("\\.");
                Calendar calendar = Calendar.getInstance();

                calendar.set(Integer.parseInt(cursor_day_str[0]), Integer.parseInt(cursor_day_str[1])-1, Integer.parseInt(cursor_day_str[2]), 0, 0, 0);
                calendar.add(Calendar.DATE, number_of_takens);
                int result = calendar.compareTo(current);
                if(result > 0)
                    dbwrite.execSQL("INSERT INTO temp_time VALUES(" + code + ", '"+ str + "', '" + time +"')");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public HashMap<String, ArrayList<Integer>> getRecentAlarmInfo()
    {
        SQLiteDatabase db = getReadableDatabase();
        HashMap<String, ArrayList<Integer>> result = new HashMap<String, ArrayList<Integer>>();

        Calendar calendar = Calendar.getInstance();
        int check = -1;

        int criterion = calendar.get(Calendar.HOUR_OF_DAY )*100 + calendar.get(Calendar.MINUTE);
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM medicine_list A INNER JOIN temp_time B ON A.code = B.code ORDER BY time ASC", null);
            while(cursor.moveToNext())
            {
                int code = cursor.getInt(cursor.getColumnIndex("code"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String hour_min[] = time.split(":");
                int prior = Integer.parseInt(hour_min[0])*100 + Integer.parseInt(hour_min[1]);

                if(criterion < prior)
                {

                    if(prior != check && check != -1)
                        break;

                    if(result.isEmpty())
                    {
                        check = prior;
                        result.put(time, new ArrayList<Integer>());
                        result.get(time).add(code);
                    }
                    else
                        result.get(time).add(code);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return result;
    }
}