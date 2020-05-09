package com.example.inhacsecapstone.drugs;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// singleton 설계 방식
@Database(entities = {MedicineEntity.class, TakesEntity.class}, version = 1, exportSchema = true) //exportSchema 수정
public abstract class AppDatabase extends RoomDatabase {

    public abstract ListDao ListDao();
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                ListDao listDao = INSTANCE.ListDao();
                listDao.deleteMedicineAll();
                listDao.deleteTakesAll();
                MedicineEntity item1 = new MedicineEntity("11111111", "포크랄시럽", 30, "https://www.health.kr/images/ext_images/pack_img/P_A11AGGGGA5864_01.jpg",
                        1, "3개", 3, 10, 0, "불면증, 수술 전 진정");
                listDao.insert(item1);

                TakesEntity take1 = new TakesEntity("11111111", "2020.5.9", "12:10");
                listDao.insert(take1);
            });
        }
    };
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }
}