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

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    Repository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mListDao = db.ListDao();
        mAllDrugs = mListDao.getAllMedicine();
        mAllTakes = mListDao.getAllTakes();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<MedicineEntity>> getAllDrugs() {
        return mAllDrugs;
    }
    LiveData<List<TakesEntity>> getAllTakes() {
        return mAllTakes;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    LiveData<List<MedicineEntity>> getMediAtDay(String day){
        return mListDao.getMedicineAtDay(day);
    }
    LiveData<List<TakesEntity>> getTakesAtDay(String day){
        return mListDao.getTakesAtDay(day);
    }
    void insert(MedicineEntity drug) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mListDao.insert(drug);
        });
    }
    void insert(TakesEntity take){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mListDao.insert(take);
        });
    }
    void update(TakesEntity take){
        AppDatabase.databaseWriteExecutor.execute(() ->{
            mListDao.update(take);
        });
    }
}