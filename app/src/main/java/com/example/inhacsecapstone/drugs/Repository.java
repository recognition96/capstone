package com.example.inhacsecapstone.drugs;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class Repository {

    private DrugItemDao mWordDao;
    private LiveData<List<DrugItem>> mAllItems;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    Repository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mWordDao = db.drugDao();
        mAllItems = mWordDao.getAlphabetizedWords();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<DrugItem>> getAllWords() {
        return mAllItems;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(DrugItem drug) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mWordDao.insert(drug);
        });
    }
}