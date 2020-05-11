package com.example.inhacsecapstone.drugs;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.inhacsecapstone.Entity.ListDao;
import com.example.inhacsecapstone.Entity.MedicineEntity;
import com.example.inhacsecapstone.Entity.TakesEntity;

import java.util.List;

// viewmodel은 repository로 부터 얻은 정보가 어디서 왔는지 알 필요가 없다.
public class Repository {
    private String TAG = "Repository";
    private ListDao mListDao;
    private LiveData<List<MedicineEntity>> mAllDrugs;
    private LiveData<List<TakesEntity>> mAllTakes;
    static private Repository INSTANCE;
    Repository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mListDao = db.ListDao();
        mAllDrugs = mListDao.getAllMedicine();
        mAllTakes = mListDao.getAllTakes();
    }

    LiveData<List<MedicineEntity>> getAllDrugs() {
        return mAllDrugs;
    }
    LiveData<List<TakesEntity>> getAllTakes() {
        return mAllTakes;
    }

    LiveData<List<MedicineEntity>> getMediAtDay(String day){
        return mListDao.getMedicineAtDay(day);
    }
    LiveData<List<TakesEntity>> getTakesAtDay(String day){
        return mListDao.getTakesAtDay(day);
    }
    public void insert(MedicineEntity drug) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                mListDao.insert(drug);
                Log.d(TAG, drug.getName());
            }catch(Exception ex)
            {
                ex.printStackTrace();
            }
            //mAllDrugs = mListDao.getAllMedicine();
        });
    }
    public void insert(TakesEntity take){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mListDao.insert(take);
            //mAllTakes = mListDao.getAllTakes();
        });
    }
    public void update(TakesEntity take){
        AppDatabase.databaseWriteExecutor.execute(() ->{
            mListDao.update(take);
            //mAllTakes = mListDao.getAllTakes();
        });
    }
    public void deleteAll(){
        AppDatabase.databaseWriteExecutor.execute(() ->{
            mListDao.deleteMedicineAll();
            mListDao.deleteTakesAll();
            MedicineEntity item1 = new MedicineEntity("11111111", "포크랄시럽", 30, "https://www.health.kr/images/ext_images/pack_img/P_A11AGGGGA5864_01.jpg",
                    1, "3개", 3, 10, 0, "불면증, 수술 전 진정");
            mListDao.insert(item1);

            TakesEntity take1 = new TakesEntity("11111111", "2020.5.9", "12:10");
            mListDao.insert(take1);
        });
    }
    public static Repository getRepository(Application application) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = new Repository(application);
            }
        }
        return INSTANCE;
    }
}