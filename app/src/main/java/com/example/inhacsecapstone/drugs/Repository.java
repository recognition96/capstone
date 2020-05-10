package com.example.inhacsecapstone.drugs;

import android.app.Application;
import android.widget.ListView;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

// viewmodel은 repository로 부터 얻은 정보가 어디서 왔는지 알 필요가 없다.
class Repository {

    private ListDao mListDao;
    private LiveData<List<MedicineEntity>> mAllDrugs;
    private LiveData<List<TakesEntity>> mAllTakes;

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
    void insert(MedicineEntity drug) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mListDao.insert(drug);
            //mAllDrugs = mListDao.getAllMedicine();
        });
    }
    void insert(TakesEntity take){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mListDao.insert(take);
            //mAllTakes = mListDao.getAllTakes();
        });
    }
    void update(TakesEntity take){
        AppDatabase.databaseWriteExecutor.execute(() ->{
            mListDao.update(take);
            //mAllTakes = mListDao.getAllTakes();
        });
    }
}